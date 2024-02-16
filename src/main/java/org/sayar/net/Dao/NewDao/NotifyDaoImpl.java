package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Enum.NotifyEvent;
import org.sayar.net.Model.newModel.Notify;
import org.sayar.net.exceptionHandling.ApiExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.sayar.net.Model.newModel.Enum.NotifyEvent.*;

@Repository("notifyDaoImpl")
@Transactional
public class NotifyDaoImpl extends GeneralDaoImpl<Notify> implements NotifyDao {
    public NotifyDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public Notify getOneById(String notifyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(notifyId));
        query.addCriteria(Criteria.where("deleted").ne(true));
        return mongoOperations.findOne(query, Notify.class);
    }

    @Override
    public Notify postNotify(Notify notify) {
        return mongoOperations.save(notify);
    }

    @Override
    public UpdateResult updateNotify(Notify notify, String notifyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(notifyId));
        Update update = new Update();
        update.set("id", notify.getId());
        update.set("referenceId", notify.getReferenceId());
        update.set("user", notify.getUser());
        update.set("events", notify.getEvents());
        return mongoOperations.updateFirst(query, update, Notify.class);
    }

    @Override
    public List<Notify> getAllNotifyByNotifyIdList(List<String> notifications) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(notifications));
        return mongoOperations.find(query, Notify.class);
    }

    @Override
    public List<Notify> getNotifyListByReferenceId(String referenceId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("referenceId").is(referenceId));
        return mongoOperations.find(query, Notify.class);
    }

    @Override
    public void createNotifyByUserAssignedIdOfWorkOrder(User user, String workOrderId) {
        List<NotifyEvent> notifyEventList = new ArrayList<>();
        notifyEventList.add(ONSTATUSCHANG);
        notifyEventList.add(ONONLINEOFFLINE);

        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("referenceId").is(workOrderId));
        Notify notify = mongoOperations.findOne(query, Notify.class);

        if (notify != null) {
            Update update = new Update();
            update.set("user", user);
            update.set("events", notifyEventList);
            mongoOperations.updateFirst(query, update, Notify.class);
        } else {
            Notify notify1 = new Notify();
            notify1.setUser(user);
            notify1.setReferenceId(workOrderId);
            notify1.setEvents(notifyEventList);
            mongoOperations.save(notify1);
        }
    }

    @Override
    public Notify getOldVersionNotify(String notifyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(notifyId));
        return mongoOperations.findOne(query, Notify.class);
    }

    @Override
    public Notify saveScheduleMaintenanceNotify(Notify notify) {
        return mongoOperations.save(notify);
    }

    @Override
    public List<Notify> getAllNotifyOfTheWorkOrderByNotifyId(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("referenceId").is(id));
        return mongoOperations.find(query, Notify.class);
    }

    @Override
    public Notify newVersionNotify(Notify notify, String notifyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(notifyId));
        Update update = new Update();
        update.set("id", notify.getId());
        update.set("referenceId", notify.getReferenceId());
        update.set("user", notify.getUser());
        update.set("events", notify.getEvents());
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        return findAndModify(query, update, options, Notify.class);
    }
}
