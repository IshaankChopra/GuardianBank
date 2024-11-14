package com.ishaank.Projects.GuardianBank.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class TransactionConfig {

    //Need a transactional manager for managing the transactions, and that is achieved by the
    // PlatformTransactionManager, which is implmented by the JpaTransactionManager for our case
    // where we are using MongoDB
    // Simply finds the bean that is implementing the PlatformTransactionManager, method name doesn't matter

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory dbFactory) {
        return new JpaTransactionManager(dbFactory);
    }
}
