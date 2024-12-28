package com.example.blog.services;

import com.example.blog.constants.EntityType;

import java.time.LocalDateTime;

public interface LogEntryService {
    void logAction(String username, String action, EntityType entity, String targetUser, String details);
}
