package org.sayar.net.Service.WorkOrderSchedule;

import org.sayar.net.Controller.WorkOrderSchedule.dto.ReqWorkOrderScheduleGetPageDto;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleCreateDto;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleGetPageDto;
import org.sayar.net.Controller.newController.dto.ReqWorkOrderForCalendarGetListDTO;
import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.WorkOrderSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WorkOrderScheduleService extends GeneralService<WorkOrderSchedule> {


    Boolean createWorkOrderSchedule(WorkOrderScheduleCreateDto entity);

    Map<String, Object> getOneWorkOrderSchedule(String id);

    boolean updateWorkOrderSchedule(WorkOrderSchedule entity);

    boolean updateNextDate(String id, Date nextDate);

    void deActiveWorkOrderSchedule(String id);

    Boolean deleteWorkOrderSchedule(String id);

    List<WorkOrderSchedule> getAllType();

    Page<WorkOrderScheduleGetPageDto> getPage(ReqWorkOrderScheduleGetPageDto entity, Pageable pageable, Integer total);

    List<WorkOrderScheduleGetPageDto> getAll(ReqWorkOrderScheduleGetPageDto entity);

    List<WorkOrderSchedule> getAll();

    void makeNextDateNull(String id);

    WorkOrderSchedule getWorkOrderScheduleNextDate(String scheduleId);

    void updateFloatWorkOrderScheduleNextDate(Integer per, WorkOrderSchedule.Frequency frequency, String id, Date endDate, Date nextDate);

    Map<String, Object> getListWorkOrderScheduleForCalendar(ReqWorkOrderForCalendarGetListDTO entity);
}
