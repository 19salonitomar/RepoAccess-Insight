package com.cloudeagle.RepoAccess.Insight.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccessDTO {

    private String username;

    private List<String> repositories;

}
