package org.core.acid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"org.core.acid.system.repository"})
public class AcidApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcidApplication.class, args);
    }

}
