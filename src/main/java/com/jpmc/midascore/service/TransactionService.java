package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jpmc.midascore.foundation.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public TransactionService(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    @Transactional
    public boolean processTransaction(Transaction tx) {
        try {
            logger.info("Processing transaction: {}", tx);
            
            // Validate transaction
            if (!validateTransaction(tx)) {
                logger.warn("Transaction validation failed: {}", tx);
                return false;
            }
            
            // Get sender and recipient accounts
            UserRecord sender = userRepository.findById(tx.getSenderId()).orElse(null);
            UserRecord recipient = userRepository.findById(tx.getRecipientId()).orElse(null);

            if (sender == null || recipient == null) {
                logger.error("Sender or recipient not found. SenderId: {}, RecipientId: {}", 
                    tx.getSenderId(), tx.getRecipientId());
                return false;
            }

            // Check if sender has sufficient balance
            if (sender.getBalance() >= tx.getAmount()) {
                // Deduct from sender
                sender.setBalance(sender.getBalance() - tx.getAmount());
                // Add to recipient
                recipient.setBalance(recipient.getBalance() + tx.getAmount());

                // Save updated balances
                userRepository.save(sender);
                userRepository.save(recipient);

                // Record the transaction
                TransactionRecord record = new TransactionRecord(sender, recipient, tx.getAmount());
                transactionRecordRepository.save(record);

                logger.info("Transaction processed successfully. Sender {} -> Recipient {}: Amount {}", 
                    sender.getName(), recipient.getName(), tx.getAmount());
                
                // Debug: Log all user balances after each transaction
                logAllUserBalances();
                
                return true;
            } else {
                logger.warn("Insufficient funds. Sender balance: {}, Required amount: {}", 
                    sender.getBalance(), tx.getAmount());
                return false;
            }
        } catch (Exception e) {
            logger.error("Error processing transaction: {}", tx, e);
            return false;
        }
    }
    
    private void logAllUserBalances() {
        logger.info("=== Current User Balances ===");
        Iterable<UserRecord> allUsers = userRepository.findAll();
        for (UserRecord user : allUsers) {
            logger.info("User {} (ID: {}): Balance = {}", user.getName(), user.getId(), user.getBalance());
        }
        logger.info("=============================");
    }
    
    private boolean validateTransaction(Transaction transaction) {
        // Basic validation
        if (transaction.getAmount() <= 0) {
            logger.warn("Invalid amount: {}", transaction.getAmount());
            return false;
        }
        
        if (transaction.getSenderId() == transaction.getRecipientId()) {
            logger.warn("Sender and recipient cannot be the same: {}", transaction.getSenderId());
            return false;
        }
        
        if (transaction.getSenderId() <= 0 || transaction.getRecipientId() <= 0) {
            logger.warn("Invalid sender or recipient ID: sender={}, recipient={}", 
                transaction.getSenderId(), transaction.getRecipientId());
            return false;
        }
        
        return true;
    }
}
