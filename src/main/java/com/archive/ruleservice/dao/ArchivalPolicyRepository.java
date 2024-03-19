package com.archive.ruleservice.dao;

import com.archive.ruleservice.entity.ArchivePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivalPolicyRepository extends JpaRepository<ArchivePolicy, Long> {
}
