package com.galid.card_refund.common.config.database;

import com.galid.card_refund.common.file.KeyValueFileLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;


@Configuration
@Profile("deploy")
@RequiredArgsConstructor
public class DatabaseConfiguration {
    private final KeyValueFileLoader keyValueFileLoader;
    private String databaseAccountFilePath = ".refund/databaseAccount.txt";

    @Bean
    public DataSource dataSource() {
        String username = keyValueFileLoader.getValueFromFile(databaseAccountFilePath, "username");
        String password = keyValueFileLoader.getValueFromFile(databaseAccountFilePath, "password");

        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.username(username);
        builder.password(password);
        builder.driverClassName("com.mysql.cj.jdbc.Driver");
        builder.url("jdbc:mysql://localhost:3306/refund?serverTimezone=Asia/Seoul&useSSL=false&allowPublicKeyRetrieval=true&useSSL=false&characterEncoding=UTF-8");
        return builder.build();
    }
}
