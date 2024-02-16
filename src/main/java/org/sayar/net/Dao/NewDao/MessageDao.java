package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.DTO.MessageDTO;
import org.sayar.net.Model.newModel.Messaging.Message;

import java.util.List;

public interface MessageDao extends GeneralDao<Message> {

    UpdateResult updateMessage(MessageDTO message);

    Message createMessage(Message message);

    Message getOneMessage(String messageId);

    void createNewMessageForAUser(String id);

    List<Message> getUsersMessageByUserIdList(List<String> userIdListForSendingNotification);

    List<Message> getAllUsersMessage(List<String> messageIdList);
}
