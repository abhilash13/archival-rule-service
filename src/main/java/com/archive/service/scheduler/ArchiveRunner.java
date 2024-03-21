package com.archive.service.scheduler;

import com.archive.service.dao.ArchivalPolicyRepository;
import com.archive.service.service.IArchiveDataService;
import com.archive.service.service.IDataReadService;
import com.archive.service.util.PasswordSecurity;
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

    private final PasswordSecurity passwordSecurity;

    @Scheduled(cron = "0 2 * * * * ")
    //@Scheduled(cron = "0 */3 * * * *")
    public void executeArchive() {
        System.out.println("Executing archive scheduler job at " + System.currentTimeMillis());
        var archivalPolicies = archivalPolicyRepository.findAll();

        try{
            for(var archivalPolicy : archivalPolicies){
                System.out.println("Fetched details from DB " + archivalPolicy.toString());
                String password = passwordSecurity.decrypt(archivalPolicy.getPassword());
                archivalPolicy.setPassword(password);
                var rs = dataReadService.readDataFromDatabase(archivalPolicy);
                archiveDataWriteService.archiveDataToDatabase(rs, archivalPolicy.getTableName());
                // TODO: As a future plan if user provides shouldDelete flag value as true
                // add logic to delete data from source DB once archival is done.
            }
        } catch(SQLException e) {
            log.debug("Exception has occurred " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}

