package model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NaverSearchResponse(List<NaverSearchResult> items,  String lastBuildDate) { }
