package org.sayar.net.PDFFileExporter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleGetPageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class PDFGenerator {

    private static Logger logger = LoggerFactory.
            getLogger(PDFGenerator.class);

    public static ByteArrayInputStream employeePDFReport
            (List<WorkOrderScheduleGetPageDto> entity) throws IOException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();
            BaseFont urName = BaseFont.createFont("/home/masoud/workSpace/F_zomorod.ttf", "UTF-8",BaseFont.EMBEDDED);
            // Add Text to PDF file ->
            Font font =new Font(urName, 12);;
            Paragraph para = new Paragraph("Employee Table", font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(3);
            // Add PDF Table Header ->
            Stream.of("سلام ", "مسعود", "نظری").forEach(headerTitle ->
            {
//                FontFactory.register("/resources/fonts/arial.ttf", "my_bold_font");


                PdfPCell header = new PdfPCell();
                BaseFont urName1 = null;
                try {
                    urName1 = BaseFont.createFont("/home/masoud/workSpace/F_zomorod.ttf", "UTF-8",BaseFont.EMBEDDED);
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Add Text to PDF file ->


                Font headFont = new Font(urName1, 18);
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);
            });

            for (WorkOrderScheduleGetPageDto entit : entity) {
                PdfPCell idCell = new PdfPCell(new Phrase(entit.getId().
                        toString()));
                idCell.setPaddingLeft(4);
                idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(idCell);

                PdfPCell firstNameCell = new PdfPCell(new Phrase(entit.getImportanceDegreeName()));
                firstNameCell.setPaddingLeft(4);
                firstNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                firstNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(firstNameCell);

                PdfPCell lastNameCell = new PdfPCell(new Phrase
                        (String.valueOf(entit.getAssetName())));
                lastNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                lastNameCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                lastNameCell.setPaddingRight(4);
                table.addCell(lastNameCell);
            }
            document.add(table);

            document.close();
        } catch (DocumentException e) {
            logger.error(e.toString());
        }

        return new ByteArrayInputStream(out.toByteArray());

    }
}