package com.archive.service.service.impl;

import com.archive.service.dao.ArchivalPolicyRepository;
import com.archive.service.service.IArchiveDataService;
import com.archive.service.util.CustomJdbcConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MySqlArchiveDataServiceImpl implements IArchiveDataService {

    @Value("${target.datasource.url:}")
    private String targetUrl;
    @Value("${target.datasource.username:}")
    private String target_user;
    @Value("${target.datasource.password:}")
    private String target_password;

    private final ArchivalPolicyRepository archivalPolicyRepository;

    @Override
    public void archiveDataToDatabase(ResultSet rs, String tableName) throws SQLException {

        CustomJdbcConnector targetJdbcConnector = new CustomJdbcConnector(targetUrl, target_user, target_password);

        PreparedStatement insertStmt = targetJdbcConnector.getConnection().prepareStatement("INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?, ?)");

        try{
            // Retrieve column names once for efficiency
            ResultSetMetaData resulSetMetaData = rs.getMetaData();
            int columnCount = resulSetMetaData.getColumnCount();

            int count = 0;
            while (rs.next()) {
                // Bind values for dynamic columns
                for (int i = 1; i <= columnCount; i++) {
                    insertStmt.setObject(i, rs.getObject(i));
                }
                count++;
                insertStmt.executeUpdate();
            }

            System.out.println("Archived " + count + " rows for table " + tableName);
            System.out.println("Data transferred successfully!");

        } catch (SQLIntegrityConstraintViolationException e) {

        } catch(Exception e){

        } finally {
            targetJdbcConnector.closeConnection();
        }

    }

    @Override
    public void deleteArchiveData(String tableName, Long retainArchivedDataForInDays) {
        CustomJdbcConnector targetJdbcConnector = new CustomJdbcConnector(targetUrl, target_user, target_password);

        String query = "Delete from " + tableName + " where created_at < DATE_SUB(NOW(), INTERVAL "
                + retainArchivedDataForInDays + " DAY)";
        int count = 0;
        try {
            System.out.println("Query is " + query);
            PreparedStatement deleteStmt = targetJdbcConnector.getConnection().prepareStatement(query);
            count = deleteStmt.executeUpdate();

        } catch (SQLException e) {
            log.error("Exception occurred while deleting the archived data to remote DB " + e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println("Deleted " + count + " rows for archived table " + tableName);
    }

    @Override
    public String readDataFromArchivedTable(String tableName) throws SQLException {
        CustomJdbcConnector targetJdbcConnector = new CustomJdbcConnector(targetUrl, target_user, target_password);

        try{
            String query = "SELECT * from " + tableName;
            var result = targetJdbcConnector.executeQuery(query);

            return convertResultSetToJson(result).toString();
        } catch (SQLException e) {
            log.error("Exception occurred while reading the archived data to remote DB " + e.getMessage());
            throw new RuntimeException(e);
        } finally{
            targetJdbcConnector.closeConnection();
        }

    }

    @Override
    public String readDataFromArchived() throws SQLException {
        var archivalPolicies = archivalPolicyRepository.findAll();
        ArrayNode jsonNodes = new ObjectMapper().createArrayNode();
        CustomJdbcConnector targetJdbcConnector = new CustomJdbcConnector(targetUrl, target_user, target_password);
        try{
            for (var policy : archivalPolicies) {
                String query = "SELECT * from " + policy.getTableName();
                var result = targetJdbcConnector.executeQuery(query);
                jsonNodes.add(convertResultSetToJson(result));
            }
        } catch (SQLException e) {
            log.error("Exception occurred while reading the archived data to remote DB " + e.getMessage());
            throw new RuntimeException(e);
        }

        return jsonNodes.toString();
    }

    public static  ArrayNode convertResultSetToJson(ResultSet resultSet) throws SQLException {
        ArrayNode jsonArray = new ObjectMapper().createArrayNode();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            ObjectNode jsonObject = new ObjectMapper().createObjectNode();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                Object value = resultSet.getObject(i);
                jsonObject.put(columnName, value.toString());
            }

            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }
}
