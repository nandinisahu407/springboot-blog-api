package com.example.blog.controllers;

import com.example.blog.entity.LogEntry;
import com.example.blog.services.LogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activity_logs")
public class ActivityLogController {

    @Autowired
    private LogEntryService logEntryService;

    @GetMapping("/{userName}")
    public ResponseEntity<Object> getAllActionsPerformedByUser(@PathVariable String userName ){
        List<LogEntry> allActions=this.logEntryService.findActionsByUserName(userName);
        if(allActions.isEmpty()){
            return new ResponseEntity<>("No Actions Performed by "+userName+" till yet!", HttpStatus.OK);
        }
        return new ResponseEntity<>(allActions,HttpStatus.OK);
    }

    @GetMapping("/allLogs/{target}")
    public ResponseEntity<Object> getAllActionsPerformedOnEntity(@PathVariable String target ){
        List<LogEntry> allActions=this.logEntryService.findAllLogsPerformedOnEntity(target);
        if(allActions.isEmpty()){
            return new ResponseEntity<>("No Actions Performed on "+target+" till yet!", HttpStatus.OK);
        }
        return new ResponseEntity<>(allActions,HttpStatus.OK);
    }
}
