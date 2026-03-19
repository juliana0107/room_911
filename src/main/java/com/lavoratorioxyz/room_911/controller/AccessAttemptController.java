package com.lavoratorioxyz.room_911.controller;

import com.lavoratorioxyz.room_911.entity.AccessAttempt;
import com.lavoratorioxyz.room_911.service.AccessAttemptService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/access")
@CrossOrigin(origins = "*")
public class AccessAttemptController {

    private final AccessAttemptService accessAttemptService;

    public AccessAttemptController(AccessAttemptService accessAttemptService) {
        this.accessAttemptService = accessAttemptService;
    }

    @GetMapping("/attempts")
    public List<AccessAttempt> getAttemptsByDate(
            @RequestParam String start,
            @RequestParam String end) {

        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);

        return accessAttemptService.getAttemptsByDate(startDate, endDate);
    }
    @GetMapping("/stats")
    public Map<String, Long> getStats() {
        return accessAttemptService.getStatsGlobal();
    }

    @GetMapping("/stats/date")
    public Map<String, Long> getStatsByDate(
            @RequestParam String start,
            @RequestParam String end) {

        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);

        return accessAttemptService.getStatisticsByDate(startDate, endDate);
    }

    @GetMapping("/stats/department/{id}")
    public Map<String, Long> statsByDepartment(@PathVariable Long id) {
        return accessAttemptService.getStatsByDepartment(id);
    }
}