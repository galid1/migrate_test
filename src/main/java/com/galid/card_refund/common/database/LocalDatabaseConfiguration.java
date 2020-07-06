package com.galid.card_refund.common.database;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;


@Configuration
@Profile("local")
public class LocalDatabaseConfiguration {
    @Bean
    public DataSource dataSource() {
        DataSourceBuilder builder = DataSourceBuilder.create();
        builder.username("root");
        builder.password("crew1207");
        builder.driverClassName("com.mysql.cj.jdbc.Driver");
        builder.url("jdbc:mysql://localhost:3306/refund?serverTimezone=Asia/Seoul&useSSL=false&characterEncoding=UTF-8");
        return builder.build();
    }
}
