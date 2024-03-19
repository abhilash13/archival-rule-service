package com.archive.ruleservice.service.impl;

import com.archive.ruleservice.dao.ArchivalPolicyRepository;
import com.archive.ruleservice.service.IArchiveDataService;
import com.archive.ruleservice.util.CustomJdbcConnector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Service
@RequiredArgsConstructor
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

            while (rs.next()) {
                // Bind values for dynamic columns
                for (int i = 1; i <= columnCount; i++) {
                    insertStmt.setObject(i, rs.getObject(i));
                }
                insertStmt.executeUpdate();
            }

            System.out.println("Data transferred successfully!");

        } catch (SQLException e) {
            // Handle potential errors
            e.printStackTrace();
        }

        targetJdbcConnector.closeConnection();
    }

    @Override
    public void deleteArchiveData(String tableName, Long retainArchivedDataForInDays) {
        CustomJdbcConnector targetJdbcConnector = new CustomJdbcConnector(targetUrl, target_user, target_password);

        String query = "Delete from " + tableName + " where created_at < DATE_SUB(NOW(), INTERVAL "
                + retainArchivedDataForInDays + " DAY)";
        try {
            PreparedStatement deleteStmt = targetJdbcConnector.getConnection().prepareStatement(query);
            deleteStmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String readDataFromArchivedTable(String tableName) throws SQLException {
        CustomJdbcConnector targetJdbcConnector = new CustomJdbcConnector(targetUrl, target_user, target_password);

        String query = "SELECT * from " + tableName;
        var result = targetJdbcConnector.executeQuery(query);
        targetJdbcConnector.closeConnection();
        return convertResultSetToJson(result).toString();

    }

    @Override
    public String readDataFromArchived() throws SQLException {
        var archivalPolicies = archivalPolicyRepository.findAll();
        ArrayNode jsonNodes = new ObjectMapper().createArrayNode();
        CustomJdbcConnector targetJdbcConnector = new CustomJdbcConnector(targetUrl, target_user, target_password);
        for (var policy : archivalPolicies){
            String query = "SELECT * from " + policy.getTableName();
            var result = targetJdbcConnector.executeQuery(query);
            jsonNodes.add(convertResultSetToJson(result));
        }
        return jsonNodes.toString();
    }

    private ArrayNode convertResultSetToJson(ResultSet resultSet) throws SQLException {
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
