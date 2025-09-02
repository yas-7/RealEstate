package com.realestate.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.app.model.FlatEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class PrimKvartalParser {

    private static final Logger LOG = LoggerFactory.getLogger(PrimKvartalParser.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<FlatEntity> parseHtml(String json) {
        JsonNode root;
        try {
            root = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (!root.has("html")) {
            LOG.warn("Ответ не содержит ключа 'html'");

            return Collections.emptyList();
        }

        String html = root.get("html").asText();
        String wrappedHtml = "<table><tbody>" + html + "</tbody></table>";
        Document document = Jsoup.parse(wrappedHtml);
        Elements rows = document.select("tr");

        List<FlatEntity> flats = rows.stream()
                .map(this::createEntity)
                .filter(Objects::nonNull)
                .toList();

        LOG.info("Найдено квартир: {} ", flats.size());

        return flats;

    }

    private FlatEntity createEntity(Element row) {
        Elements cells = row.select("td");
        int expectedRowsCount = 9;

        if (cells.size() < expectedRowsCount) {
            LOG.warn("Пропущена строка, количество ячеек меньше 9: {}", cells.size());
            return null;
        }

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
    }
}
