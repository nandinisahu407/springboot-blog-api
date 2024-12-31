package com.example.blog.repository;

import com.example.blog.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry,Long> {

    @Query("select l from LogEntry l where LOWER(l.userName)=LOWER(:name)")
    List<LogEntry> findActionsPerformedByUser(@Param("name") String name);

    @Query("select l from LogEntry l where LOWER(l.targetUser)=Lower(:name)")
    List<LogEntry> findAllLogs(@Param("name") String name);


}
