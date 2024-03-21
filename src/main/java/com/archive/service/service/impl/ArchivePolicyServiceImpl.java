package com.archive.service.service.impl;

import com.archive.service.dao.ArchivalPolicyRepository;
import com.archive.service.entity.ArchivePolicy;
import com.archive.service.model.ArchivePolicyRequest;
import com.archive.service.service.IArchivePolicyService;
import com.archive.service.util.PasswordSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ArchivePolicyServiceImpl implements IArchivePolicyService {

    private final ArchivalPolicyRepository archivalPolicyRepository;

    private final PasswordSecurity passwordSecurity;

    @Override
    public ArchivePolicy createRule(ArchivePolicyRequest archiveRuleRequest) throws Exception {
        var archivePolicy = createPolicyRule(archiveRuleRequest);
        archivalPolicyRepository.save(archivePolicy);
        return archivePolicy;
    }

    @Override
    public List<ArchivePolicy> getRules() {
        return archivalPolicyRepository.findAll();
    }

    private ArchivePolicy createPolicyRule(ArchivePolicyRequest archiveRuleRequest) throws Exception {
       return ArchivePolicy.builder().name(archiveRuleRequest.getName()).
                databaseName(archiveRuleRequest.getDatabaseName()).databaseURI(archiveRuleRequest.getDatabaseURI()).
                archiveDataBeforeInDays(archiveRuleRequest.getArchiveDataBeforeInDays()).
                retainArchivedDataForInDays(archiveRuleRequest.getRetainArchivedDataForInDays()).
                password(passwordSecurity.encrypt(archiveRuleRequest.getPassword())).
                userName(archiveRuleRequest.getUserName()).
                tableNames(Collections.singletonList(archiveRuleRequest.getTableNames())).build();
    }
}
