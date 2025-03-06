package util.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.dto.APIClientParam;
import util.logger.MyLogger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APIClient {
    private final MyLogger logger;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper; // ObjectMapper를 멤버 변수로 추가


    public APIClient(MyLogger logger, ObjectMapper objectMapper) {
        this.logger = logger;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = objectMapper; // 초기화
        logger.info("Initializing API Client");
    }

    public String callAPI(APIClientParam param) throws IOException, InterruptedException {
        logger.info("Calling API: " + param.url());
        String body = convertBodyToString(param.body());

        HttpRequest request = buildHttpRequest(param, body);
        HttpResponse<String> response = sendRequest(request);

        return handleResponse(response);
    }
    private String convertBodyToString(Object body) throws JsonProcessingException {
        if (body == null || body.toString().isEmpty()) {
            return "";
        }
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            logger.severe("Error while serializing body to JSON: " + e.getMessage());
            throw e;
        }
    }
    private HttpRequest buildHttpRequest(APIClientParam param, String body) {
        return HttpRequest.newBuilder()
                .uri(URI.create(param.url()))
                .method(param.method(), HttpRequest.BodyPublishers.ofString(body))
                .headers(param.headers())
                .build();
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            logger.severe("Error during API call: " + e.getMessage());
            throw e;
        }
    }

    private String handleResponse(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        logger.info("API Response Status Code: " + statusCode);

        if (statusCode >= 200 && statusCode < 300) {
            return response.body();
        } else {
            String errorMessage = "API call failed with status code: " + statusCode + ", response: " + response.body();
            logger.severe(errorMessage);
            throw new RuntimeException(errorMessage); // 더 구체적인 예외 처리 필요
        }
    }

}
