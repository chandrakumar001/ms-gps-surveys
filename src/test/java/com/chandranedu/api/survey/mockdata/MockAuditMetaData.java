package com.chandranedu.api.survey.mockdata;

import com.chandranedu.api.survey.entity.AuditMetadata;

import java.time.LocalDateTime;

public class MockAuditMetaData {

    public static AuditMetadata getAuditMetadata() {

        final AuditMetadata auditMetadata = new AuditMetadata();
        auditMetadata.setUpdatedDate(LocalDateTime.now());
        auditMetadata.setCreationDate(LocalDateTime.now());
        return auditMetadata;
    }
}
