package com.archive.ruleservice.service;

import com.archive.ruleservice.entity.ArchivePolicy;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public interface IDataReadService {
    ResultSet readDataFromDatabase(ArchivePolicy archivePolicy);
}
