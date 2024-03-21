package com.archive.service.scheduler;

import com.archive.service.dao.ArchivalPolicyRepository;
import com.archive.service.service.IArchiveDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArchiveDeleteRunner {
    private final ArchivalPolicyRepository archivalPolicyRepository;

    private final IArchiveDataService archiveDataWriteService;

    @Scheduled(cron = "0 0 * * * SUN")
    //@Scheduled(cron = "0 */5 * * * *")
    public void executeDeleteArchiveData() {
        System.out.println("Executing delete archive scheduler job at " + System.currentTimeMillis());
        var archivalPolicies = archivalPolicyRepository.findAll();

        for(var archivalPolicy : archivalPolicies){
            log.debug("Fetched details from DB {}", archivalPolicy.toString());
            archiveDataWriteService.deleteArchiveData(archivalPolicy.getTableName(),
                    archivalPolicy.getRetainArchivedDataForInDays());
        }
    }
}
