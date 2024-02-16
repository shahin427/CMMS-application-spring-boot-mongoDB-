package org.sayar.net.Controller.newController;


import org.sayar.net.Controller.newController.dto.ReqWorkOrderForCalendarGetListDTO;
import org.sayar.net.Controller.newController.dto.ReqWorkOrderPmGetPageDTO;
import org.sayar.net.Controller.newController.dto.ReqWorkOrderScheduleUpdateDTO;
import org.sayar.net.Model.CompletionDetail;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.BasicInformation;
import org.sayar.net.Model.newModel.Task.model.Task;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;
import org.sayar.net.Service.newService.WorkOrderService;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("work-order")
public class WorkOrderController {

    @Autowired
    private WorkOrderService workOrderService;

    @PostMapping("new-save")
    public ResponseEntity<?> newSave(@RequestBody NewSaveDTO newSaveDTO) {
        return ResponseEntity.ok().body(workOrderService.newSaveWorkOrder(newSaveDTO));
    }

    @PutMapping("new-update")
    public ResponseEntity<?> newUpdate(@RequestBody NewSaveDTO newSaveDTO) {
        return ResponseEntity.ok().body(workOrderService.newUpdate(newSaveDTO));
    }
    @PutMapping("schedule-update")
    public ResponseEntity<?> scheduleWorkOrderUpdate(@RequestBody ReqWorkOrderScheduleUpdateDTO entity) {
        return ResponseEntity.ok().body(workOrderService.scheduleWorkOrderUpdate(entity));
    }

    @GetMapping("new-get-one")
    public ResponseEntity<?> newGetOne(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.newGetOne(workOrderId));
    }

    @PostMapping("save")
    public ResponseEntity<?> postCreateWorkOrder(@RequestBody WorkOrderCreateDTO workOrderCreateDTO) {
        return ResponseEntity.ok().body(workOrderService.postCreateWorkOrder(workOrderCreateDTO));
    }

    @PutMapping("update")
    public ResponseEntity<?> updateCreateWorkOrder(@RequestBody WorkOrderCreateDTO workOrderCreateDTO, @PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.updateCreateWorkOrder(workOrderCreateDTO, workOrderId));
    }

    @GetMapping("get-one-create")
    public ResponseEntity<?> getOneCreateWorkOrder(@PathParam("workOrderId") String workOrderId) {
        if (workOrderId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("ورودی فرستاده شده صحیح نمیباشد ", httpStatus);
        } else {
            return ResponseEntity.ok().body(workOrderService.getOneCreateWorkOrder(workOrderId));
        }
    }

    @GetMapping("get-one-basic-information")
    public ResponseEntity<?> getOneBasicInformation(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.getOneBasicInformation(workOrderId));
    }


    @PostMapping("get-all-by-filter-and-pagination")
    public ResponseEntity<?> getAllByFilterAndPagination(@RequestBody WorkOrderDTO workOrderDTO, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(workOrderService.getAllByFilterAndPagination(workOrderDTO, pageable, totalElement));
    }

    @PostMapping("get-all-by-filter-and-pagination/by-schedule")
    public ResponseEntity<?> getPageWorkOrderCreatedBySchedule(
            @RequestBody ReqWorkOrderPmGetPageDTO entity, Pageable pageable, Integer totalElements) {
        return ResponseEntity.ok().body(workOrderService.getPageWorkOrderCreatedBySchedule(entity, pageable, totalElements));
    }
    @PostMapping("get-list/for-calendar")
    public ResponseEntity<?> getListWorkOrderForCalendar(
            @RequestBody ReqWorkOrderForCalendarGetListDTO entity) {
        return ResponseEntity.ok().body(workOrderService.getListWorkOrderForCalendar(entity));
    }


    @PostMapping("get-all/by-schedule")
    public ResponseEntity<?> getAllWorkOrderCreatedBySchedule(
            @RequestBody ReqWorkOrderPmGetPageDTO entity) {
        return ResponseEntity.ok().body(workOrderService.getAllWorkOrderCreatedBySchedule(entity));
    }

    @GetMapping("get-one")
    public ResponseEntity<?> getOne(@RequestParam("id") String id) {
        return ResponseEntity.ok().body(workOrderService.getOne(id));
    }

    @GetMapping("get-misc-cost-list-by-work-order-id")
    public ResponseEntity<?> getMiscCostListByWorkOrderId(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.getMiscCostListByWorkOrderId(workOrderId));
    }

    @GetMapping("get-notify-list-by-work-order-id")
    public ResponseEntity<?> getNotifyListByWorkOrderId(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.getNotifyListByWorkOrderId(workOrderId));
    }

    @GetMapping("get-task-list-by-work-order-id")
    public ResponseEntity<?> getTaskListByWorkOrderId(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.getTaskListByWorkOrderId(workOrderId));
    }

    @GetMapping("get-task-group-list-by-work-order-id")
    public ResponseEntity<?> getTaskGroupListByWorkOrderId(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.getTaskGroupListByWorkOrderId(workOrderId));
    }

    @GetMapping("get-part-list-by-work-order-id")
    public ResponseEntity<?> getPartListByWorkOrderId(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.getPartListByWorkOrderId(workOrderId));
    }

    @GetMapping("check-work-order-code")
    public ResponseEntity<?> checkWorkOrderCode(@PathParam("workOrderCode") String workOrderCode) {
        return ResponseEntity.ok().body(workOrderService.checkWorkOrderCode(workOrderCode));
    }

    @GetMapping("get-completion-detail-by-work-order-id")
    public ResponseEntity<?> getCompletionDetailByWorkOrderId(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.getCompletionDetailByWorkOrderId(workOrderId));
    }

    @GetMapping("get-basic-information-by-work-order-id")
    public ResponseEntity<?> getBasicInformationByWorkOrderId(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.getBasicInformationByWorkOrderId(workOrderId));
    }

    @PutMapping("update-completion-detail-by-work-order-id")
    public ResponseEntity<?> updateCompletionByWorkOrderId(@PathParam("workOrderId") String workOrderId, @RequestBody CompletionDetail completionDetail) {
        return ResponseEntity.ok().body(workOrderService.updateCompletionByWorkOrderId(workOrderId, completionDetail));
    }

    @PutMapping("update-misc-cost-list-by-work-order-id")
    public ResponseEntity<?> updateMiscCostListByWorkOrderId(@RequestBody WorkOrderMiscCostDTO workOrderMiscCostDTO) {
        return ResponseEntity.ok().body(workOrderService.updateMiscCostListByWorkOrderId(workOrderMiscCostDTO));
    }

    @PutMapping("update-basic-information-by-work-order-id")
    public ResponseEntity<?> updateBasicInformationByWorkOrderId(@PathParam("workOrderId") String workOrderId, @RequestBody BasicInformation basicInformation) {
        return ResponseEntity.ok().body(workOrderService.updateBasicInformationByWorkOrderId(workOrderId, basicInformation));
    }

    @PutMapping("update-task-group-list-by-work-order-id")
    public ResponseEntity<?> updateTaskGroupListByWorkOrderId(@RequestBody List<String> taskGroup, @PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.updateTaskGroupListByWorkOrderId(taskGroup, workOrderId));
    }

    @GetMapping("get-work-order-list-by-project-id")
    public ResponseEntity<?> getWorkOrderByProjectId(@PathParam("projectId") String projectId) {
        return ResponseEntity.ok().body(workOrderService.getWorkOrderByProjectId(projectId));
    }

    @GetMapping("get-all-work-order-by-project-id")
    public ResponseEntity<?> getAllWorkOrdersByProjectId(@PathParam("projectId") String projectId) {
        return ResponseEntity.ok().body(workOrderService.getAllWorkOrdersByProjectId(projectId));
    }

    @DeleteMapping("delete-create-work-order")
    public ResponseEntity<?> deleteCreateWorkOrder(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.logicDeleteById(workOrderId, WorkOrder.class));
    }

    @GetMapping("count-work-order-of-user-by-userAssignedId") // // individual
    public ResponseEntity<?> countAllWorkOrderByUserId(@PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.countAllWorkOrderByUserId(userAssignedId));
    }

    @GetMapping("count-all-work-orders-by-specified-month") // // individual
    public ResponseEntity<?> getAllWorkOrdersBySpecifiedMonth(@PathParam("month") int month, @PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.getAllWorkOrdersBySpecifiedMonth(month, userAssignedId));
    }

    @GetMapping("get-all-open-work-orders-by-specified-current-month") // // individual
    public ResponseEntity<?> getAllOpenWorkOrdersByUserIdBySpecifiedCurrentMonth(@PathParam("month") int month,
                                                                                 @PathParam("userAssignedId") String userAssignedId) {
        if (userAssignedId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی کاربر فرستاده نشده است", httpStatus);
        } else {
            return ResponseEntity.ok().body(workOrderService.getAllOpenWorkOrdersByUserIdBySpecifiedCurrentMonth(month, userAssignedId));
        }
    }

    @GetMapping("count-open-work-orders-of-users-by-userAssignedId") // // individual
    public ResponseEntity<?> getAllOpenWorkOrdersOfUsersByUserId(@PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.getAllOpenWorkOrdersOfUsersByUserId(userAssignedId));
    }

    @GetMapping("get-high-priority-work-orders-by-userAssignedId") // // individual
    public ResponseEntity<?> countHighPriorityWorkOrders(@PathParam("userAssignedId") String userAssignedId) {
        if (userAssignedId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی کاربر فرستاده نشده است", httpStatus);
        } else {
            return ResponseEntity.ok().body(workOrderService.countHighPriorityWorkOrders(userAssignedId));
        }
    }

    @GetMapping("count-high-priority-work-orders-by-specified-month") // // individual
    public ResponseEntity<?> getHighPriorityWorkOrdersBySpecifiedMonth(@PathParam("month") int month,
                                                                       @PathParam("userAssignedId") String userAssignedId) {
        if (userAssignedId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی کاربر فرستاده نشده است", httpStatus);
        } else {
            return ResponseEntity.ok().body(workOrderService.getHighPriorityWorkOrdersBySpecifiedMonth(month, userAssignedId));
        }
    }

    @GetMapping("count-closed-work-orders-of-users-by-userAssignedId") // //
    public ResponseEntity<?> countClosedWorkOrdersByUserId(@PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.countAllClosedWorkOrdersByUserId(userAssignedId));
    }

    @GetMapping("get-closed-work-orders-by-user-id-by-specified-time") // // individual
    public ResponseEntity<?> getClosedWorkOrdersByUserIdBySpecifiedMonth(@PathParam("month") int month,
                                                                         @PathParam("userAssignedId") String userAssignedId) {
        if (userAssignedId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی کاربر فرستاده نشده است", httpStatus);
        } else {
            return ResponseEntity.ok().body(workOrderService.getClosedWorkOrdersByUserIdBySpecifiedMonth(month, userAssignedId));
        }
    }

//    @GetMapping("get-closed-not-completed-work-orders-by-userAssignedId")
//    public ResponseEntity<?> countClosedNotCompletedWorkOrdersByUserId(@PathParam("userAssignedId") String userAssignedId) {
//        return ResponseEntity.ok().body(workOrderService.countClosedNotCompletedWorkOrdersByUserId(userAssignedId));
//    }

    @GetMapping("count-closed-not-completed-work-orders-by-user-id-by-specified-month") // // //
    public ResponseEntity<?> getClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth(@PathParam("month") int month, @PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.getClosedNotCompletedWorkOrdersByUserIdBySpecifiedMonth(month, userAssignedId));
    }

    @GetMapping("on-time-completed-work-orders") // // //
    public ResponseEntity<?> onTimeCompletedWorkOrders() {
        return ResponseEntity.ok().body(workOrderService.onTimeCompletedWorkOrders());
    }

    @GetMapping("get-on-time-completed-work-orders-by-specified-time") // // //
    public ResponseEntity<?> getOnTimeCompletedWorkOrdersBySpecifiedMonth(@PathParam("month") int month,
                                                                          @PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.getOnTimeCompletedWorkOrdersBySpecifiedMonth(month, userAssignedId));
    }

    @GetMapping("count-on-time-completed-work-orders-by-specified-month") // // //
    public ResponseEntity<?> countOnTimeCompletedWorkOrdersBySpecifiedMonth(@PathParam("month") int month,
                                                                            @PathParam("user-assigned-id") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.countOnTimeCompletedWorkOrdersBySpecifiedMonth(month, userAssignedId));
    }

    @GetMapping("over-due-and-not-completed-work-orders") // // //
    public ResponseEntity<?> overDueAndNotCompletedWorkOrders() {
        return ResponseEntity.ok().body(workOrderService.overDueAndNotCompletedWorkOrders());
    }

    @GetMapping("get-over-due-and-not-completed-work-orders-by-specified-time") // // //
    public ResponseEntity<?> getOverDueAndNotCompletedWorkOrdersBySpecifiedTime(@PathParam("month") int month,
                                                                                @PathParam("userId") String userId) {
        return ResponseEntity.ok().body(workOrderService.getOverDueAndNotCompletedWorkOrdersBySpecifiedTime(month, userId));
    }

    @GetMapping("count-over-due-and-not-completed-work-orders-by-specified-time") // // //
    public ResponseEntity<?> countOverDueAndNotCompletedWorkOrdersBySpecifiedTime(@PathParam("month") int month, @PathParam("userAssignedId") String userAssignedId) {
        if (userAssignedId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی کاربر فرستاده نشده است", httpStatus);
        } else {
            return ResponseEntity.ok().body(workOrderService.countOverDueAndNotCompletedWorkOrdersBySpecifiedTime(month, userAssignedId));
        }
    }

    @GetMapping("get-over-due-and-completed-work-orders-by-specified-time") // // //
    public ResponseEntity<?> getOverDueAndCompletedWorkOrdersForSpecificUser(@PathParam("month") int month,
                                                                             @PathParam("userId") String userId) {
        return ResponseEntity.ok().body(workOrderService.getOverDueAndCompletedWorkOrders(month, userId));
    }

    @GetMapping("count-over-due-and-completed-work-orders") // // //
    public ResponseEntity<?> countOverDueAndCompletedWorkOrders(@PathParam("month") int month, @PathParam("userId") String userAssignedId) {
        if (userAssignedId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی کاربر فرستاده نشده است", httpStatus);
        } else {
            return ResponseEntity.ok().body(workOrderService.countOverDueAndCompletedWorkOrders(month, userAssignedId));
        }
    }

    @GetMapping("on-time-completion-rate") // // // individual
    public ResponseEntity<?> onTimeCompletionRate() {
        return ResponseEntity.ok().body(workOrderService.onTimeCompletionRate());
    }

    @GetMapping("on-time-completion-rate-by-specified-time") // // // individual
    public ResponseEntity<?> onTimeCompletionRateBySpecifiedTime(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getOnTimeCompletionRateBySpecifiedTime(month));
    }

    @PostMapping("get-all-by-filter-and-pagination-by-user-id")
    public ResponseEntity<?> getAllByFilterAndPaginationByUserId(@PathParam("userId") String userId, @RequestBody WorkOrderDTO workOrderDTO, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(workOrderService.getAllByFilterAndPaginationByUserId(userId, workOrderDTO, pageable, totalElement));
    }


    @GetMapping("get-all-real-work-orders-by-specified-month")
    public ResponseEntity<?> getAllRealWorkOrdersInSpecifiedMonth(@PathParam("month") int month, @PathParam("userAssignedId") String userAssignedId) {
        if (userAssignedId == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("آی دی کاربر فرستاده نشده است", httpStatus);
        } else {
            return ResponseEntity.ok().body(workOrderService.getAllWorkOrdersInSpecifiedMonth(month, userAssignedId));
        }
    }

    @GetMapping("get-real-on-time-completed-work-orders-against-all-completed-work-orders")
    public ResponseEntity<?> getRealOnTimeCompletedWorkOrdersAgainstAllCompletedWorkOrders(@PathParam("userAssignedId") String userAssignedId,
                                                                                           @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getOnTimeCompletedWorkOrdersAgainstAllCompletedWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-all-real-work-orders")
    public ResponseEntity<?> getAllRealWorkOrdersByUserAssignedId(@PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.getAllRealWorkOrdersByUserAssignedId(userAssignedId));
    }


    //_________________________________________________________________________________________
    //-----------------------------------------------------------------------------------------
    @GetMapping("get-real-highest-priority-work-orders")
    public ResponseEntity<?> getRealHighestPriorityWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getHighestPriorityWorkOrdersBySpecifiedMonth(userAssignedId, month));
    }

    @GetMapping("count-real-highest-priority-work-orders")
    public ResponseEntity<?> countRealHighestPriorityWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealHighestPriorityWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-highest-priority-not-planned-work-orders")
    public ResponseEntity<?> getRealHighestPriorityPlannedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getHighestPriorityNotPlannedWorkOrdersBySpecifiedMonth(userAssignedId, month));
    }

    @GetMapping("count-real-highest-priority-not-planned-work-orders")
    public ResponseEntity<?> countRealHighestPriorityNotPlannedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealHighestPriorityNotPlannedWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-high-priority-work-orders")
    public ResponseEntity<?> getRealHighPriorityWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getRealHighPriorityWorkOrders(userAssignedId, month));
    }

    @GetMapping("count-real-high-priority-work-orders")
    public ResponseEntity<?> countRealHighPriorityWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealHighPriorityWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-high-priority-not-planned-work-orders")
    public ResponseEntity<?> getRealHighPriorityNotPlannedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getRealHighPriorityNotPlannedWorkOrders(userAssignedId, month));
    }

    @GetMapping("count-real-high-priority-not-planned-work-orders")
    public ResponseEntity<?> countRealHighPriorityNotPlannedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealHighPriorityNotPlannedWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-average-priority-work-orders")
    public ResponseEntity<?> getRealAverageWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getRealAverageWorkOrders(userAssignedId, month));
    }

    @GetMapping("count-real-average-priority-work-orders")
    public ResponseEntity<?> countRealAveragePriorityWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealAveragePriorityWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-average-priority-not-planned-work-orders")
    public ResponseEntity<?> getRealAveragePriorityNotPlannedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getRealAveragePriorityNotPlannedWorkOrders(userAssignedId, month));
    }

    @GetMapping("count-real-average-priority-not-planned-work-orders")
    public ResponseEntity<?> countRealAveragePriorityNotPlannedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealAveragePriorityNotPlannedWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-low-priority-work-orders")
    public ResponseEntity<?> getRealLowPriorityWorkOrders(@PathParam("assignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getRealLowPriorityWorkOrders(userAssignedId, month));
    }

    @GetMapping("count-real-low-priority-work-orders")
    public ResponseEntity<?> countRealLowPriorityWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealLowPriorityWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-low-priority-not-planned-work-orders")
    public ResponseEntity<?> getRealLowPriorityNotPlannedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getRealLowPriorityNotPlannedWorkOrders(userAssignedId, month));
    }

    @GetMapping("count-real-low-priority-not-planned-work-orders")
    public ResponseEntity<?> countRealLowPriorityNotPlannedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealLowPriorityNotPlannedWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-very-low-priority-work-orders")
    public ResponseEntity<?> getRealVeryLowPriorityWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getRealVeryLowPriorityWorkOrders(userAssignedId, month));
    }

    @GetMapping("count-real-very-low-priority-work-orders")
    public ResponseEntity<?> countRealVeryLowPriorityWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealVeryLowPriorityWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-very-low-priority-not-planned-work-orders")
    public ResponseEntity<?> getRealVeryLowPriorityNotPlannedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getRealVeryLowPriorityNotPlannedWorkOrders(userAssignedId, month));
    }

    @GetMapping("count-real-very-low-priority-not-planned-work-orders")
    public ResponseEntity<?> countRealVeryLowPriorityNotPlannedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealVeryLowPriorityNotPlannedWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-limit-late-and-open-work-orders")
    public ResponseEntity<?> getRealLateAndOpenWorkOrders(@PathParam("userId") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.getRealLateAndOpenWorkOrders(userAssignedId));
    }

    @GetMapping("get-real-pending-work-orders-with-color-status")
    public ResponseEntity<?> getRealPendingWorkOrdersWithColorStatus(@PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.getRealPendingWorkOrdersWithColorStatus(userAssignedId));
    }

    @GetMapping("get-real-current-week-on-work-orders-with-color-status")
    public ResponseEntity<?> getRealCurrentWeekOpenWorkOrdersWithColorStatus(@PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.getRealCurrentWeekOpenWorkOrdersWithColorStatus(userAssignedId));
    }

    @GetMapping("get-real-open-work-orders")
    public ResponseEntity<?> getRealOpenWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getAllRealOpenWorkOrdersBySpecifiedMonth(userAssignedId, month));
    }

    @GetMapping("count-real-open-work-orders")
    public ResponseEntity<?> countRealOpenWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealOpenWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-closed-work-orders")
    public ResponseEntity<?> getRealClosedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getRealClosedWorkOrders(userAssignedId, month));
    }

    @GetMapping("count-real-closed-work-orders")
    public ResponseEntity<?> countRealClosedWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealClosedWorkOrders(userAssignedId, month));
    }

    @GetMapping("get-real-pending-work-orders")
    public ResponseEntity<?> getRealPendingWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getRealPendingWorkOrders(userAssignedId, month));
    }

    @GetMapping("count-real-pending-work-orders")
    public ResponseEntity<?> countRealPendingWorkOrders(@PathParam("month") int month, @PathParam("userAssignedId") String userAssignedId) {
        return ResponseEntity.ok().body(workOrderService.numberOfPendingWorkOrderOfUserInSpecificTime(month, userAssignedId));
    }

    @GetMapping("get-real-draft-work-orders")
    public ResponseEntity<?> getRealDraftWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.getRealDraftWorkOrders(userAssignedId, month));
    }

    @GetMapping("count-real-draft-work-orders")
    public ResponseEntity<?> countRealDraftWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.countRealDraftWorkOrders(userAssignedId, month));
    }

    @GetMapping("real-planned-maintenance-ratio")  // special for manager
    public ResponseEntity<?> realPlannedMaintenanceRatio(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.realPlannedMaintenanceRatio(month));
    }

    @GetMapping("real-on-time-completion-rate")  // special for manager
    public ResponseEntity<?> realOnTimeCompletionRate(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.realOnTimeCompletionRate(month));
    }

    /**
     * manager dashboard
     */

    /**
     * unplanned workOrders
     */
    @GetMapping("manager-dashboard-get-highest-priority-work-orders")
    public ResponseEntity<?> managerDashboardGetHighestPriorityWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetHighestPriorityWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-highest-priority-work-orders")
    public ResponseEntity<?> managerDashboardCountHighestPriorityWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountHighestPriorityWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-high-priority-work-orders")
    public ResponseEntity<?> managerDashboardGetHighPriorityWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetHighPriorityWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-high-priority-work-orders")
    public ResponseEntity<?> managerDashboardCountHighPriorityWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountHighPriorityWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-average-priority-work-orders")
    public ResponseEntity<?> managerDashboardGetAveragePriorityWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetAveragePriorityWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-average-priority-work-orders")
    public ResponseEntity<?> managerDashboardCountAveragePriorityWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountAveragePriorityWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-low-priority-work-orders")
    public ResponseEntity<?> managerDashboardGetLowPriorityWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetLowPriorityWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-low-priority-work-orders")
    public ResponseEntity<?> managerDashboardCountLowPriorityWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountLowPriorityWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-lowest-priority-work-orders")
    public ResponseEntity<?> managerDashboardGetLowestPriorityWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetLowestPriorityWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-lowest-priority-work-orders")
    public ResponseEntity<?> managerDashboardCountLowestPriorityWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountLowestPriorityWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-open-work-orders")
    public ResponseEntity<?> managerDashboardGetOpenWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetOpenWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-open-work-orders")
    public ResponseEntity<?> managerDashboardCountOpenWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountOpenWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-closed-work-orders")
    public ResponseEntity<?> managerDashboardGetClosedWorkOrder(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetClosedWorkOrder(month));
    }

    @GetMapping("manager-dashboard-count-closed-work-orders")
    public ResponseEntity<?> managerDashboardCountClosedWorkOrder(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountClosedWorkOrder(month));
    }

    @GetMapping("manager-dashboard-get-pending-work-orders")
    public ResponseEntity<?> managerDashboardGetPendingWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetPendingWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-pending-work-orders")
    public ResponseEntity<?> managerDashboardCountPendingWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountPendingWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-draft-work-orders")
    public ResponseEntity<?> managerDashboardGetDraftWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetDraftWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-draft-work-orders")
    public ResponseEntity<?> managerDashboardCountDraftWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountDraftWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-over-due-work-orders")
    public ResponseEntity<?> managerDashboardGetOverDueWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetOverDueWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-over-due-work-orders")
    public ResponseEntity<?> managerDashboardCountOverDueWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountOverDueWorkOrders(month));
    }

    /**
     * planned workOrders
     */
    @GetMapping("manager-dashboard-get-highest-priority-planned-work-orders")
    public ResponseEntity<?> managerDashboardGetHighestPriorityPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardHighestPriorityPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-highest-priority-planned-work-orders")
    public ResponseEntity<?> managerDashboardCountHighestPriorityPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountHighestPriorityPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-high-priority-planned-work-orders")
    public ResponseEntity<?> managerDashboardGetHighPriorityPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetHighPriorityPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-high-priority-planned-work-orders")
    public ResponseEntity<?> managerDashboardCountHighPriorityPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountHighPriorityPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-average-priority-planned-work-orders")
    public ResponseEntity<?> managerDashboardGetAveragePriorityPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetAveragePriorityPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-average-priority-planned-work-orders")
    public ResponseEntity<?> managerDashboardCountAveragePriorityPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountAveragePriorityPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-low-priority-planned-work-orders")
    public ResponseEntity<?> managerDashboardGetLowPriorityPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetLowPriorityPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-low-priority-planned-work-orders")
    public ResponseEntity<?> managerDashboardCountLowPriorityPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountLowPriorityPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-lowest-priority-planned-work-orders")
    public ResponseEntity<?> managerDashboardGetLowestPriorityPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetLowestPriorityPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-lowest-priority-planned-work-orders")
    public ResponseEntity<?> managerDashboardCountLowestPriorityPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountLowestPriorityPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-open-planned-work-orders")
    public ResponseEntity<?> managerDashboardGetOpenPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetOpenPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-open-planned-work-orders")
    public ResponseEntity<?> managerDashboardCountOpenPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountOpenPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-closed-planned-work-orders")
    public ResponseEntity<?> managerDashboardGetClosedPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetClosedPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-closed-planned-work-orders")
    public ResponseEntity<?> managerDashboardCountClosedPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountClosedPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-pending-planned-work-orders")
    public ResponseEntity<?> managerDashboardGetPendingPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetPendingPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-pending-planned-work-orders")
    public ResponseEntity<?> managerDashboardCountPendingPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountPendingPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-draft-planned-work-orders")
    public ResponseEntity<?> managerDashboardGetDraftPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetDraftPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-draft-planned-work-orders")
    public ResponseEntity<?> managerDashboardCountDraftPlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountDraftPlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-get-over-due-planned-work-orders")
    public ResponseEntity<?> managerDashboardGetOverDuePlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardGetOverDuePlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-count-over-due-planned-work-orders")
    public ResponseEntity<?> managerDashboardCountOverDuePlannedWorkOrders(@PathParam("month") int month) {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCountOverDuePlannedWorkOrders(month));
    }

    @GetMapping("manager-dashboard-planned-work-orders-ratio")
    public ResponseEntity<?> managerDashboardPlannedWorkOrdersRatio(@PathParam("month") int month) {
        // calculated period is from start of selected month till TODAY
        return ResponseEntity.ok().body(workOrderService.managerDashboardPlannedWorkOrdersRatio(month));
    }

    @GetMapping("manager-dashboard-unscheduled-work-orders-for-bar")
    public ResponseEntity<?> managerDashboardUnscheduledWorkOrdersForBar() {
        return ResponseEntity.ok().body(workOrderService.managerDashboardUnscheduledWorkOrdersForBar());
    }

    @GetMapping("manager-dashboard-pending-work-orders-for-bar")
    public ResponseEntity<?> managerDashboardPendingWorkOrdersForBar() {
        return ResponseEntity.ok().body(workOrderService.managerDashboardPendingWorkOrdersForBar());
    }

    @GetMapping("manager-dashboard-late-work-orders-for-bar")
    public ResponseEntity<?> managerDashboardLateWorkOrdersForBar() {
        return ResponseEntity.ok().body(workOrderService.managerDashboardLateWorkOrdersForBar());
    }

    @GetMapping("manager-dashboard-current-week-work-orders-for-bar")
    public ResponseEntity<?> managerDashboardCurrentWeekWorkOrdersForBar() {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCurrentWeekWorkOrdersBar());
    }

    @GetMapping("manager-dashboard-current-week-planned-work-orders-for-bar")
    public ResponseEntity<?> managerDashboardCurrentPlannedWorkOrdersForBar() {
        return ResponseEntity.ok().body(workOrderService.managerDashboardCurrentPlannedWorkOrdersBar());
    }

    @GetMapping("get-tasks-by-work-order-id")
    public ResponseEntity<?> getTasksByWorkOrderId(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.getTasksByWorkOrderId(workOrderId));
    }

    @GetMapping("get-task-group-of-workOrder")
    public ResponseEntity<?> getTaskGroupOfWorkOrder(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.getTaskGroupOfWorkOrder(workOrderId));
    }

    @PostMapping("save-task-of-work-order")
    public ResponseEntity<?> saveTaskOfWorkOrder(@RequestBody Task task) {
        return ResponseEntity.ok().body(workOrderService.saveTaskOfWorkOrder(task));
    }

    @GetMapping("check-if-work-order-is-in-process")
    public ResponseEntity<?> checkIfWorkOrderIsInProcess(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.checkIfWorkOrderIsInProcess(workOrderId));
    }

    @PostMapping("used-part-of-work-order")
    public ResponseEntity<?> usedPartOfWOrkOrder(@RequestBody UsedPartOfWorkOrder usedPartOfWorkOrder, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(workOrderService.usedPartOfWOrkOrder(usedPartOfWorkOrder, pageable, totalElement));
    }

    @PostMapping("count-used-part-of-work-order")
    public ResponseEntity<?> countUsedPartOfWorkOrder(@RequestBody UsedPartOfWorkOrder usedPartOfWorkOrder) {
        return ResponseEntity.ok().body(workOrderService.countUsedPartOfWorkOrder(usedPartOfWorkOrder));
    }

    @PostMapping("personnel-function")
    public ResponseEntity<?> personnelFunction(@RequestBody PersonnelFunctionDTO personnelFunctionDTO, Pageable pageable, Integer totalElement) {
        return ResponseEntity.ok().body(workOrderService.personnelFunction(personnelFunctionDTO, pageable, totalElement));
    }

    @PostMapping("total-worked-time-of-personnel")
    public ResponseEntity<?> totalWorkedTimeOfPersonnel(@RequestBody PersonnelFunctionDTO personnelFunctionDTO) {
        return ResponseEntity.ok().body(workOrderService.TotalWorkedTimeOfPersonnel(personnelFunctionDTO));
    }

    @PostMapping("mtbf-calculation")
    public ResponseEntity<?> mtbfCalculation(@RequestBody MtbfDTO mtbfDTO) {
        return ResponseEntity.ok().body(workOrderService.mtbfCalculation(mtbfDTO));
    }

    @PostMapping("mttr-calculation")
    public ResponseEntity<?> mttrCalculation(@RequestBody MttrDTO mttrDTO) {
        return ResponseEntity.ok().body(workOrderService.mttrCalculation(mttrDTO));
    }

    @PostMapping("mdt-calculation")
    public ResponseEntity<?> mdtCalculation(@RequestBody MdtDTO mdtDTO) {
        return ResponseEntity.ok().body(workOrderService.mdtCalculation(mdtDTO));
    }

    @PostMapping("mtbf-table")
    public ResponseEntity<?> mtbfTable(@RequestBody MtbfTableDTO mtbfTableDTO) {
        return ResponseEntity.ok().body(workOrderService.mtbfTable(mtbfTableDTO));
    }

    @PostMapping("mttr-table")
    public ResponseEntity<?> mttrTable(@RequestBody MttrTableDTO mttrTableDTO) {
        return ResponseEntity.ok().body(workOrderService.mttrTable(mttrTableDTO));
    }

    @PostMapping("mdt-table")
    public ResponseEntity<?> mdtTable(@RequestBody MdtTableDTO mdtTableDTO) {
        return ResponseEntity.ok().body(workOrderService.mdtTable(mdtTableDTO));
    }

    @GetMapping("is-accepted-by-manager")
    public ResponseEntity<?> isAcceptedByManager(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.isAcceptedByManager(workOrderId));
    }

    @PostMapping("get-all-for-excel")
    public ResponseEntity<?> getAllWorkOrderForExcel(@RequestBody WorkOrderDTO workOrderDTO) {
        return ResponseEntity.ok().body(workOrderService.getAllWorkOrderForExcel(workOrderDTO));
    }

    @GetMapping("get-one-work-order-schedule")
    public ResponseEntity<?> getOneScheduleWorkOrder(@PathParam("workOrderId") String workOrderId) {
        return ResponseEntity.ok().body(workOrderService.getOneScheduleWorkOrder(workOrderId));
    }

    @PutMapping("update-schedule-work-order")
    public ResponseEntity<?> updateScheduleWorkOrder(@RequestBody WorkOrderScheduleDTO workOrderScheduleDTO) {
        return ResponseEntity.ok().body(workOrderService.updateScheduleWorkOrder(workOrderScheduleDTO));
    }

//    @PostMapping("mttr-calculation")
//     public ResponseEntity<?> mttrCalculation() {
//
//     }

//    @GetMapping("count-real-over-due-work-orders")
//    public ResponseEntity<?> countRealOverDueWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
//        return ResponseEntity.ok().body(workOrderService.countRealOverDueWorkOrders(userAssignedId, month));
//    }

//    @GetMapping("get-real-maintenance-type-work-orders")
//    public ResponseEntity<?> getRealMaintenanceTypeWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month,
//                                                              @RequestBody MaintenanceType maintenanceType) {
//        return ResponseEntity.ok().body(workOrderService.getRealMaintenanceTypeWorkOrders(userAssignedId, month, maintenanceType));
//    }

//    @GetMapping("count-real-maintenance-type-work-orders")
//    public ResponseEntity<?> countRealMaintenanceTypeWorkOrders(@PathParam("userAssignedId") String userAssignedId, @PathParam("month") int month) {
//        return ResponseEntity.ok().body(workOrderService.countRealMaintenanceTypeWorkOrders(userAssignedId, month));
//    }
}
