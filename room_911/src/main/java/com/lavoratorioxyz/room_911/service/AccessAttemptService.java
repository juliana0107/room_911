package com.lavoratorioxyz.room_911.service;

import com.lavoratorioxyz.room_911.entity.AccessAttempt;
import com.lavoratorioxyz.room_911.repository.AccessAttemptRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccessAttemptService {

    private final AccessAttemptRepository accessAttemptRepository;

    public AccessAttemptService(AccessAttemptRepository accessAttemptRepository) {
        this.accessAttemptRepository = accessAttemptRepository;
    }

    public List<AccessAttempt> getAttemptsByDate(LocalDateTime start, LocalDateTime end) {
        return accessAttemptRepository.findByAttemptTimeBetween(start, end);
    }

        public Map<String, Long> getStatisticsByDate(LocalDateTime start, LocalDateTime end) {

            List<Object[]> results = accessAttemptRepository.countByResultBetween(start, end);

            Map<String, Long> stats = new HashMap<>();

            stats.put("AUTHORIZED", 0L);
            stats.put("DENIED", 0L);
            stats.put("NOT_REGISTERED", 0L);

            long total = 0;

            for (Object[] row : results) {
                String result = row[0].toString();
                Long count = (Long) row[1];

                stats.put(result, count);
                total += count;
            }

            stats.put("TOTAL", total);

            return stats;
        }

    public Map<String, Long> getStatsByDepartment(Long departmentId) {

        List<AccessAttempt> attempts = accessAttemptRepository.findByDepartmentId(departmentId);

        long total = attempts.size();
        long authorized = attempts.stream().filter(a -> a.getResult().name().equals("AUTHORIZED")).count();
        long denied = attempts.stream().filter(a -> a.getResult().name().equals("DENIED")).count();
        long notRegistered = attempts.stream().filter(a -> a.getResult().name().equals("NOT_REGISTERED")).count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("TOTAL", total);
        stats.put("AUTHORIZED", authorized);
        stats.put("DENIED", denied);
        stats.put("NOT_REGISTERED", notRegistered);

        return stats;
    }
}