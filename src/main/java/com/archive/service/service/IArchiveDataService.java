package com.archive.service.service;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IArchiveDataService {
    void archiveDataToDatabase(ResultSet resultSet, String tableName) throws SQLException;

    void deleteArchiveData(String tableName, Long retainArchivedDataForInDays);

    String readDataFromArchivedTable(String tableName) throws SQLException;

    String readDataFromArchived() throws SQLException;
}
