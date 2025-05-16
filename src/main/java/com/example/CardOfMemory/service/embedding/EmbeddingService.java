package com.example.CardOfMemory.service.embedding;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmbeddingService {
    @Value("${hf.api.token}")
    private String hfToken;
    @Value("${hf.model.name}")
    private String modelName;

    private final RestTemplate restTemplate;

    public List<Double> getEmbedding(String text) {
        String url = "https://router.huggingface.co/hf-inference/models/sentence-transformers/" + modelName +"/pipeline/feature-extraction";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(hfToken.trim());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, Object> payload = Collections.singletonMap("inputs", List.of(text));
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        @SuppressWarnings("unchecked")
        List<List<Double>> response = restTemplate.postForObject(url, request, List.class);

        return (response != null && !response.isEmpty())
                ? response.get(0)
                : Collections.emptyList();
    }
}
