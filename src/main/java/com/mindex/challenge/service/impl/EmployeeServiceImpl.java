package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure getReportingStructure(String id) {
        LOG.debug("Reading employee reporting structure with employee id [{}]", id);

        Employee employee = this.read(id);

        ReportingStructure reportingStructure = new ReportingStructure(employee);
        reportingStructure.setNumberOfReports(calcNumberOfReports(employee));

        return reportingStructure;
    }

    private int calcNumberOfReports(Employee employee) {
        if (!employee.getDirectReports().isPresent()) {
            return 0;
        }
        List<Employee> directReports = employee.getDirectReports().get();
        int n = directReports.size();
        for (Employee emp : directReports) {
            Employee hydrated = this.read(emp.getEmployeeId());
            n += calcNumberOfReports(hydrated);
        }
        return n;
    }
}
