package com.uts_saberpro_platform.app.service;

import com.uts_saberpro_platform.app.model.Estudiante;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportService {

    public void exportarEstudiantesExcel(List<Estudiante> estudiantes, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Estudiantes");
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        Row headerRow = sheet.createRow(0);
        String[] columnas = {"ID", "Nombre", "Apellido", "Cédula", "Email", "Teléfono", "Carrera", "Semestre", "Puntaje Saber Pro", "Puntaje TYT", "Beneficio", "Descuento", "Cumple Graduación", "Pago Aprobado"};
        
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 5000);
        }
        
        int rowNum = 1;
        for (Estudiante e : estudiantes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(e.getId());
            row.createCell(1).setCellValue(e.getUsuario().getNombre());
            row.createCell(2).setCellValue(e.getUsuario().getApellido());
            row.createCell(3).setCellValue(e.getUsuario().getCedula());
            row.createCell(4).setCellValue(e.getUsuario().getEmail());
            row.createCell(5).setCellValue(e.getUsuario().getTelefono() != null ? e.getUsuario().getTelefono() : "");
            row.createCell(6).setCellValue(e.getCarrera() != null ? e.getCarrera().getNombre() : "");
            row.createCell(7).setCellValue(e.getSemestre() != null ? e.getSemestre() : 0);
            row.createCell(8).setCellValue(e.getPuntajeSaberPro() != null ? e.getPuntajeSaberPro() : 0);
            row.createCell(9).setCellValue(e.getPuntajeTyt() != null ? e.getPuntajeTyt() : 0);
            row.createCell(10).setCellValue(e.getBeneficioAplicable() != null ? e.getBeneficioAplicable() : "Sin beneficio");
            row.createCell(11).setCellValue(e.getDescuentoGrado() != null ? e.getDescuentoGrado() : 0);
            row.createCell(12).setCellValue(e.getCumpleGraduacion() ? "Sí" : "No");
            row.createCell(13).setCellValue(e.getPagoAprobado() ? "Aprobado" : "Pendiente");
        }
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=estudiantes.xlsx");
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    
    public void exportarInformeGeneralExcel(List<Map<String, Object>> carrerasData, 
                                             long totalEstudiantes, long cumplen, 
                                             long conBeneficio, long pagosAprobados,
                                             HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Informe General");
        
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Resumen general
        Row resumenRow = sheet.createRow(0);
        Cell titleCell = resumenRow.createCell(0);
        titleCell.setCellValue("RESUMEN GENERAL");
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);
        titleCell.setCellStyle(titleStyle);
        
        String[][] resumen = {
            {"Total estudiantes", String.valueOf(totalEstudiantes)},
            {"Cumplen graduación", String.valueOf(cumplen)},
            {"Con beneficios", String.valueOf(conBeneficio)},
            {"Pagos aprobados", String.valueOf(pagosAprobados)}
        };
        
        for (int i = 0; i < resumen.length; i++) {
            Row row = sheet.createRow(i + 2);
            row.createCell(0).setCellValue(resumen[i][0]);
            row.createCell(1).setCellValue(resumen[i][1]);
        }
        
        // Distribución por carrera
        Row tituloCarreras = sheet.createRow(7);
        Cell tituloCell = tituloCarreras.createCell(0);
        tituloCell.setCellValue("DISTRIBUCIÓN POR CARRERA");
        tituloCell.setCellStyle(titleStyle);
        
        Row headerRow = sheet.createRow(8);
        String[] columnas = {"Carrera", "Total estudiantes", "Cumplen graduación", "Con beneficios"};
        for (int i = 0; i < columnas.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columnas[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 6000);
        }
        
        int rowNum = 9;
        for (Map<String, Object> c : carrerasData) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue((String) c.get("nombre"));
            row.createCell(1).setCellValue(((Number) c.get("total")).longValue());
            row.createCell(2).setCellValue(((Number) c.get("cumplen")).longValue());
            row.createCell(3).setCellValue(((Number) c.get("beneficios")).longValue());
        }
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=informe_general.xlsx");
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}