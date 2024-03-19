package com.archive.ruleservice.scheduler;

import com.archive.ruleservice.dao.ArchivalPolicyRepository;
import com.archive.ruleservice.service.IArchiveDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArchiveDeleteRunner {
    private final ArchivalPolicyRepository archivalPolicyRepository;

    private final IArchiveDataService archiveDataWriteService;

    @Scheduled(cron = "0 0 * * * SUN")
    public void executeDeleteArchiveData() {
        var archivalPolicies = archivalPolicyRepository.findAll();

        for(var archivalPolicy : archivalPolicies){
            log.debug("Fetched details from DB {}", archivalPolicy.toString());
            archiveDataWriteService.deleteArchiveData(archivalPolicy.getTableName(),
                    archivalPolicy.getRetainArchivedDataForInDays());
        }
    }
}
