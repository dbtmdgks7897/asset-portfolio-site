package com.ysh.back.model.auditLog.entity;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AUDIT_LOG")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false, unique = true)
    private Long idx;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "user_idx", nullable = false)
    private Long userIdx;

    @Column(name = "row_id")
    private Long rowId;

    @Column(name = "operation", nullable = false)
    private String operation;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at")
    private String createdAt;
}
