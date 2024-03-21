package com.archive.service.service;

import com.archive.service.entity.ArchivePolicy;

import java.sql.ResultSet;

public interface IDataReadService {
    ResultSet readDataFromDatabase(ArchivePolicy archivePolicy, String table);
}
