package com.archive.service.service;

import com.archive.service.entity.ArchivePolicy;
import com.archive.service.model.ArchivePolicyRequest;

import java.util.List;

public interface IArchivePolicyService {
    ArchivePolicy createRule(ArchivePolicyRequest archiveRuleRequest);

    List<ArchivePolicy> getRules();
}
