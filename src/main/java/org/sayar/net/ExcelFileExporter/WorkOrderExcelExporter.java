package org.sayar.net.ExcelFileExporter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleGetPageDto;
import org.sayar.net.Model.DTO.WorkOrderDTO;
import org.sayar.net.Model.JalaliCalendar;
import org.sayar.net.Model.WorkOrderSchedule;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.sayar.net.Enumes.AssetStatus.RUN;
import static org.sayar.net.Enumes.AssetStatus.STOP;
//-----------------------------






public class WorkOrderExcelExporter {
    @Autowired
    private static JalaliCalendar jalaliCalendar;

    public static ByteArrayInputStream workOrderScheduleToExcelFile(List<WorkOrderScheduleGetPageDto> entity) {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("لیست زمانبندی ها ");

            Row row = sheet.createRow(0);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Creating header
            Cell cell = row.createCell(0);
            cell.setCellValue("نام دستگاه");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(1);
            cell.setCellValue("قطعه اصلی");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(2);
            cell.setCellValue("قطعه جزئی");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(3);
            cell.setCellValue("رسته کاری");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(4);
            cell.setCellValue("نوع فعالیت");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(5);
            cell.setCellValue("شرح فعالیت");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(6);
            cell.setCellValue("درجه اهمیت");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(7);
            cell.setCellValue("وضعیت تجهیز");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(8);
            cell.setCellValue("تخمین");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(9);
            cell.setCellValue("تناوب");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(9);
            cell.setCellValue("مدت زمان فعالیت(دقیقه)");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(10);
            cell.setCellValue("وضعیت اجرا");
            cell.setCellStyle(headerCellStyle);

            // Creating data rows for each customer
            for (int i = 0; i < entity.size(); i++) {

                Print.print("entity.get(i)",entity.get(i));
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(entity.get(i).getAssetName());
                dataRow.createCell(1).setCellValue(entity.get(i).getMainSubSystemName());
                dataRow.createCell(2).setCellValue(entity.get(i).getMinorSubSystem());
                dataRow.createCell(3).setCellValue(entity.get(i).getWorkCategoryName());
                dataRow.createCell(4).setCellValue(entity.get(i).getActivityTypeName());
                dataRow.createCell(5).setCellValue(entity.get(i).getSolution());
                dataRow.createCell(6).setCellValue(entity.get(i).getImportanceDegreeName());
                if (entity.get(i).getAssetStatus() == STOP) {
                    dataRow.createCell(7).setCellValue("متوقف");
                } else if (entity.get(i).getAssetStatus() == RUN) {
                    dataRow.createCell(7).setCellValue("در حال کار");
                }
                dataRow.createCell(8).setCellValue(entity.get(i).getEstimateCompletionDate());
                dataRow.createCell(9).setCellValue(entity.get(i).getActivityTime());
                if (entity.get(i).getRunStatus() == WorkOrderSchedule.RunStatus.ACTIVE) {
                    dataRow.createCell(10).setCellValue("فعال");
                } else if (entity.get(i).getRunStatus() == WorkOrderSchedule.RunStatus.DE_ACTIVE) {
                    dataRow.createCell(10).setCellValue("غیر فعال");
                }

            }

            // Making size of column auto resize to fit with data
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static ByteArrayInputStream workOrderToExcelFile(List<WorkOrderDTO> workOrders) {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("لیست دستور کارها");

            Row row = sheet.createRow(0);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Creating header
            Cell cell = row.createCell(0);
            cell.setCellValue("نام دستگاه");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(1);
            cell.setCellValue("تاریخ وقوع خرابی");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(2);
            cell.setCellValue("تاریخ راه‌اندازی");
            cell.setCellStyle(headerCellStyle);

            cell = row.createCell(3);
            cell.setCellValue("نوع درخواست");
            cell.setCellStyle(headerCellStyle);

            // Creating data rows for each customer
            for (int i = 0; i < workOrders.size(); i++) {

                String startDate = null;
                String endDate = null;
                if (workOrders.get(i).getStartDate() != null) {
                    startDate = JalaliCalendar.getJalaliDate(workOrders.get(i).getStartDate());
                    startDate = convertToEnglishDigits(startDate);
                }
                if (workOrders.get(i).getEndDate() != null) {
                    endDate = JalaliCalendar.getJalaliDate(workOrders.get(i).getEndDate());
                    endDate = convertToEnglishDigits(endDate);
                }
                Row dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(workOrders.get(i).getAssetName());
                dataRow.createCell(1).setCellValue(startDate);
                dataRow.createCell(2).setCellValue(endDate);
                if (workOrders.get(i).getFromSchedule().equals(true)) {
                    dataRow.createCell(3).setCellValue("پیشگیرانه");
                } else {
                    dataRow.createCell(3).setCellValue("اضطراری");
                }
            }

            // Making size of column auto resize to fit with data
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            sheet.autoSizeColumn(3);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String convertToEnglishDigits(String value) {
        String newValue = value.replace("1", "۱").replace("2", "۲").replace("3", "۳").replace("4", "۴").replace("5", "۵")
                .replace("6", "۶").replace("7", "٧").replace("8", "۸").replace("9", "۹").replace("0", "۰");
        return newValue;
    }






}
