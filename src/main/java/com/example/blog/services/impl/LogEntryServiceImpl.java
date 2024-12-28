package com.example.blog.services.impl;

import com.example.blog.constants.EntityType;
import com.example.blog.entity.LogEntry;
import com.example.blog.repository.LogEntryRepository;
import com.example.blog.services.LogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class LogEntryServiceImpl implements LogEntryService {

    @Autowired
    private LogEntryRepository logEntryRepository;

    @Override
    public void logAction(String username, String action, EntityType entity, String targetUser, String details) {
        LogEntry newLog=new LogEntry();
        newLog.setUserName(username);
        newLog.setAction(action);
        newLog.setEntity(entity);
        newLog.setTargetUser(targetUser);
        newLog.setDetails(details);
        newLog.setTimeStamp(LocalDateTime.now());

        this.logEntryRepository.save(newLog);
    }
}
