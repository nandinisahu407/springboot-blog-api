package com.example.blog.repository;

import com.example.blog.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry,Long> {
}
