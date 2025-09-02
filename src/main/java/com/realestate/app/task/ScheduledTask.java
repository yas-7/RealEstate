package com.realestate.app.task;

import com.realestate.app.model.FlatEntity;
import com.realestate.app.service.FlatService;
import com.realestate.app.service.PrimKvartalParser;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ScheduledTask {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final FlatService flatService;
    private final PrimKvartalParser primKvartalParser;
    private final String url;

    @Autowired
    public ScheduledTask(FlatService flatService,
                         PrimKvartalParser primKvartalParser,
                         @Value("${complexes.primKvartal.url}") String url) {
        this.flatService = flatService;
        this.primKvartalParser = primKvartalParser;
        this.url = url;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void doTask() {
        String html = fetchHtml();
        List<FlatEntity> flats = primKvartalParser.parseHtml(html);
        flats.forEach(flatService::saveEntity);
    }

    private String fetchHtml() {
        try {
            HttpGet request = new HttpGet(url);
            request.addHeader("accept", "application/json, text/javascript");
            request.addHeader("referer", "https://primkvartal.ru/onSale/flats");
            request.addHeader("x-requested-with", "XMLHttpRequest");

            ClassicHttpResponse response = httpClient.executeOpen(null, request, null);
            return EntityUtils.toString(response.getEntity());

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
