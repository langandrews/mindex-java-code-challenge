package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
    private String baseUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeRepository employeeRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        baseUrl = "http://localhost:" + port;
        reportingStructureUrl = baseUrl + "/employee/{id}/reporting-structure";
    }

    @Test
    public void testGetReportingStructure() {
        Employee testEmployee1 = employeeRepository.findByEmployeeId("16a596ae-edd3-4847-99fe-c4518e82c86f");

        ReportingStructure expectedReportingStructure = new ReportingStructure();
        expectedReportingStructure
                .setEmployee(testEmployee1)
                .setNumberOfReports(4);

        ReportingStructure actualReportingStructure = restTemplate.getForEntity(
                reportingStructureUrl,
                ReportingStructure.class,
                testEmployee1.getEmployeeId()
        ).getBody();

        assertNotNull(actualReportingStructure);
        assertReportingStructureEquivalence(expectedReportingStructure, actualReportingStructure);
    }

    @Test
    public void testGetReportingStructureWithNoDirectReports() {
        Employee employee = employeeRepository.findByEmployeeId("62c1084e-6e34-4630-93fd-9153afb65309");

        ReportingStructure expectedReportingStructure = new ReportingStructure();
        expectedReportingStructure
                .setEmployee(employee)
                .setNumberOfReports(0);

        ReportingStructure actualReportingStructure = restTemplate.getForEntity(
                reportingStructureUrl,
                ReportingStructure.class,
                employee.getEmployeeId()
        ).getBody();

        assertNotNull(actualReportingStructure);
        assertReportingStructureEquivalence(expectedReportingStructure, actualReportingStructure);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    private static void assertReportingStructureEquivalence(ReportingStructure expected, ReportingStructure actual) {
        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
    }
}
