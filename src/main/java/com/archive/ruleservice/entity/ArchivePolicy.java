package com.archive.ruleservice.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "archivalpolicy")
@Builder
public class ArchivePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true)
    private String name;
    @Column(name="databasename",nullable=false)
    private String databaseName;
    @Column(name="databaseuri",nullable=false)
    private String databaseURI;
    @Column(name = "username",nullable=false)
    private String userName;
    @Column(nullable=false)
    private String password;
    @Column(name = "tablename", nullable=false)
    String tableName;
    @Column(name = "archiveDataBeforeInDays", nullable=false)
    private Long archiveDataBeforeInDays;
    @Column(name = "retainArchivedDataForInDays", nullable=false)
    private Long retainArchivedDataForInDays;
}
