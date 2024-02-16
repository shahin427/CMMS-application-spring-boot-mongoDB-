package org.sayar.net.Service.newService;

import org.sayar.net.Dao.NewDao.MessageDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.DTO.MessageDTO;
import org.sayar.net.Model.newModel.Messaging.Message;
import org.sayar.net.Tools.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageServiceImpl extends GeneralServiceImpl<Message> implements MessageService {

    @Autowired
    private MessageDao messageDao;

    @Override
    public boolean updateMessage(MessageDTO message) {
        return super.updateResultStatus(messageDao.updateMessage(message));
    }

    @Override
    public Message createMessage(Message message) {
        return messageDao.createMessage(message);
    }

    @Override
    public Message getOneMessage(String userId) {
        return messageDao.getOneMessage(userId);
    }

    @Override
    public void createNewMessageForAUser(String id) {
         messageDao.createNewMessageForAUser(id);
    }

    @Override
    public List<Message> getUsersMessageByUserIdList(List<String> userIdListForSendingNotification) {
        return messageDao.getUsersMessageByUserIdList(userIdListForSendingNotification);
    }

    @Override
    public List<Message> getAllUsersMessage(List<String> messageIdList) {
        return messageDao.getAllUsersMessage(messageIdList);
    }

}
