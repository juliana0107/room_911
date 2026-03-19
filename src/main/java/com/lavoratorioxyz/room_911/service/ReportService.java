package com.lavoratorioxyz.room_911.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;

import com.lavoratorioxyz.room_911.entity.AccessAttempt;
import com.lavoratorioxyz.room_911.entity.CompanyProfile;
import com.lavoratorioxyz.room_911.entity.Employee;
import com.lavoratorioxyz.room_911.repository.AccessAttemptRepository;
import com.lavoratorioxyz.room_911.repository.CompanyProfileRepository;
import com.lavoratorioxyz.room_911.repository.EmployeeRepository;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReportService {

    private final AccessAttemptRepository accessAttemptRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyProfileRepository companyProfileRepository;

    public ReportService(AccessAttemptRepository accessAttemptRepository,
                         EmployeeRepository employeeRepository,
                         CompanyProfileRepository companyProfileRepository) {
        this.accessAttemptRepository = accessAttemptRepository;
        this.employeeRepository = employeeRepository;
        this.companyProfileRepository = companyProfileRepository;
    }



    public byte[] generateReportByDate(LocalDateTime start, LocalDateTime end) {
        List<AccessAttempt> attempts =
                accessAttemptRepository.findByAttemptTimeBetween(start, end);

        return generatePdf(attempts);
    }

    public byte[] generateReportByDepartment(Long departmentId) {
        List<AccessAttempt> attempts =
                accessAttemptRepository.findByDepartmentId(departmentId);

        return generatePdf(attempts);
    }


    private byte[] generatePdf(List<AccessAttempt> attempts) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            CompanyProfile profile = companyProfileRepository
                    .findAll()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (profile != null && profile.getLogoUrl() != null) {
                try {
                    String fileName = profile.getLogoUrl().replace("/uploads/logos/", "");
                    Path path = Paths.get("uploads/logos").resolve(fileName);

                    ImageData data = ImageDataFactory.create(path.toString());
                    Image logo = new Image(data);

                    logo.setWidth(120);
                    logo.setHorizontalAlignment(HorizontalAlignment.CENTER);

                    document.add(logo);
                } catch (Exception e) {
                    System.out.println("No se pudo cargar el logo");
                }
            }

            document.add(new Paragraph("\n"));

            document.add(new Paragraph(profile != null ? profile.getCompanyName() : "ROOM 911")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("REPORTE DE ACCESOS")
                    .setBold()
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Fecha: " + LocalDateTime.now().toLocalDate())
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("\n"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            // 📊 TABLA
            float[] columnWidths = {4, 6, 4, 6};
            Table table = new Table(columnWidths);
            table.useAllAvailableWidth();
            table.setMarginTop(15);

            DeviceGray gray = new DeviceGray(0.85f);

            table.addHeaderCell(createHeaderCell("Empleado", gray));
            table.addHeaderCell(createHeaderCell("Nombre", gray));
            table.addHeaderCell(createHeaderCell("Resultado", gray));
            table.addHeaderCell(createHeaderCell("Fecha", gray));

            for (AccessAttempt a : attempts) {

                String fullName = "N/A";

                Optional<Employee> emp = employeeRepository.findByInternalId(a.getEmployeeCode());

                if (emp.isPresent()) {
                    fullName = emp.get().getFirstName() + " " + emp.get().getLastName();
                }

                table.addCell(createCell(a.getEmployeeCode()));
                table.addCell(
                        new Cell()
                                .add(new Paragraph(fullName))
                                .setTextAlignment(TextAlignment.LEFT)
                                .setPadding(10)
                );
                table.addCell(createResultCell(a.getResult().name()));
                table.addCell(createCell(a.getAttemptTime().format(formatter)));

            }

            document.add(table);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("RESUMEN")
                    .setBold()
                    .setFontSize(14));

            document.add(new Paragraph("\n"));

            long total = attempts.size();
            long authorized = attempts.stream().filter(a -> a.getResult().name().equals("AUTHORIZED")).count();
            long denied = attempts.stream().filter(a -> a.getResult().name().equals("DENIED")).count();
            long notRegistered = attempts.stream().filter(a -> a.getResult().name().equals("NOT_REGISTERED")).count();

            document.add(new Paragraph("Total de accesos: " + total));
            document.add(new Paragraph("Autorizados: " + authorized));
            document.add(new Paragraph("Denegados: " + denied));
            document.add(new Paragraph("No registrados: " + notRegistered));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }

    private Cell createHeaderCell(String text, DeviceGray bgColor) {
        return new Cell()
                .add(new Paragraph(text)
                        .setBold()
                        .setFontSize(11))
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(bgColor)
                .setPadding(10);
    }

    private Cell createCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setFontSize(10))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(10);
    }
    private Cell createResultCell(String result) {

        DeviceGray bgColor;

        switch (result) {
            case "AUTHORIZED":
                bgColor = new DeviceGray(0.8f); // gris claro (puedes cambiar a verde si quieres)
                break;
            case "DENIED":
                bgColor = new DeviceGray(0.6f); // más oscuro
                break;
            default:
                bgColor = new DeviceGray(0.9f); // muy claro
        }

        return new Cell()
                .add(new Paragraph(result).setBold())
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(bgColor)
                .setPadding(5);
    }

    public byte[] generateFullReport() {
        List<AccessAttempt> attempts = accessAttemptRepository.findAll();
        return generatePdf(attempts);
    }


    public byte[] generateExcelReport() {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Accesos");

            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            Row header = sheet.createRow(0);

            String[] columns = {"Empleado", "Resultado", "Fecha"};

            for (int i = 0; i < columns.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = header.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            List<AccessAttempt> attempts = accessAttemptRepository.findAll();

            int rowNum = 1;

            for (AccessAttempt a : attempts) {
                Row row = sheet.createRow(rowNum++);
                org.apache.poi.ss.usermodel.Cell cell0 = row.createCell(0);
                cell0.setCellValue(a.getEmployeeCode());
                row.createCell(1).setCellValue(a.getResult().name());
                row.createCell(2).setCellValue(a.getAttemptTime().toString());
            }


            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error Excel", e);
        }
    }

    public Map<String, Long> getStats() {

        List<AccessAttempt> attempts = accessAttemptRepository.findAll();

        long total = attempts.size();
        long authorized = attempts.stream().filter(a -> a.getResult().name().equals("AUTHORIZED")).count();
        long denied = attempts.stream().filter(a -> a.getResult().name().equals("DENIED")).count();
        long notRegistered = attempts.stream().filter(a -> a.getResult().name().equals("NOT_REGISTERED")).count();

        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("authorized", authorized);
        stats.put("denied", denied);
        stats.put("notRegistered", notRegistered);

        return stats;
    }
}