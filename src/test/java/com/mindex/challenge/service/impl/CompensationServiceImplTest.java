package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {
    private String baseUrl;
    private String compensationCreateUrl;
    private String compensationReadUrl;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    CompensationRepository compensationRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        baseUrl = "http://localhost:" + port;
        compensationCreateUrl = baseUrl + "/compensation";
        compensationReadUrl = compensationCreateUrl + "/{employeeId}";
    }

    @Test
    public void testCreateAndRead() {
        Employee testEmployee = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");

        Compensation testCompensation = new Compensation();
        testCompensation
                .setEmployee(testEmployee)
                .setSalary(120000.0)
                .setEffectiveDate(LocalDate.of(1963, Month.MARCH, 22));

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationCreateUrl, testCompensation, Compensation.class).getBody();

        assertCompensationIsNotNull(createdCompensation);
        assertEquals(testCompensation.getEmployee().getEmployeeId(), createdCompensation.getEmployee().getEmployeeId());
        assertEquals(testCompensation.getSalary(), createdCompensation.getSalary());
        assertEquals(testCompensation.getEffectiveDate(), createdCompensation.getEffectiveDate());

        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationReadUrl, Compensation.class, createdCompensation.getEmployee().getEmployeeId()).getBody();
        assertCompensationIsNotNull(readCompensation);
        assertCompensationEquivalence(createdCompensation, readCompensation);
    }

    @Test
    public void testCreateWithNoEmployeeId() {
        Compensation testCompensation = new Compensation();
        testCompensation
                .setEmployee(new Employee())
                .setSalary(120000.0)
                .setEffectiveDate(LocalDate.of(1963, Month.MARCH, 22));

        ResponseEntity createCompensationResponse = restTemplate.postForEntity(compensationCreateUrl, testCompensation, Compensation.class);
        assertEquals(HttpStatus.BAD_REQUEST, createCompensationResponse.getStatusCode());
    }

    private void assertCompensationIsNotNull(Compensation compensation) {
        assertNotNull(compensation);
        assertNotNull(compensation.getEmployee().getEmployeeId());
        assertNotNull(compensation.getEmployee().getFirstName());
        assertNotNull(compensation.getEmployee().getLastName());
        assertNotNull(compensation.getEmployee().getPosition());
        assertNotNull(compensation.getEmployee().getDepartment());
        assertNotNull(compensation.getSalary());
        assertNotNull(compensation.getEffectiveDate());
    }

    private void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getEmployee().getEmployeeId(), actual.getEmployee().getEmployeeId());
        assertEquals(expected.getEmployee().getFirstName(), actual.getEmployee().getFirstName());
        assertEquals(expected.getEmployee().getLastName(), actual.getEmployee().getLastName());
        assertEquals(expected.getEmployee().getPosition(), actual.getEmployee().getPosition());
        assertEquals(expected.getEmployee().getDepartment(), actual.getEmployee().getDepartment());
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
