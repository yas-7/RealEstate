package com.realestate.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.app.model.FlatEntity;
import com.realestate.app.model.FlatPriceHistoryEntity;
import com.realestate.app.repository.FlatPriceHistoryRepository;
import com.realestate.app.repository.FlatRepository;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ScheduledTask {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private FlatPriceHistoryRepository flatPriceHistoryRepository;

//    @Scheduled(fixedRate = 60000)
    @Scheduled(cron = "0 0 0 * * ?")
    public void doTask() {
        try {
            String html = fetchHtml();
            List<FlatEntity> flats = parseHtml(html);
            flats.forEach(this::saveEntity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fetchHtml() {
        try {
            HttpGet request = new HttpGet("https://primkvartal.ru/ajax/filterOnSale?filter%5BtypeId%5D=1&page=1&loadAllPagesToCurrent=1");
            request.addHeader("accept", "application/json, text/javascript");
            request.addHeader("referer", "https://primkvartal.ru/onSale/flats");
            request.addHeader("x-requested-with", "XMLHttpRequest");

            ClassicHttpResponse response = httpClient.executeOpen(null, request, null);
            return EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<FlatEntity> parseHtml(String json) {
        JsonNode root;
        try {
            root = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (root.has("html")) {
            String html = root.get("html").asText();
            String wrappedHtml = "<table><tbody>" + html + "</tbody></table>";
            Document document = Jsoup.parse(wrappedHtml);
            Elements rows = document.select("tr");

            List<FlatEntity> flats = rows.stream()
                    .map(this::createEntity)
                    .filter(Objects::nonNull)
                    .limit(5)
                    .toList();

            System.out.println("Найдено квартир: " + flats.size());

            return flats;
        }

        System.out.println("Ответ не содержит ключа 'html'");

        return null;
    }

    private void saveEntity(FlatEntity flatEntity) {
        Optional<FlatEntity> optionalFlat = flatRepository.findByComplexIdAndBuildingAndNumber(
                flatEntity.getComplexId(),
                flatEntity.getBuilding(),
                flatEntity.getNumber()
        );

        if (optionalFlat.isEmpty()) {
            FlatEntity savedFlat = flatRepository.save(flatEntity);
            System.out.println("Квартира №: " + flatEntity.getId() + " сохранена");

            FlatPriceHistoryEntity historyEntity = new FlatPriceHistoryEntity();
            historyEntity.setFlatId(savedFlat.getId());
            historyEntity.setPriceTotal(savedFlat.getActualPriceTotal());
            historyEntity.setPricePerM2(savedFlat.getActualPricePerM2());

            flatPriceHistoryRepository.save(historyEntity);
            System.out.println("Добавлена новая запись истории цены новой Квартиры №: " + savedFlat.getId());

        } else {
            FlatEntity existedFlat = optionalFlat.get();
            System.out.println("Квартира №: " + existedFlat.getId() + " уже существует");

            existedFlat.setActualPriceTotal(flatEntity.getActualPriceTotal());
            existedFlat.setActualPricePerM2(flatEntity.getActualPricePerM2());

            flatRepository.save(existedFlat);
            System.out.println("Цена Квартиры №: " + existedFlat.getId() + " была обновлена");

            FlatPriceHistoryEntity historyEntity = new FlatPriceHistoryEntity();
            historyEntity.setFlatId(existedFlat.getId());
            historyEntity.setPriceTotal(existedFlat.getActualPriceTotal());
            historyEntity.setPricePerM2(existedFlat.getActualPricePerM2());

            flatPriceHistoryRepository.save(historyEntity);
            System.out.println("Добавлена новая запись истории цены существующей Квартиры №: " + existedFlat.getId());
        }
    }

    private FlatEntity createEntity(Element row) {
        Elements cells = row.select("td");
        if (cells.size() >= 9) {
            try {
                String building = cells.get(0).text().trim();
                int floor = Integer.parseInt(cells.get(1).text().trim());
                String number = cells.get(2).text().trim();

                int rooms;
                try {
                    rooms = Integer.parseInt(cells.get(3).text().trim());
                } catch (NumberFormatException e) {
                    rooms = 0;
                }

                double areaTotal = Double.parseDouble(cells.get(4).text().trim().replace(",", "."));
                int pricePerM2 = Integer.parseInt(cells.get(7).text().trim().replace(" ", ""));
                int priceTotal = Integer.parseInt(cells.get(8).text().trim().replace(" ", ""));

                FlatEntity flat = new FlatEntity();
                flat.setComplexId(1);
                flat.setBuilding(building);
                flat.setNumber(number);
                flat.setFloor(floor);
                flat.setRooms(rooms);
                flat.setAreaTotal(areaTotal);
                flat.setActualPriceTotal(priceTotal);
                flat.setActualPricePerM2(pricePerM2);

                return flat;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Пропущена строка, количество ячеек меньше 9: " + cells.size());
        return null;
    }
}
