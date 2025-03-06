import com.fasterxml.jackson.databind.ObjectMapper;
import model.dto.NaverSearchResult;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import service.search.NaverSearch;
import util.api.APIClient;
import util.config.AppConfig;
import util.logger.MyLogger;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        MyLogger logger = new MyLogger(Application.class, appConfig);
        ObjectMapper objectMapper = new ObjectMapper();
        APIClient apiClient = new APIClient(new MyLogger(APIClient.class, appConfig), objectMapper);
        NaverSearch searchAPI = new NaverSearch(apiClient, appConfig, new MyLogger(NaverSearch.class, appConfig), objectMapper);
        String searchKeyword = appConfig.getSearchKeyword();

        logger.info("Searching for: " + searchKeyword);

        String filenameFormat = "%d_%s.xlsx";
        String filename = String.format(filenameFormat, System.currentTimeMillis(), searchKeyword);

        if ("DEV".equals(appConfig.getMode())) {
            filename = filename.replace(".xlsx", "_dev.xlsx");
        }

        Path workspacePath = Paths.get(System.getenv("GITHUB_WORKSPACE"));
        String filePath = workspacePath.resolve(filename).toString();

        try (
                Workbook workbook = new XSSFWorkbook();
                FileOutputStream fileOut = new FileOutputStream(filePath)) {
            List<NaverSearchResult> results = searchAPI.searchByKeyword(searchKeyword);
            logger.info("Found " + results.size() + " results");


            Sheet sheet = workbook.createSheet(searchKeyword);
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("날짜");
            headerRow.createCell(1).setCellValue("링크");
            headerRow.createCell(2).setCellValue("제목");
            headerRow.createCell(3).setCellValue("설명");
            int i = 0;
            for (NaverSearchResult item : results) {
                Row row = sheet.createRow(++i);
                row.createCell(0).setCellValue(item.pubDate());
                row.createCell(1).setCellValue(item.link());
                row.createCell(2).setCellValue(item.title());
                row.createCell(3).setCellValue(item.description());
            }
            workbook.write(fileOut);

        } catch (Exception e) {
            logger.severe(e.getMessage());
            e.printStackTrace(); // 스택 트레이스 출력
        }

    }
}