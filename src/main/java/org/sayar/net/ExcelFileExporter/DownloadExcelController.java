package org.sayar.net.ExcelFileExporter;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.commons.compress.utils.IOUtils;
import org.sayar.net.Controller.WorkOrderSchedule.dto.ReqWorkOrderScheduleGetPageDto;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleGetPageDto;
import org.sayar.net.Model.DTO.WorkOrderDTO;
import org.sayar.net.Service.WorkOrderSchedule.WorkOrderScheduleService;
import org.sayar.net.Service.newService.WorkOrderService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("excel")
public class DownloadExcelController {
    @Autowired
    private WorkOrderService workOrderService;

    @Autowired
    private WorkOrderScheduleService workOrderScheduleService;

    @PostMapping("/download/work-order")
    public void downloadWorkOrder(HttpServletResponse response, @RequestBody WorkOrderDTO workOrderDTO) throws IOException {
        Print.print("BBBB", workOrderDTO);
        List<WorkOrderDTO> workOrderDTOS = workOrderService.getAllWorkOrderForExcel(workOrderDTO);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=work-order");
        ByteArrayInputStream stream = WorkOrderExcelExporter.workOrderToExcelFile(workOrderDTOS);
        IOUtils.copy(stream, response.getOutputStream());
    }

    @PostMapping("/download/work-order-schedule")
    public void downloadWorkOrder(HttpServletResponse response,
                                  @RequestBody ReqWorkOrderScheduleGetPageDto entity) throws IOException {
        Print.print("BBBB", entity);
        List<WorkOrderScheduleGetPageDto> workOrderDTOS = workOrderScheduleService.getAll(entity);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=work-order-schedule");
        ByteArrayInputStream stream = WorkOrderExcelExporter.workOrderScheduleToExcelFile(workOrderDTOS);
        IOUtils.copy(stream, response.getOutputStream());
    }





}
