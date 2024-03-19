package com.archive.ruleservice.service;

import com.archive.ruleservice.entity.ArchivePolicy;
import com.archive.ruleservice.model.ArchivePolicyRequest;

import java.util.List;

public interface IArchivePolicyService {
    ArchivePolicy createRule(ArchivePolicyRequest archiveRuleRequest);

    List<ArchivePolicy> getRules();
}
