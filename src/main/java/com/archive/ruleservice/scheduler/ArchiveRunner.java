package com.archive.ruleservice.scheduler;

import com.archive.ruleservice.dao.ArchivalPolicyRepository;
import com.archive.ruleservice.service.IArchiveDataService;
import com.archive.ruleservice.service.IDataReadService;
import com.archive.ruleservice.service.impl.MySqlArchiveDataServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArchiveRunner {

    private final ArchivalPolicyRepository archivalPolicyRepository;

    private final IDataReadService dataReadService;

    private final IArchiveDataService archiveDataWriteService;

    @Scheduled(cron = "0 2 * * * * ")
    //@Scheduled(cron = "0 */3 * * * *")
    public void executeArchive() throws SQLException {
        System.out.println("Executing archive scheduler job at " + System.currentTimeMillis());
        var archivalPolicies = archivalPolicyRepository.findAll();

        try{
            for(var archivalPolicy : archivalPolicies){
                System.out.println("Fetched details from DB " + archivalPolicy.toString());
                var rs = dataReadService.readDataFromDatabase(archivalPolicy);
                archiveDataWriteService.archiveDataToDatabase(rs, archivalPolicy.getTableName());

            }
        } catch(SQLException e) {
            log.debug("Exception has occurred " + e.getMessage());
        }

    }

}

