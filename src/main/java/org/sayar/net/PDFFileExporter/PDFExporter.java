package org.sayar.net.PDFFileExporter;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleGetPageDto;
import org.sayar.net.Model.WorkOrderSchedule;

import static org.sayar.net.Enumes.AssetStatus.RUN;
import static org.sayar.net.Enumes.AssetStatus.STOP;


public class PDFExporter {
    private List<WorkOrderScheduleGetPageDto> workOrderDTOS;

    public PDFExporter(List<WorkOrderScheduleGetPageDto> workOrderDTOS) {
        this.workOrderDTOS = workOrderDTOS;
    }

    private void writeTableHeader(PdfPTable table) throws IOException, com.itextpdf.text.DocumentException {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(11);

        BaseFont urName = BaseFont.createFont("/home/masoud/workSpace/BRoya.ttf", "UTF-8", com.itextpdf.text.pdf.BaseFont.EMBEDDED);
        // Add Text to PDF file ->
        Font font =new Font(urName, 12);;
//        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
//        font.setCharSet(FontCharset.ARABIC);
//        font.setFontHeightInPoints((short)24);
//        font.setFontName("B Nazanin");

        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("نام دستگاه", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("قطعه اصلی", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("قطعه جزئی", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("رسته کاری", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("نوع فعالیت", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("شرح فعالیت", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("درجه اهمیت", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("وضعیت تجهیز", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("تخمین", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("تناوب", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("مدت زمان فعالیت(دقیقه)", font));
        table.addCell(cell);

    }

    private void writeTableData(PdfPTable table) {
        for (WorkOrderScheduleGetPageDto user : workOrderDTOS) {

            table.addCell(user.getAssetName());
            table.addCell(user.getMainSubSystemName());
            table.addCell(user.getMinorSubSystem());
            table.addCell(user.getWorkCategoryName());
            table.addCell(user.getActivityTypeId());
            table.addCell(user.getSolution());
            table.addCell(user.getImportanceDegreeName());
            if (user.getAssetStatus() == STOP) {
                table.addCell("متوقف");
            } else if (user.getAssetStatus() == RUN) {
                table.addCell("در حال کار");
            }
            table.addCell(user.getEstimateCompletionDate().toString());
            table.addCell(user.getActivityTime().toString());
            if (user.getRunStatus() == WorkOrderSchedule.RunStatus.ACTIVE) {
                table.addCell("فعال");
            } else if (user.getRunStatus() == WorkOrderSchedule.RunStatus.DE_ACTIVE) {
                table.addCell("غیر فعال");
            }
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException, com.itextpdf.text.DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        BaseFont urName = BaseFont.createFont("/home/masoud/workSpace/BRoya.ttf", "UTF-8", com.itextpdf.text.pdf.BaseFont.EMBEDDED);
        // Add Text to PDF file ->
        Font font =new Font(urName, 12);;
//        Font font = FontFactory.getFont(FontFactory.TIMES);
        font.setSize(12);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("List of Users", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(11);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f,1.5f, 3.5f, 3.0f, 3.0f, 1.5f, 1.5f});
        table.setSpacingBefore(11);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}