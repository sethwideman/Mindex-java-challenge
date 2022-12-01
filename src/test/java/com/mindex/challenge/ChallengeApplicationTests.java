package com.mindex.challenge;

import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChallengeApplicationTests {

	@Autowired
	private EmployeeService employeeService;

	@Test
	public void testZeroReports() {
		ReportingStructure rs = employeeService.getReportingStructure("b7839309-3348-463b-a7e3-5de1c168beb3");
		assertEquals(0, rs.getNumberOfReports());
	}

	@Test
	public void testOneReports() {
		//None in snapshot, skipping.
	}

	@Test
	public void testMultipleReports() {
		ReportingStructure rs = employeeService.getReportingStructure("03aa1462-ffa9-4978-901b-7c001562cf6f");
		assertEquals(2, rs.getNumberOfReports());
	}

	@Test
	public void testMultipleGenerationReports() {
		ReportingStructure rs = employeeService.getReportingStructure("16a596ae-edd3-4847-99fe-c4518e82c86f");
		assertEquals(4, rs.getNumberOfReports());
	}

}
