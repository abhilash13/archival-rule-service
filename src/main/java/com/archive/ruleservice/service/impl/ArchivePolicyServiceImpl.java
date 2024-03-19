package com.archive.ruleservice.service.impl;

import com.archive.ruleservice.dao.ArchivalPolicyRepository;
import com.archive.ruleservice.entity.ArchivePolicy;
import com.archive.ruleservice.model.ArchivePolicyRequest;
import com.archive.ruleservice.service.IArchivePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ArchivePolicyServiceImpl implements IArchivePolicyService {

    private final ArchivalPolicyRepository archivalPolicyRepository;

    @Override
    public ArchivePolicy createRule(ArchivePolicyRequest archiveRuleRequest) {
        var archivePolicy = createPolicyRule(archiveRuleRequest);
        archivalPolicyRepository.save(archivePolicy);
        return archivePolicy;
    }

    @Override
    public List<ArchivePolicy> getRules() {
        return archivalPolicyRepository.findAll();
    }

    private ArchivePolicy createPolicyRule(ArchivePolicyRequest archiveRuleRequest){
       return ArchivePolicy.builder().name(archiveRuleRequest.getName()).
                databaseName(archiveRuleRequest.getDatabaseName()).databaseURI(archiveRuleRequest.getDatabaseURI()).
                archiveDataBeforeInDays(archiveRuleRequest.getArchiveDataBeforeInDays()).
                retainArchivedDataForInDays(archiveRuleRequest.getRetainArchivedDataForInDays()).
                password(archiveRuleRequest.getPassword()).
                userName(archiveRuleRequest.getUserName()).
                tableName(archiveRuleRequest.getTableName()).build();
    }
}
