package org.sayar.net.Dao.NewDao;


import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrderTasks;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("workOrderTaskDaoImpl")
@Transactional
public class WorkOrderTaskDaoImpl extends GeneralDaoImpl<WorkOrderTasks> implements WorkOrderTaskDao {


    public WorkOrderTaskDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }


}
