package model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NaverSearchResult(
        String title,
        String originallink,
        String link,
        String description,
        String pubDate
) { }

