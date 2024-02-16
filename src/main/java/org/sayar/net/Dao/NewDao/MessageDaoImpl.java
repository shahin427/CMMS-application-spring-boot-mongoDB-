package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDaoImpl;
import org.sayar.net.Model.DTO.MessageDTO;
import org.sayar.net.Model.newModel.Messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MessageDaoImpl extends GeneralDaoImpl<Message> implements MessageDao {
    public MessageDaoImpl(MongoDatabaseFactory mongoDbFactory) {
        super(mongoDbFactory);
    }

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public UpdateResult updateMessage(MessageDTO message) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userId").is(message.getUserId()));
        Update update = new Update();
        update.set("emailAllMessages", message.isEmailAllMessages());
        update.set("pushNotificationMessages", message.isPushNotificationMessages());
        update.set("draft", message.isDraft());
        update.set("pending", message.isPending());
        update.set("closed", message.isClosed());
        update.set("open", message.isOpen());
        update.set("allAssets", message.isAllAssets());
        update.set("assetsIAmAssignedTo", message.isAssetsIAmAssignedTo());
        update.set("assetsInTheFacilitiesThatIManage", message.isAssetsInTheFacilitiesThatIManage());
        return mongoOperations.updateFirst(query, update, Message.class);
    }

    @Override
    public Message createMessage(Message message) {
        return mongoOperations.save(message);
    }

    @Override
    public Message getOneMessage(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userId").is(userId));
        return mongoOperations.findOne(query, Message.class);
    }

    @Override
public void createNewMessageForAUser(String id) {
        Message message = new Message();
        message.setUserId(id);
        message.setEmailAllMessages(false);
        message.setPushNotificationMessages(false);
        message.setDraft(false);
        message.setClosed(false);
        message.setOpen(false);
        message.setAllAssets(false);
        message.setAssetsIAmAssignedTo(false);
        message.setAssetsInTheFacilitiesThatIManage(false);
        mongoOperations.save(message);
    }

    @Override
    public List<Message> getUsersMessageByUserIdList(List<String> userIdListForSendingNotification) {
        Query query=new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("userId").in(userIdListForSendingNotification));
        return mongoOperations.find(query,Message.class);
    }

    @Override
    public List<Message> getAllUsersMessage(List<String> messageIdList) {
        Query query=new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").in(messageIdList));
        return mongoOperations.find(query,Message.class);
    }


}
