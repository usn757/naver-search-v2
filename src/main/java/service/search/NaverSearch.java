package service.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.dto.NaverSearchResponse;
import model.dto.APIClientParam;
import model.dto.NaverSearchResult;
import util.api.APIClient;
import util.config.AppConfig;
import util.logger.MyLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NaverSearch {

    private final String clientId;
    private final String clientSecret;
    private final MyLogger logger;
    private final APIClient apiClient;
    private final ObjectMapper objectMapper;
    private static final String NAVER_API_URL = "https://openapi.naver.com/v1/search/news.json?query=%s";


    public NaverSearch(APIClient apiClient, AppConfig appConfig, MyLogger logger, ObjectMapper objectMapper) {
        this.apiClient = apiClient;
        this.clientId = appConfig.getNaverClientId();
        this.clientSecret = appConfig.getNaverClientSecret();
        this.logger = logger;
        this.objectMapper = objectMapper;

        if (clientId == null || clientSecret == null) {
            throw new RuntimeException("clientId and clientSecret are mandatory");
        }
        logger.info("NaverSearchAPI initialized");
    }

    public List<NaverSearchResult> searchByKeyword(String keyword) throws IOException, InterruptedException {
        logger.info("searchByKeyword API start");

        APIClientParam param = new APIClientParam(
                String.format(NAVER_API_URL, keyword),
                "GET",
                new HashMap<>(),
                "X-Naver-Client-Id",clientId,"X-Naver-Client-Secret",clientSecret
        );



        return objectMapper.readValue(apiClient.callAPI(param), NaverSearchResponse.class).items();
    }

}
