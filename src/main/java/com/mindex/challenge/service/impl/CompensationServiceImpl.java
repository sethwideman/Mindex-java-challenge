package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        return compensationRepository.save(compensation);
    }

    @Override
    public List<Compensation> getCompensation(String employeeId) {
        LOG.debug("Reading compensation for employee with id [{}]", employeeId);

        List<Compensation> compensation = compensationRepository.findCompensationByEmployeeOrderByEffectiveDateDesc(employeeService.read(employeeId));

        if (compensation == null) {
            throw new RuntimeException("No compensation found for employeeId: " + employeeId);
        }

        return compensation;
    }
}
