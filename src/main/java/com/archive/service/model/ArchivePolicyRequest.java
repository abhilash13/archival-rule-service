package com.archive.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivePolicyRequest {
    private String name;
    private String databaseName;
    private String databaseURI;
    private String userName;
    private String password;
    private Long port;
    private String tableNames;
    private Long archiveDataBeforeInDays;
    private Long retainArchivedDataForInDays;
}
