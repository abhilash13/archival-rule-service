package com.archive.ruleservice.scheduler;

import com.archive.ruleservice.dao.ArchivalPolicyRepository;
import com.archive.ruleservice.service.IArchiveDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class ArchiveDeleteRunner {
    private final ArchivalPolicyRepository archivalPolicyRepository;

    private final IArchiveDataService archiveDataWriteService;

    //@Scheduled(cron = "*/55 * * * * *")
    public void executeDeleteArchiveData() throws SQLException {
        var archivalPolicies = archivalPolicyRepository.findAll();

        for(var archivalPolicy : archivalPolicies){
            System.out.println("Fetched details from DB " + archivalPolicy.toString());
            archiveDataWriteService.deleteArchiveData(archivalPolicy.getTableName(),
                    archivalPolicy.getRetainArchivedDataForInDays());
        }
    }
}
