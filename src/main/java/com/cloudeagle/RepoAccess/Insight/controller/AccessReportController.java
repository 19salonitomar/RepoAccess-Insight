package com.cloudeagle.RepoAccess.Insight.controller;

import com.cloudeagle.RepoAccess.Insight.dto.AccessReportResponse;
import com.cloudeagle.RepoAccess.Insight.service.AccessReportService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/access-report")
@RequiredArgsConstructor
public class AccessReportController {

    private final AccessReportService accessReportService;

    @GetMapping
    public ResponseEntity<AccessReportResponse> generateReport() {

        AccessReportResponse report =
                accessReportService.generateReport();

        return ResponseEntity.ok(report);
    }

}