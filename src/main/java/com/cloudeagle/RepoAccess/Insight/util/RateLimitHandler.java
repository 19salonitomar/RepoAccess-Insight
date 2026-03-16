package com.cloudeagle.RepoAccess.Insight.util;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class RateLimitHandler {

    public void handleRateLimit(HttpHeaders headers) {

        if (headers == null) {
            return;
        }

        String remaining = headers.getFirst("X-RateLimit-Remaining");
        String resetTime = headers.getFirst("X-RateLimit-Reset");

        if (remaining != null && Integer.parseInt(remaining) == 0) {

            long resetEpoch = Long.parseLong(resetTime);
            long currentTime = System.currentTimeMillis() / 1000;

            long waitTime = resetEpoch - currentTime;

            if (waitTime > 0) {
                try {
                    System.out.println("GitHub API rate limit exceeded. Waiting for "
                            + waitTime + " seconds before retrying...");

                    Thread.sleep(waitTime * 1000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}