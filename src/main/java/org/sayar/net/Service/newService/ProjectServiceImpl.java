package org.sayar.net.Service.newService;


import org.sayar.net.Dao.NewDao.ProjectDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.DTO.*;
import org.sayar.net.Model.newModel.Project;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.Scheduling.ScheduleMaintenance;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintananceService;
import org.sayar.net.Service.UserService;
import org.sayar.net.Service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl extends GeneralServiceImpl<Project> implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private ScheduleMaintananceService scheduleMaintananceService;

    @Override
    public boolean postOneProject(Project project) {
        return projectDao.postOneProject(project);
    }

    @Override
    public Page<ProjectDTO> getAllFilterAndPagination(ProjectDTOCodeAndName projectDTOCodeAndName, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                projectDao.getAllProject(projectDTOCodeAndName, pageable, totalElement)
                , pageable
                , projectDao.countProject(projectDTOCodeAndName)
        );
    }

    @Override
    public boolean checkProjectCode(String code) {
        return projectDao.checkProjectCode(code);
    }

    @Override
    public List<Project> getAllProjectNameAndCode() {
        return projectDao.getAllProjectNameAndCode();
    }

    @Override
    public Project getOne(String projectId) {
        return projectDao.getOne(projectId);
    }

    @Override
    public List<Project> getAllProjectByProjectIdList(List<String> projectIdList) {
        return projectDao.getAllProjectByProjectIdList(projectIdList);
    }

    @Override
    public Project getAssociatedProjectToTheSchedule(String projectId) {
        return projectDao.getAssociatedProjectToTheSchedule(projectId);
    }

    @Override
    public boolean updateProject(Project project, String projectId) {
        return updateResultStatus(projectDao.updateProject(project, projectId));
    }

    @Override
    public boolean checkIfUserExistsInProject(String userId) {
        return projectDao.checkIfUserExistsInProject(userId);
    }

    @Override
    public List<UserWithUserTypeNameDTO> getPersonnelOfProject(String projectId) {
        Project project = projectDao.getPersonnelOfProject(projectId);
        if (project.getAssignedToPersonList() != null) {
            List<String> users = new ArrayList<>();
            project.getAssignedToPersonList().forEach(assignedToPerson -> {
                        users.add(assignedToPerson.getUserId());
                    }
            );
            return userService.getPersonnelOfProject(users);
        } else
            return null;
    }

    @Override
    public List<ScheduleMaintenance> getProjectScheduleMaintenance(String projectId) {
//        List<ScheduleMaintenance> scheduleMaintenanceList = scheduleMaintananceService.getProjectScheduleMaintenance(projectId);
        return null;
    }

    @Override
    public boolean addPersonnelToProject(String projectId, List<AssignedToPerson> assignedToPersonList) {
        return projectDao.addPersonnelToProject(projectId, assignedToPersonList);
    }

    @Override
    public boolean addGroupPersonnelToProject(String projectId, List<AssignedToGroup> assignedToGroupList) {
        return projectDao.addGroupPersonnelToProject(projectId, assignedToGroupList);
    }

    @Override
    public List<ProjectGroupPersonnelDTO> getPersonnelGroupOfProject(String projectId) {
        Project project = projectDao.getPersonnelGroupOfProject(projectId);
        if (project.getAssignedToGroupList() != null) {
            List<String> userTypeIdList = new ArrayList<>();
            project.getAssignedToGroupList().forEach(assignedToGroup -> {
                userTypeIdList.add(assignedToGroup.getUserTypeId());
            });
            return userTypeService.getProjectPersonnelUserType(userTypeIdList);
        } else
            return null;
    }
}
