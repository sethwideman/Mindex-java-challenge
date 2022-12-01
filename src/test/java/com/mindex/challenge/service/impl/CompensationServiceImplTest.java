package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationByEmployeeIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/employee/compensation";
        compensationByEmployeeIdUrl = "http://localhost:" + port + "/employee/{id}/compensation";
    }

    @Test
    public void testCreateRead() throws ParseException {
        Employee employee = employeeService.read("16a596ae-edd3-4847-99fe-c4518e82c86f");
        Compensation compensation = new Compensation();
        compensation.setEmployee(employee);
        compensation.setSalary(55000.00f);
        compensation.setEffectiveDate(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2021"));

        // Create checks
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();

        assertNotNull(createdCompensation);
        assertEmployeeEquivalence(employee, createdCompensation.getEmployee());


        // Read checks
        Compensation[] readCompensation = restTemplate.getForEntity(compensationByEmployeeIdUrl, Compensation[].class, employee.getEmployeeId()).getBody();
        assertEquals(readCompensation[0].getSalary(), compensation.getSalary(), 0.000f);
        assertEmployeeEquivalence(readCompensation[0].getEmployee(), employee);


        // Multiple entity checks
        Compensation compensation1 = new Compensation();
        compensation1.setEmployee(employee);
        compensation1.setSalary(65000.00f);
        compensation1.setEffectiveDate(new SimpleDateFormat("dd-MM-yyyy").parse("30-11-2022"));

        restTemplate.postForEntity(compensationUrl, compensation1, Compensation.class);

        Compensation[] compensations = restTemplate.getForEntity(compensationByEmployeeIdUrl, Compensation[].class, employee.getEmployeeId()).getBody();
        assertEquals(2, compensations.length);
        assertTrue(compensations[0].getEffectiveDate().after(compensations[1].getEffectiveDate()));
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
