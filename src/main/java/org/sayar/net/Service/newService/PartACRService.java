package org.sayar.net.Service.newService;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.PartACR;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PartACRService extends GeneralService<PartACR> {

    PartACR savePartACR(PartACR partACR);

    Page<PartACR> getAllPartACR(String partId, Pageable pageable, Integer totalElement);

    PartACR getOnePartACR(String ACRId);

    boolean updatePartACR(PartACR partACR);

    boolean checkIfPartACRRegistered(PartACR partACR);
}
