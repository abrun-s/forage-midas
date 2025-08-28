package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TransactionListener {

  private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
  
  private final TransactionService transactionService;
  
  public TransactionListener(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @KafkaListener(
    topics = "${general.kafka-topic}",
    groupId = "midas-core-group",
    containerFactory = "kafkaListenerContainerFactory"
  )

  public void listen(Transaction transaction) {
    logger.info("Received Transaction: {}", transaction);
    
    boolean success = transactionService.processTransaction(transaction);
    
    if (success) {
      logger.info("Transaction processed successfully: {}", transaction);
    } else {
      logger.warn("Transaction processing failed: {}", transaction);
    }
  }
}
