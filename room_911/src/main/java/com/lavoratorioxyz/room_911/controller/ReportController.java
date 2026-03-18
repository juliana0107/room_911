package com.lavoratorioxyz.room_911.controller;

import com.lavoratorioxyz.room_911.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/full")
    public ResponseEntity<byte[]> getFullReport() {

        byte[] pdf = reportService.generateFullReport();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_accesos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/date")
    public ResponseEntity<byte[]> reportByDate(
            @RequestParam String start,
            @RequestParam String end) {

        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);

        byte[] pdf = reportService.generateReportByDate(startDate, endDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_fecha.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}