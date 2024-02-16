package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Notify;

import java.util.List;

public interface NotifyDao extends GeneralDao<Notify> {

    Notify getOneById(String notifyId);

    Notify postNotify(Notify notify);

    UpdateResult updateNotify(Notify notify, String notifyId);

    List<Notify> getAllNotifyByNotifyIdList(List<String> notifications);

    List<Notify> getNotifyListByReferenceId(String referenceId);

    void createNotifyByUserAssignedIdOfWorkOrder(User user, String workOrderId);

    Notify getOldVersionNotify(String notifyId);

    Notify saveScheduleMaintenanceNotify(Notify notify);

    List<Notify> getAllNotifyOfTheWorkOrderByNotifyId(String id);

    Notify newVersionNotify(Notify notify, String notifyId);
}
