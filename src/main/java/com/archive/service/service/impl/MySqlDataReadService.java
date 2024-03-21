package com.archive.service.service.impl;

import com.archive.service.entity.ArchivePolicy;
import com.archive.service.service.IDataReadService;
import com.archive.service.util.CustomJdbcConnector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;

@Service
@Slf4j
public class MySqlDataReadService implements IDataReadService {
    @Override
    public ResultSet readDataFromDatabase(ArchivePolicy archivePolicy, String table) {
        CustomJdbcConnector sourceJdbcConnector;
        try{
            sourceJdbcConnector = new CustomJdbcConnector(archivePolicy.getDatabaseURI(),
                    archivePolicy.getUserName(), archivePolicy.getPassword());

            String query = "select * from " + table + " where created_at < DATE_SUB(NOW(), INTERVAL "
                    + archivePolicy.getArchiveDataBeforeInDays() + " DAY)";
            return sourceJdbcConnector.executeQuery(query);
        } catch(Exception e) {
            log.error("Exception occurred while reading data from source DB " + e.getStackTrace());
            throw new RuntimeException(e);
        }
    }
}
