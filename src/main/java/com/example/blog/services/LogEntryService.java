package com.example.blog.services;

import com.example.blog.constants.EntityType;
import com.example.blog.entity.LogEntry;

import java.time.LocalDateTime;
import java.util.List;

public interface LogEntryService {
    void logAction(String username, String action, EntityType entity, String targetUser, String details);

    List<LogEntry> findActionsByUserName(String name);
    List<LogEntry> findAllLogsPerformedOnEntity(String name);
}
