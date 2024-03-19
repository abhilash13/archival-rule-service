package com.archive.ruleservice.controller;

import com.archive.ruleservice.entity.ArchivePolicy;
import com.archive.ruleservice.model.ArchivePolicyRequest;
import com.archive.ruleservice.service.IArchivePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/archive")
@RequiredArgsConstructor
public class ArchiveRulesController {

    private final IArchivePolicyService archiveRuleService;

    @PostMapping("/rules")
    public ResponseEntity<ArchivePolicy> createRule(@RequestBody ArchivePolicyRequest archiveRuleRequest){
         return new ResponseEntity<>(archiveRuleService.createRule(archiveRuleRequest), HttpStatus.CREATED);
    }

    @GetMapping("/rules")
    public ResponseEntity<List<ArchivePolicy>> getRules() {
        return new ResponseEntity<>(archiveRuleService.getRules(), HttpStatus.OK);
    }

}
