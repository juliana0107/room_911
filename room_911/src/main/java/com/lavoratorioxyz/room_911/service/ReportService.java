package com.lavoratorioxyz.room_911.service;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.lavoratorioxyz.room_911.entity.AccessAttempt;
import com.lavoratorioxyz.room_911.entity.CompanyProfile;
import com.lavoratorioxyz.room_911.entity.Employee;
import com.lavoratorioxyz.room_911.repository.AccessAttemptRepository;
import com.lavoratorioxyz.room_911.repository.CompanyProfileRepository;
import com.lavoratorioxyz.room_911.repository.EmployeeRepository;
import com.itextpdf.layout.properties.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
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
        List<AccessAttempt> attempts = accessAttemptRepository
                .findByAttemptTimeBetween(start, end);

        return generatePdf(attempts);
    }

    public byte[] generateReportByDepartment(Long departmentId) {
        List<AccessAttempt> attempts = accessAttemptRepository
                .findByDepartmentId(departmentId);

        return generatePdf(attempts);
    }

    private byte[] generatePdf(List<AccessAttempt> attempts) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("REPORTE DE ACCESOS - ROOM 911")
                    .setBold()
                    .setFontSize(20));

            document.add(new Paragraph("Fecha: " + LocalDateTime.now().toLocalDate()));
            document.add(new Paragraph("\n"));

            Table table = new Table(new float[]{3, 3, 3, 5});
            table.setWidth(100);

            table.addHeaderCell(new Paragraph("Empleado").setBold());
            table.addHeaderCell(new Paragraph("Nombre").setBold());
            table.addHeaderCell(new Paragraph("Resultado").setBold());
            table.addHeaderCell(new Paragraph("Fecha").setBold());

            for (AccessAttempt a : attempts) {
                table.addCell(a.getEmployeeCode());
                table.addCell("N/A"); // luego lo mejoras con EmployeeRepository
                table.addCell(a.getResult().name());
                table.addCell(a.getAttemptTime().toString());
            }

            document.add(table);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }


    public byte[] generateFullReport() {

        List<AccessAttempt> attempts = accessAttemptRepository.findAll();


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
                Image logo = new Image(ImageDataFactory.create(profile.getLogoUrl()));
                logo.setWidth(100);
                logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(logo);

            }

            // 🧾 TÍTULO
            document.add(new Paragraph("REPORTE DE ACCESOS - ROOM 911")
                    .setBold()
                    .setFontSize(20));

            document.add(new Paragraph("Fecha: " + LocalDateTime.now().toLocalDate()));
            document.add(new Paragraph("\n"));

            Table table = new Table(new float[]{3, 3, 3, 5});
            table.setWidth(100);

            table.addHeaderCell(new Paragraph("Empleado").setBold());
            table.addHeaderCell(new Paragraph("Nombre").setBold()); //quiero que se vea el nombre completo del empleado
            table.addHeaderCell(new Paragraph("Resultado").setBold());
            table.addHeaderCell(new Paragraph("Fecha").setBold());

            for (AccessAttempt a : attempts) {

                String fullName = "N/A";

                Optional<Employee> emp = employeeRepository.findByInternalId(a.getEmployeeCode());

                if (emp.isPresent()) {
                    fullName = emp.get().getFirstName() + " " + emp.get().getLastName();
                }


                table.addCell(a.getEmployeeCode());
                table.addCell(fullName);
                table.addCell(a.getResult().name());
                table.addCell(a.getAttemptTime().toString());
            }

            document.add(table);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("RESUMEN").setBold().setFontSize(14));
            document.add(new Paragraph("\n"));



            // 📈 ESTADÍSTICAS
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
    public byte[] generateExcelReport() {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Accesos");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Empleado");
            header.createCell(1).setCellValue("Resultado");
            header.createCell(2).setCellValue("Fecha");

            List<AccessAttempt> attempts = accessAttemptRepository.findAll();

            int rowNum = 1;

            for (AccessAttempt a : attempts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(a.getEmployeeCode());
                row.createCell(1).setCellValue(a.getResult().name());
                row.createCell(2).setCellValue(a.getAttemptTime().toString());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error Excel", e);
        }
    }
}