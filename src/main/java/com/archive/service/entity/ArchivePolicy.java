package com.archive.service.entity;

import com.archive.service.util.StringListConverter;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @Convert(converter = StringListConverter.class)
    @Column(name = "tablenames", nullable=false)
    List<String> tableNames;

    @Column(name = "archivedatabeforeindays", nullable=false)
    private Long archiveDataBeforeInDays;
    @Column(name = "retainarchiveddataforindays", nullable=false)
    private Long retainArchivedDataForInDays;

    //TODO: As a future plan add a shouldDelete flag to indicate if service has
    // permission to delete the original data after retention
}
