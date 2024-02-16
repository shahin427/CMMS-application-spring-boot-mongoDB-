package org.sayar.net.Service.newService;


import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Project;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService extends GeneralService<Project> {

    boolean postOneProject(Project project);

    Page<ProjectDTO> getAllFilterAndPagination(ProjectDTOCodeAndName projectDTOCodeAndName, Pageable pageable, Integer totalElement);

    boolean checkProjectCode(String code);

    List<Project> getAllProjectNameAndCode();

    Project getOne(String projectId);

    List<Project> getAllProjectByProjectIdList(List<String> projectIdList);

    Project getAssociatedProjectToTheSchedule(String projectId);

    boolean updateProject(Project project, String projectId);

    boolean checkIfUserExistsInProject(String userId);

    List<UserWithUserTypeNameDTO> getPersonnelOfProject(String projectId);

    List<ScheduleMaintenance> getProjectScheduleMaintenance(String projectId);

    boolean addPersonnelToProject(String projectId, List<AssignedToPerson> assignedToPersonList);

    boolean addGroupPersonnelToProject(String projectId, List<AssignedToGroup> assignedToGroupList);

    List<ProjectGroupPersonnelDTO> getPersonnelGroupOfProject(String projectId);
}
