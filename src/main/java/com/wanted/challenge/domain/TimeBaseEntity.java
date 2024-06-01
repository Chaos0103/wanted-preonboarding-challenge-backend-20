package com.wanted.challenge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeBaseEntity {

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    @Column(nullable = false, columnDefinition = "datetime default current_timestamp on update current_timestamp")
    private LocalDateTime lastModifiedDateTime;

    protected TimeBaseEntity(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
