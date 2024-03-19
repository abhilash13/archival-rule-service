package com.archive.ruleservice.service.impl;

import com.archive.ruleservice.entity.ArchivePolicy;
import com.archive.ruleservice.service.IDataReadService;
import com.archive.ruleservice.util.CustomJdbcConnector;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;

@Service
public class MySqlDataReadService implements IDataReadService {
    @Override
    public ResultSet readDataFromDatabase(ArchivePolicy archivePolicy) {
        CustomJdbcConnector sourceJdbcConnector = null;
        try{
            sourceJdbcConnector = new CustomJdbcConnector(archivePolicy.getDatabaseURI(),
                    archivePolicy.getUserName(), archivePolicy.getPassword());

            String query = "select * from " + archivePolicy.getTableName() + " where created_at < DATE_SUB(NOW(), INTERVAL "
                    + archivePolicy.getArchiveDataBeforeInDays() + " DAY)";
            return sourceJdbcConnector.executeQuery(query);
        } catch(Exception e) {

        } finally {
            sourceJdbcConnector.closeConnection();
        }
        return null;
    }
}
