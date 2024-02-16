package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.DTO.MessageDTO;
import org.sayar.net.Model.newModel.Messaging.Message;

import java.util.List;

public interface MessageService extends GeneralService<Message> {

    boolean updateMessage(MessageDTO message);

    Message createMessage(Message message);

    Message getOneMessage(String userId);

    void createNewMessageForAUser(String id);

    List<Message> getUsersMessageByUserIdList(List<String> userIdListForSendingNotification);

    List<Message> getAllUsersMessage(List<String> messageIdList);
}
