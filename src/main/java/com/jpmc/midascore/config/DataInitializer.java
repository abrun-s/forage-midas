package com.jpmc.midascore.config;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Commenting out to prevent interference with test UserPopulator
// @Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final UserRepository userRepository;
    
    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Disabled to prevent interference with test data
        logger.info("DataInitializer disabled - test will use UserPopulator instead");
        
        /*
        logger.info("Initializing test data...");
        
        // Create test users with initial balances
        if (userRepository.count() == 0) {
            UserRecord waldorf = new UserRecord("waldorf", 1000.0f);
            UserRecord user2 = new UserRecord("User2", 500.0f);
            UserRecord user3 = new UserRecord("User3", 750.0f);
            UserRecord user4 = new UserRecord("User4", 300.0f);
            UserRecord user5 = new UserRecord("User5", 1200.0f);
            UserRecord user6 = new UserRecord("User6", 600.0f);
            UserRecord user7 = new UserRecord("User7", 900.0f);
            UserRecord user8 = new UserRecord("User8", 450.0f);
            UserRecord user9 = new UserRecord("User9", 800.0f);
            UserRecord user10 = new UserRecord("User10", 650.0f);
            
            userRepository.save(waldorf);  // This will have ID 1
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);
            userRepository.save(user6);
            userRepository.save(user7);
            userRepository.save(user8);
            userRepository.save(user9);
            userRepository.save(user10);
            
            logger.info("Created {} test users with initial balances", userRepository.count());
            logger.info("Waldorf user created with ID: {} and balance: {}", waldorf.getId(), waldorf.getBalance());
        } else {
            logger.info("Database already contains {} users, skipping initialization", userRepository.count());
        }
        */
    }
}
