package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.NotifyDTO;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Notify;

import java.util.List;

public interface NotifyService extends GeneralService<Notify> {

    Notify getOneById(String notifyId);

    Notify postNotify(Notify notify);

    boolean updateNotify(Notify notify,String notifyId);

    List<NotifyDTO> getNotifyListByReferenceId(String referenceId);

    void createNotifyByUserAssignedIdOfWorkOrder(User user, String workOrderId);

    boolean updateNotifyOfScheduleMaintenance(Notify notify);

    Notify saveScheduleMaintenanceNotify(Notify notify);

    List<Notify> getAllNotifyOfTheWorkOrderByNotifyId(String id);

    List<Notify> getNotifyListOfTheScheduleMaintenanceBackupById(String scheduleMaintenanceId);
}
