package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Generating report for employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);
        Integer numberOfReports = calculateNumberOfReports(employee.getDirectReports());

        if (numberOfReports != 0) {
            for (Employee reportingEmployee : employee.getDirectReports()) {
                Employee employeeLookup = employeeRepository.findByEmployeeId(reportingEmployee.getEmployeeId());
                if (employeeLookup == null) {
                    continue;
                }
                numberOfReports += calculateNumberOfReports(employeeLookup.getDirectReports());
            }
        }

        ReportingStructure reportingStructure = new ReportingStructure();
        return reportingStructure
                .setEmployee(employee)
                .setNumberOfReports(numberOfReports);
    }

    private Integer calculateNumberOfReports(List<Employee> directReports) {
        if (directReports == null) {
            return 0;
        }

        return directReports.size();
    }
}
