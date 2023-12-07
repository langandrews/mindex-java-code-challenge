package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompensationRepository compensationRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        String employeeId = compensation.getEmployee().getEmployeeId();

        if (employeeId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "employee object must include field: employeeId"
            );
        }

        Employee employee = employeeService.read(employeeId);
        Compensation compensationLookup = compensationRepository.findByEmployee(employee);

        if (compensationLookup == null) {
            compensationRepository.insert(compensation.setEmployee(employee));

            return compensation;
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Compensation already exists for this employee"
            );
        }
    }

    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Reading compensation for employeeId [{}]", employeeId);

        Employee employee = employeeService.read(employeeId);
        Compensation compensation = compensationRepository.findByEmployee(employee);

        if (compensation == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No compensation found for employee with id: " + employeeId
            );
        }

        return compensation;
    }
}
