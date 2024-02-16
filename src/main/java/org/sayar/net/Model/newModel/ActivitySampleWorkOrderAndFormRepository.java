package org.sayar.net.Model.newModel;

import lombok.Data;
import org.sayar.net.Model.CompletionDetail;
import org.sayar.net.Model.DTO.WorkOrderCreateDTO;
import org.sayar.net.Model.Mongo.poll.model.form.FormData;
import org.sayar.net.Model.NewForm;
import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.Task.model.Task;
import org.sayar.net.Model.newModel.WorkOrder.PartWithUsageCount;
import org.springframework.data.annotation.Id;

import java.util.List;


@Data
public class ActivitySampleWorkOrderAndFormRepository {
    @Id
    private String id;
    private String activityLevelId;
    private String activityInstanceId;
    private WorkOrderCreateDTO workOrderCreateDTO;
    private NewForm form;
    private int numberOfParticipation;
    private FormData formData;
    private List<Notify> notifyList;
    private List<Task> taskList;
    private List<String> taskGroupList;
    private List<PartWithUsageCount> partWithUsageCountList;
    private List<MiscCost> miscCostList;
    private CompletionDetail completionDetail;
    private BasicInformation workOrderBasicInformation;
    private List<DocumentFile> documentList;
}
