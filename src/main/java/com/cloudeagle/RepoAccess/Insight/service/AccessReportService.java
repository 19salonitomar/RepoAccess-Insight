package com.cloudeagle.RepoAccess.Insight.service;

import com.cloudeagle.RepoAccess.Insight.client.GitHubClient;
import com.cloudeagle.RepoAccess.Insight.config.GitHubConfig;
import com.cloudeagle.RepoAccess.Insight.dto.AccessReportResponse;
import com.cloudeagle.RepoAccess.Insight.dto.CollaboratorDTO;
import com.cloudeagle.RepoAccess.Insight.dto.RepositoryDTO;
import com.cloudeagle.RepoAccess.Insight.dto.UserAccessDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class AccessReportService {

    private final GitHubClient client;
    private final GitHubConfig config;

    public AccessReportResponse generateReport() {

        List<RepositoryDTO> repos = client.getRepositories();

        Map<String, List<String>> accessMap = new ConcurrentHashMap<>();

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (RepositoryDTO repo : repos) {

            CompletableFuture<Void> future =
                    CompletableFuture.runAsync(() -> {

                        List<CollaboratorDTO> users =
                                client.getCollaborators(repo.getName());

                        for (CollaboratorDTO user : users) {

                            accessMap
                                    .computeIfAbsent(
                                            user.getLogin(),
                                            k -> new ArrayList<>()
                                    )
                                    .add(repo.getName());
                        }

                    });

            futures.add(future);
        }

        CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        ).join();

        List<UserAccessDTO> result =
                accessMap.entrySet()
                        .stream()
                        .map(e -> new UserAccessDTO(
                                e.getKey(),
                                e.getValue()))
                        .toList();

        return AccessReportResponse.builder()
                .organization(config.getOrganization())
                .generatedAt(LocalDateTime.now())
                .users(result)
                .build();
    }
}