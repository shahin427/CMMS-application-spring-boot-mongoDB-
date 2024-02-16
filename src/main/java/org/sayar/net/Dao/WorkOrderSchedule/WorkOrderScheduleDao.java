package org.sayar.net.Dao.WorkOrderSchedule;


import org.sayar.net.Controller.WorkOrderSchedule.dto.ReqWorkOrderScheduleForCalenderGetListDto;
import org.sayar.net.Controller.WorkOrderSchedule.dto.ReqWorkOrderScheduleGetPageDto;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleCreateDto;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleGetPageDto;
import org.sayar.net.Controller.newController.dto.ReqWorkOrderForCalendarGetListDTO;
import org.sayar.net.Model.WorkOrderSchedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WorkOrderScheduleDao {


    Boolean createWorkOrderSchedule(WorkOrderScheduleCreateDto entity);

    Map<String, Object> getOneWorkOrderSchedule(String entityId);

    Boolean updateWorkOrderSchedule(WorkOrderSchedule entity);

    Boolean deleteWorkOrderSchedule(String entityId);

    List<WorkOrderSchedule> getAllType();

    Page<WorkOrderScheduleGetPageDto> getPage(ReqWorkOrderScheduleGetPageDto entity, Pageable pageable, Integer total);

    List<WorkOrderSchedule> getAll();

    List<WorkOrderScheduleGetPageDto> getAll(ReqWorkOrderScheduleGetPageDto entity);

    boolean updateNextDate(String id, Date nextDate);

    void deActiveWorkOrderSchedule(String id);

    void makeNextDateNull(String id);

    WorkOrderSchedule getWorkOrderScheduleNextDate(String scheduleId);

    void updateFloatWorkOrderScheduleNextDate(Integer per, WorkOrderSchedule.Frequency frequency, String id, Date endDate,Date nextDate);

    List<ReqWorkOrderScheduleForCalenderGetListDto> getListWorkOrderScheduleForCalendar(ReqWorkOrderForCalendarGetListDTO entity);
}
