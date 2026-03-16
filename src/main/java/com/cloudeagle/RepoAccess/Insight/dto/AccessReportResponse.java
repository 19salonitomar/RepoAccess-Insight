package com.cloudeagle.RepoAccess.Insight.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AccessReportResponse {

    private String organization;

    private LocalDateTime generatedAt;

    private List<UserAccessDTO> users;

}