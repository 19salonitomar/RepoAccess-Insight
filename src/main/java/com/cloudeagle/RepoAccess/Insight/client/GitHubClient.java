package com.cloudeagle.RepoAccess.Insight.client;

import com.cloudeagle.RepoAccess.Insight.config.GitHubConfig;
import com.cloudeagle.RepoAccess.Insight.dto.CollaboratorDTO;
import com.cloudeagle.RepoAccess.Insight.dto.RepositoryDTO;
import com.cloudeagle.RepoAccess.Insight.util.RateLimitHandler;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GitHubClient {

    private final RestTemplate restTemplate;
    private final GitHubConfig config;
    private final RateLimitHandler rateLimitHandler;

    private HttpHeaders createHeaders() {

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(config.getToken());
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        return headers;
    }

    public List<RepositoryDTO> getRepositories() {

        List<RepositoryDTO> repositories = new ArrayList<>();

        int page = 1;

        while (true) {

            String url =
                    config.getBaseUrl()
                            + "/orgs/"
                            + config.getOrganization()
                            + "/repos?per_page="
                            + config.getPerPage()
                            + "&page="
                            + page;

            HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

            ResponseEntity<RepositoryDTO[]> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            RepositoryDTO[].class
                    );
            rateLimitHandler.handleRateLimit(response.getHeaders());

            RepositoryDTO[] repos = response.getBody();

            if (repos == null || repos.length == 0) {
                break;
            }

            repositories.addAll(Arrays.asList(repos));

            page++;
        }

        return repositories;
    }

    public List<CollaboratorDTO> getCollaborators(String repoName) {

        List<CollaboratorDTO> collaborators = new ArrayList<>();

        int page = 1;

        while (true) {

            String url =
                    config.getBaseUrl()
                            + "/repos/"
                            + config.getOrganization()
                            + "/"
                            + repoName
                            + "/collaborators?per_page="
                            + config.getPerPage()
                            + "&page="
                            + page;

            HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

            ResponseEntity<CollaboratorDTO[]> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            CollaboratorDTO[].class
                    );
            rateLimitHandler.handleRateLimit(response.getHeaders());
            CollaboratorDTO[] users = response.getBody();

            if (users == null || users.length == 0) {
                break;
            }

            collaborators.addAll(Arrays.asList(users));

            page++;
        }

        return collaborators;
    }

}