package com.imeasystems.orderservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

@Slf4j
public class MYSQLInitializer implements
    ApplicationContextInitializer<ConfigurableApplicationContext> {

    static DockerImageName myImage = DockerImageName.parse("mysql:latest")
        .asCompatibleSubstituteFor("mysql");

    @Container
    public static MySQLContainer<?> mssqlServerContainer = new MySQLContainer<>(myImage)
            .withDatabaseName("ordersDB")
            .withUsername("user")
            .withPassword("test.Pwd-123");

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Startables.deepStart(Stream.of(mssqlServerContainer)).join();
        applicationContext.getBeanFactory()
            .registerSingleton("mysqlContainer", mssqlServerContainer);
        applicationContext.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                mssqlServerContainer.stop();
            }
        });

        log.info("Test Containers mysql jdbc url {}", mssqlServerContainer.getJdbcUrl());

        TestPropertyValues values = TestPropertyValues.of(
            "spring.datasource.url=" + mssqlServerContainer.getJdbcUrl(),
            "spring.datasource.password=" + mssqlServerContainer.getPassword(),
            "spring.datasource.username=" + mssqlServerContainer.getUsername());
        values.applyTo(applicationContext);
    }
}
