// util/config/AppConfig.java
package util.config;

import io.github.cdimascio.dotenv.Dotenv;

public class AppConfig {
    private final Dotenv dotenv;
    private final boolean isGitHubActions;

    public AppConfig() {
        isGitHubActions = "true".equalsIgnoreCase(System.getenv("GITHUB_ACTIONS"));
        dotenv = isGitHubActions ? null : Dotenv.load(); // GitHub Actions 환경에서는 dotenv를 로드하지 않음
    }

    private String getEnvironmentVariable(String name) {
        if (isGitHubActions) {
            // GitHub Actions 환경에서는 환경 변수에서 값을 가져옴
            return System.getenv(name);
        } else {
            String value = dotenv.get(name);
            if(value == null || value.isBlank()){
                throw new RuntimeException(name + "환경변수를 설정해주세요");
            }
            // 로컬 환경에서는 .env 파일에서 값을 가져옴
            return value;
        }
    }
    public String getNaverClientId() {
        return getEnvironmentVariable("NAVER_CLIENT_ID");
    }

    public String getNaverClientSecret() {
        return getEnvironmentVariable("NAVER_CLIENT_SECRET");
    }

    public String getSearchKeyword() {
        return getEnvironmentVariable("SEARCH_KEYWORD");
    }
    public String getLogLevel() {
        return getEnvironmentVariable("LOG_LEVEL");
    }
    public String getMode() {
        return getEnvironmentVariable("MODE"); // 기본값 PROD 설정 안함.
    }
}