package com.archive.ruleservice.scheduler;

import com.archive.ruleservice.dao.ArchivalPolicyRepository;
import com.archive.ruleservice.service.IArchiveDataService;
import com.archive.ruleservice.service.IDataReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class ArchiveRunner {

    private final ArchivalPolicyRepository archivalPolicyRepository;

    private final IDataReadService dataReadService;

    private final IArchiveDataService archiveDataWriteService;

    @Scheduled(cron = "0 2 * * * * ")
    public void executeArchive() throws SQLException {
        var archivalPolicies = archivalPolicyRepository.findAll();

        try{
            for(var archivalPolicy : archivalPolicies){
                System.out.println("Fetched details from DB " + archivalPolicy.toString());
                var rs = dataReadService.readDataFromDatabase(archivalPolicy);
                archiveDataWriteService.archiveDataToDatabase(rs, archivalPolicy.getTableName());
            }
        } catch(SQLException e) {
            System.out.println("Exception has occurred " + e.getMessage());
        }

    }

}

