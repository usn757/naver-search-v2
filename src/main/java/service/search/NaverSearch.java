package service.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import model.dto.NaverSearchResponse;
import model.dto.APIClientParam;
import model.dto.NaverSearchResult;
import util.api.APIClient;
import util.logger.MyLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NaverSearch {

    private final String clientId;
    private final String clientSecret;
    private final MyLogger logger;
    private final APIClient apiClient;

    public NaverSearch() {
        Dotenv dotenv = Dotenv.load();
        clientId = dotenv.get("NAVER_CLIENT_ID");
        clientSecret = dotenv.get("NAVER_CLIENT_SECRET");
        if (clientId == null || clientSecret == null)
            throw new RuntimeException("clientId and clientSecret are mandatory");

        this.logger = new MyLogger(NaverSearch.class);
        this.apiClient = new APIClient();
//        logger.info(clientId);
//        logger.info(clientSecret);
        logger.info("NaverSearchAPI initialized");
    }

    public List<NaverSearchResult> searchByKeyword(String keyword) throws IOException, InterruptedException {
        logger.info("searchByKeyword API start");

        HashMap<String, String> body = new HashMap<>();
        APIClientParam param = new APIClientParam(
                "https://openapi.naver.com/v1/search/news.json?query=%s".formatted(keyword),
                "GET",
                body,
                "X-Naver-Client-Id",clientId,"X-Naver-Client-Secret",clientSecret
        );


        ObjectMapper objectmapper = new ObjectMapper();

        return objectmapper.readValue(apiClient.callAPI(param), NaverSearchResponse.class).items();
    }

}
