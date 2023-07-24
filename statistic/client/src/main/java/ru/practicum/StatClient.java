package ru.practicum;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.model.HitDto;
import ru.practicum.model.StatDto;

import java.util.*;

@Service
public class StatClient extends BaseClient {
    public StatClient(@Value("${statserver.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> hit(HitDto hitDto) {
        System.out.println("Add hit to statistic");
        return post("/hit", hitDto);
    }

    public List<StatDto> getStat(String start,
                      String end,
                      List<String> uris,
                      Boolean unique) {
        System.out.println("Get statistics");
        String startParam = "start=" + start;
        String endParam = "end=" + end;
        String urisParam = null;
        String uniqueParam = null;
        String query = "/stats?" + startParam + "&" + endParam;
        if (uris != null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : uris) {
                stringBuilder.append("&uris=").append(s);
            }
            urisParam = stringBuilder.toString();
            query = query + urisParam;
        }
        if (unique != null) {
            uniqueParam = "&unique=" + unique;
            query = query + uniqueParam;
        }

        ResponseEntity<Object> response = get(query);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(response.getBody(), new TypeReference<List<StatDto>>() {});
    }
}
