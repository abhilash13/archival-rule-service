package com.archive.ruleservice.dao;

import com.archive.ruleservice.entity.ArchivePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivalPolicyRepository extends JpaRepository<ArchivePolicy, Long> {
}
