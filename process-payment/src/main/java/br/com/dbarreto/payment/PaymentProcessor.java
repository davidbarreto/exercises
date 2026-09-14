package br.com.dbarreto.payment;

import java.util.Currency;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaymentProcessor {

    private final Map<String, PaymentResult> cacheResponse;
    private final PaymentGateway gateway;

    public PaymentProcessor(PaymentGateway gateway) {
        this.cacheResponse = new ConcurrentHashMap<>();
        this.gateway = gateway;
    }

    public PaymentResult processPayment(String idempotencyKey, PaymentRequest request) {
        var result = cacheResponse.computeIfAbsent(idempotencyKey, k -> doPayment(request));
        if (!result.request.equals(request)) {
            throw new IdempotencyConflictException("Request doesn't match idempotency key " + idempotencyKey);
        }
        return result;
    }

    private PaymentResult doPayment(PaymentRequest request) {
        String paymentId = gateway.processPayment(request.recipient, request.currency, request.amount);
        var status = paymentId != null ? PaymentStatus.SUCCEEDED : PaymentStatus.FAILED;
        return new PaymentResult(paymentId, status, request);
    }

    public record PaymentRequest(long amount, Currency currency, String recipient) {}
    public record PaymentResult(String paymentId, PaymentStatus status, PaymentRequest request) {}

    public enum PaymentStatus {
        SUCCEEDED,
        FAILED
    }

    public static class IdempotencyConflictException extends RuntimeException {
        public IdempotencyConflictException(String msg) {
            super(msg);
        }
    }
}
