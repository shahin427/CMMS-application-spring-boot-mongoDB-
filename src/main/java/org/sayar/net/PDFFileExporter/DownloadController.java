package org.sayar.net.PDFFileExporter;

import com.lowagie.text.DocumentException;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.commons.compress.utils.IOUtils;
import org.sayar.net.Controller.WorkOrderSchedule.dto.ReqWorkOrderScheduleGetPageDto;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleGetPageDto;
import org.sayar.net.Model.DTO.WorkOrderDTO;
import org.sayar.net.PDFFileExporter.PDFExporter;
import org.sayar.net.Service.WorkOrderSchedule.WorkOrderScheduleService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("pdf")
public class DownloadController {
    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private WorkOrderScheduleService workOrderScheduleService;


//    @PostMapping("/download/work-order-schedule")
//    public void downloadWorkOrder(HttpServletResponse response,
//                                  @RequestBody ReqWorkOrderScheduleGetPageDto entity) throws IOException {
//        Print.print("BBBB", entity);
//        List<WorkOrderScheduleGetPageDto> workOrderDTOS = workOrderScheduleService.getAll(entity);
//        response.setContentType("application/octet-stream");
//        response.setHeader("Content-Disposition", "attachment; filename=work-order-schedule");
//        ByteArrayInputStream stream = WorkOrderExcelExporter.workOrderScheduleToExcelFile(workOrderDTOS);
//        IOUtils.copy(stream, response.getOutputStream());
//    }


    @PostMapping("/download/work-order-schedule")
    public void exportToPDF(HttpServletResponse response,
                            @RequestBody ReqWorkOrderScheduleGetPageDto entity) throws DocumentException, IOException, com.itextpdf.text.DocumentException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<WorkOrderScheduleGetPageDto> workOrderDTOS = workOrderScheduleService.getAll(entity);

        PDFExporter exporter = new PDFExporter(workOrderDTOS);
        exporter.export(response);

    }



}
