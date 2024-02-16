package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Asset.Budget;
import org.sayar.net.Model.Asset.ChargeDepartment;
import org.sayar.net.Model.newModel.WorkOrder.WorkOrder;

@Data
public class CompletionDetailDTO {
    private String budgetId;
    private String budgetTitle;
    private String chargeDepartmentId;
    private String chargeDepartmentTitle;
    private String note;
    private String problem;
    private String rootCause;
    private String solution;
    private String adminNote;

    public static CompletionDetailDTO map(WorkOrder workOrder, Budget budgetTitle, ChargeDepartment chargeDepartmentTitle) {

        CompletionDetailDTO completionDetailDTO = new CompletionDetailDTO();
        if (workOrder.getCompletionDetail() != null) {
            if (workOrder.getCompletionDetail().getAdminNote() != null)
                completionDetailDTO.setAdminNote(workOrder.getCompletionDetail().getAdminNote());
            if (workOrder.getCompletionDetail().getProblem() != null)
                completionDetailDTO.setProblem(workOrder.getCompletionDetail().getProblem());
            if (workOrder.getCompletionDetail().getRootCause() != null)
                completionDetailDTO.setRootCause(workOrder.getCompletionDetail().getRootCause());
            if (workOrder.getCompletionDetail().getSolution() != null)
                completionDetailDTO.setSolution(workOrder.getCompletionDetail().getSolution());
            if (workOrder.getCompletionDetail().getNote() != null)
                completionDetailDTO.setNote(workOrder.getCompletionDetail().getNote());
        }
        if (budgetTitle != null) {
            if (budgetTitle.getId() != null)
                completionDetailDTO.setBudgetId(budgetTitle.getId());
            if (budgetTitle.getTitle() != null)
                completionDetailDTO.setBudgetTitle(budgetTitle.getTitle());
        }
        if (chargeDepartmentTitle != null) {
            if (chargeDepartmentTitle.getId() != null)
                completionDetailDTO.setChargeDepartmentId(chargeDepartmentTitle.getId());
            if (chargeDepartmentTitle.getTitle() != null)
                completionDetailDTO.setChargeDepartmentTitle(chargeDepartmentTitle.getTitle());
        }
        return completionDetailDTO;

    }
}
