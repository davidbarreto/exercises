package br.com.dbarreto.payment;

import java.util.Currency;

public interface PaymentGateway {
    String processPayment(String recipient, Currency currency, long amount);
}
