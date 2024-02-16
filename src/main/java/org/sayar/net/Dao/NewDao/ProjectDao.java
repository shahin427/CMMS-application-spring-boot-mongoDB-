package org.sayar.net.Dao.NewDao;


import com.mongodb.client.result.UpdateResult;
import org.sayar.net.General.dao.GeneralDao;
import org.sayar.net.Model.Asset.AssignedToGroup;
import org.sayar.net.Model.Asset.AssignedToPerson;
import org.sayar.net.Model.DTO.ProjectDTO;
import org.sayar.net.Model.DTO.ProjectDTOCodeAndName;
import org.sayar.net.Model.newModel.Project;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProjectDao extends GeneralDao<Project> {

    List<Project> getAllProjectNameAndCode();

    boolean postOneProject(Project project);

    List<ProjectDTO> getAllFilterAndPagination(String term, Pageable pageable, Integer totalElement);

    long countProject(ProjectDTOCodeAndName projectDTOCodeAndName);

    List<ProjectDTO> getAllProject(ProjectDTOCodeAndName projectDTOCodeAndName, Pageable pageable, Integer totalElement);

    boolean checkProjectCode(String code);

    Project getOne(String projectId);

    List<Project> getAllProjectByProjectIdList(List<String> projectIdList);

    Project getAssociatedProjectToTheSchedule(String projectId);

    UpdateResult updateProject(Project project, String projectId);

    boolean checkIfUserExistsInProject(String userId);

    Project getPersonnelOfProject(String projectId);

    boolean addPersonnelToProject(String projectId, List<AssignedToPerson> assignedToPersonList);

    boolean addGroupPersonnelToProject(String projectId, List<AssignedToGroup> assignedToGroupList);

    Project getPersonnelGroupOfProject(String projectId);
}
