package com.archive.service.dao;

import com.archive.service.entity.ArchivePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchivalPolicyRepository extends JpaRepository<ArchivePolicy, Long> {
}
