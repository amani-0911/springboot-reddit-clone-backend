package org.sid.springreact;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class BaseTest {

  static   MySQLContainer mySQLContainer=new MySQLContainer("mysql:latest")
            .withDatabaseName("spring-reddit-test-db")
            .withUsername("testuser")
            .withPassword("pass");
    static {
        mySQLContainer.start();
    }
}
