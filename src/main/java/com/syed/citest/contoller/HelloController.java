package com.syed.citest.contoller;

import com.syed.citest.configuration.ExternalAPICaller;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class HelloController {

    private final ExternalAPICaller externalAPICaller;

    @GetMapping("/test")
    public String test() {
        return "Account are in good condition";
    }

    @Autowired
    public HelloController(ExternalAPICaller externalApi) {
        this.externalAPICaller = externalApi;
    }

    @GetMapping("/circuit-breaker")
    @CircuitBreaker(name = "CircuitBreakerService", fallbackMethod = "secondMethod")
    public String circuitBreakerApi() {
        return externalAPICaller.callApi();
    }

    public String secondMethod(Exception ex) {
        return "fallback reply";
    }

    @GetMapping("/retry")
    @Retry(name = "retryApi", fallbackMethod = "fallbackAfterRetry")
    public String retryApi() {
        return externalAPICaller.callApi();
    }

    @GetMapping("/rate-limiter")
    @RateLimiter(name = "externalService")
    public String rateLimitApi() {
        return externalAPICaller.callApi();
    }

    @GetMapping("/time-limiter")
    @TimeLimiter(name = "externalService")
    public CompletableFuture<String> timeLimiterApi() {
        return CompletableFuture.supplyAsync(externalAPICaller::callApiWithDelay);
    }

    public String fallbackAfterRetry(Exception ex) {
        return "all retries have exhausted";
    }

}
