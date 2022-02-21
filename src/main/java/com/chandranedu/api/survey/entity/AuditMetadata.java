package com.chandranedu.api.survey.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Setter
@Getter
public class AuditMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDateTime creationDate;
    private LocalDateTime updatedDate;
}
