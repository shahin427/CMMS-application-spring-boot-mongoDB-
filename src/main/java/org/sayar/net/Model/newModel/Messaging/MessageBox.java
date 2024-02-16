package org.sayar.net.Model.newModel.Messaging;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.sayar.net.Model.newModel.BaseOne.BaseModel1;

import java.util.List;

//import javax.persistence.*;

//@Entity
//@Table(name = "system_message_box")
public class MessageBox extends BaseModel1 {


    @JsonIgnoreProperties(ignoreUnknown = true)
//    @OneToMany(mappedBy = "messageBox")
    private List<Message> messages;

    public MessageBox() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}
