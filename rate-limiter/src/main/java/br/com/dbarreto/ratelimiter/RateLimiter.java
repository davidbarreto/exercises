package br.com.dbarreto.ratelimiter;

public interface RateLimiter {

    boolean allowRequest(String clientId);
}
