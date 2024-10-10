package com.ejunior.fisio_api;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/invoices/invoices-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/invoices/invoices-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SlipPaymentIT {
}
