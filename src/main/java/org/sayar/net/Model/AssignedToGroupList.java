package org.sayar.net.Model;

import lombok.Data;
import org.sayar.net.Model.Asset.AssignedToGroup;

import java.util.List;

@Data
public class AssignedToGroupList {
    private List<AssignedToGroup> assignedToGroups;
}
