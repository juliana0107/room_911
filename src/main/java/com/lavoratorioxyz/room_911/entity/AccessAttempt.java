package com.lavoratorioxyz.room_911.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "access_attempts")
public class AccessAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_code")
    private String employeeCode;

    @Column(name = "attempt_time")
    private LocalDateTime attemptTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "result")
    private AccessResult result;

    private String notes;

    @PrePersist
    public void prePersist() {
        this.attemptTime = LocalDateTime.now();
    }
}