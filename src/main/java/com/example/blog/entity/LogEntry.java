package com.example.blog.entity;

import com.example.blog.constants.EntityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_log")
@NoArgsConstructor
@Getter
@Setter
public class LogEntry {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false)
    private String userName;
    private String action;
    @Enumerated(EnumType.STRING)
    private EntityType entity;
    private String targetUser;
    @Column(nullable = false)
    private String details;
    private LocalDateTime timeStamp;
}
