package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.User;
import org.sayar.net.Model.newModel.Enum.NotifyEvent;
import org.sayar.net.Model.newModel.Notify;
import org.sayar.net.Tools.Print;

import java.util.ArrayList;
import java.util.List;

@Data
public class NotifyDTO {
    private String id;
    private User user;
    private List<NotifyEvent> events;
    private String referenceId;

    public static List<NotifyDTO> map(List<Notify> notifyList) {
        List<NotifyDTO> notifyDTOList = new ArrayList<>();
        notifyList.forEach(notify -> {
                    NotifyDTO notifyDTO = new NotifyDTO();
                    notifyDTO.setId(notify.getId());
                    notifyDTO.setEvents(notify.getEvents());
                    notifyDTO.setReferenceId(notify.getReferenceId());
                    notifyDTO.setUser(notify.getUser());
                    notifyDTOList.add(notifyDTO);
                }
        );
        Print.print("NOTIFY",notifyDTOList);
        return notifyDTOList;
    }
}
