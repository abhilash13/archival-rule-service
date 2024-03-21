package com.archive.service.controller;

import com.archive.service.model.CustomUserDetails;
import com.archive.service.service.IArchiveDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ArchivedDataController {
    private final IArchiveDataService archiveDataService;

    @GetMapping("/archivedData/table/{tableName}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<String> fetchArchivedData(@PathVariable String tableName,
                                                    @AuthenticationPrincipal UserDetails userDetails)
            throws SQLException {

        if(userDetails instanceof CustomUserDetails){
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            // Check if User is trying to fetch details of table for which they have access
            if(tableName.equals("student") && !customUserDetails.getAuthorities().stream().
                    map(Objects::toString).collect(Collectors.joining(",")).
                    contains("USER") ){
                log.error("The current user doe not have required permission to fetch the result");
                return new ResponseEntity<>("Unauthorised", HttpStatus.UNAUTHORIZED);
            }
        }

        return new ResponseEntity<>(archiveDataService.readDataFromArchivedTable(tableName), HttpStatus.OK);
    }

    @GetMapping("/archivedData/")
    public ResponseEntity<String> fetchArchivedDataForAllTables() throws SQLException {
        return new ResponseEntity<>(archiveDataService.readDataFromArchived(), HttpStatus.OK);
    }
}
