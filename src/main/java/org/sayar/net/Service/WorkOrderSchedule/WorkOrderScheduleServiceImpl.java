package org.sayar.net.Service.WorkOrderSchedule;


import org.sayar.net.Controller.WorkOrderSchedule.dto.ReqWorkOrderScheduleForCalenderGetListDto;
import org.sayar.net.Controller.WorkOrderSchedule.dto.ReqWorkOrderScheduleGetPageDto;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleCreateDto;
import org.sayar.net.Controller.WorkOrderSchedule.dto.WorkOrderScheduleGetPageDto;
import org.sayar.net.Controller.newController.dto.ReqWorkOrderForCalendarGetListDTO;
import org.sayar.net.Dao.WorkOrderSchedule.WorkOrderScheduleDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.UserDTO;
import org.sayar.net.Model.WorkOrderSchedule;
import org.sayar.net.Service.UserService;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("WorkOrderScheduleServiceImpl")
public class WorkOrderScheduleServiceImpl extends GeneralServiceImpl<WorkOrderSchedule> implements WorkOrderScheduleService {
    @Autowired
    private WorkOrderScheduleDao entityDao;
    @Autowired
    private UserService userService;


    @Override
    public Boolean createWorkOrderSchedule(WorkOrderScheduleCreateDto entity) {
        return entityDao.createWorkOrderSchedule(entity);
    }

    @Override
    public Map<String, Object> getOneWorkOrderSchedule(String entityId) {
        return entityDao.getOneWorkOrderSchedule(entityId);
    }

    @Override
    public boolean updateWorkOrderSchedule(WorkOrderSchedule entity) {
        return entityDao.updateWorkOrderSchedule(entity);
    }

    @Override
    public boolean updateNextDate(String id, Date nextDate) {
        return entityDao.updateNextDate(id, nextDate);
    }

    @Override
    public void deActiveWorkOrderSchedule(String id) {
        entityDao.deActiveWorkOrderSchedule(id);
    }

    @Override
    public Boolean deleteWorkOrderSchedule(String id) {
        return entityDao.deleteWorkOrderSchedule(id);
    }


    @Override
    public List<WorkOrderSchedule> getAllType() {
        return entityDao.getAllType();
    }

    @Override
    public Page<WorkOrderScheduleGetPageDto> getPage(ReqWorkOrderScheduleGetPageDto entity, Pageable pageable, Integer total) {
        return entityDao.getPage(entity, pageable, total);
    }

    @Override
    public List<WorkOrderScheduleGetPageDto> getAll(ReqWorkOrderScheduleGetPageDto entity) {
        return entityDao.getAll(entity);
    }

    @Override
    public List<WorkOrderSchedule> getAll() {
        return entityDao.getAll();
    }

    @Override
    public void makeNextDateNull(String id) {
        entityDao.makeNextDateNull(id);
    }

    @Override
    public WorkOrderSchedule getWorkOrderScheduleNextDate(String scheduleId) {
        return entityDao.getWorkOrderScheduleNextDate(scheduleId);
    }

    @Override
    public void updateFloatWorkOrderScheduleNextDate(Integer per, WorkOrderSchedule.Frequency frequency, String id, Date endDate, Date nextDate) {
        entityDao.updateFloatWorkOrderScheduleNextDate(per, frequency, id, endDate, nextDate);
    }

    @Override
    public Map<String, Object> getListWorkOrderScheduleForCalendar(ReqWorkOrderForCalendarGetListDTO entity) {
        List<ReqWorkOrderScheduleForCalenderGetListDto> workOrder = entityDao.getListWorkOrderScheduleForCalendar(entity);
        if (workOrder.size() <= 0) {
            return null;
        }
        Set<String> userIdList = new HashSet<>();
        workOrder.forEach(a -> {
            userIdList.addAll( a.getUserIdList());
        });
        Print.print("userIdList",userIdList);
        List<UserDTO> userList = new ArrayList<>();
        if (userIdList.size() > 0)
            userList = userService.getByIdList(userIdList);

        Print.print("userlist",userList);
        Map<String, Object> result = new HashMap<>();
        result.put("workOrder", workOrder);
        result.put("userList", userList);
        return result;
    }

}
