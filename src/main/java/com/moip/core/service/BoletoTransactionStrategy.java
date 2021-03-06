package com.moip.core.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.moip.core.client.boleto.BoletoClient;
import com.moip.core.client.boleto.BoletoClient.BoletoPaymentResponse;
import com.moip.core.model.Payment;
import com.moip.core.model.PaymentResponse;
import com.moip.core.model.PaymentStatus;
import com.moip.core.model.PaymentType;
import com.moip.core.model.Transaction;
import com.moip.core.repository.TransactionRepository;

@Component
public class BoletoTransactionStrategy implements TransactionStrategy {

	private final BoletoClient boletoClient;
	private final TransactionRepository transactionRepository;

	@Value("${boleto.dayToDueDate}")
	private Long daysToDueDate;
	private Clock clock;

	public BoletoTransactionStrategy(BoletoClient boletoClient, TransactionRepository transactionRepository, Clock clock) {
		this.boletoClient = boletoClient;
		this.transactionRepository = transactionRepository;
		this.clock = clock;
	}

	@Autowired
	public BoletoTransactionStrategy(BoletoClient boletoClient, TransactionRepository transactionRepository) {
		this.boletoClient = boletoClient;
		this.transactionRepository = transactionRepository;
		this.clock = Clock.systemDefaultZone();
	}

	@Override
	public PaymentResponse processPayment(Transaction transaction) {
		Payment payment = transaction.getPayment();
		if (payment.getType() == PaymentType.CREDIT_CARD) {
			throw new IllegalArgumentException("This method can't be called for credit card payments");
		}
		LocalDate dueDate = LocalDate.now(clock).plus(daysToDueDate, ChronoUnit.DAYS);
		BoletoPaymentResponse boletoResponse = boletoClient.newPayment(payment.getAmount(), dueDate);
		transactionRepository.save(transaction);
		return new PaymentResponse(PaymentStatus.PENDING, boletoResponse.getBarCode());
	}
}
