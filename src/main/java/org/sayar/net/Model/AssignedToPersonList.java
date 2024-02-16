package org.sayar.net.Model;

import lombok.Data;
import org.sayar.net.Model.Asset.AssignedToPerson;

import java.util.List;

@Data
public class AssignedToPersonList {
    private List<AssignedToPerson> assignedToPersonList;
}
