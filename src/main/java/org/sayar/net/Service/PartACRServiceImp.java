package org.sayar.net.Service;

import org.sayar.net.Dao.NewDao.PartACRDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.PartACR;
import org.sayar.net.Service.newService.PartACRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PartACRServiceImp extends GeneralServiceImpl<PartACR> implements PartACRService {
    @Autowired
    private PartACRDao partACRDao;

    @Override
    public PartACR savePartACR(PartACR partACR) {
        return partACRDao.savePartACR(partACR);
    }

    @Override
    public Page<PartACR> getAllPartACR(String partId, Pageable pageable, Integer totalElement) {
        return new PageImpl<>(
                partACRDao.getAllPartACR(partId, pageable, totalElement),
                pageable,
                partACRDao.countPartACRs(partId)
        );
    }

    @Override
    public PartACR getOnePartACR(String ACRID) {
        return partACRDao.getOnePartACR(ACRID);
    }

    @Override
    public boolean updatePartACR(PartACR partACR) {
        return super.updateResultStatus(partACRDao.updatePartACR(partACR));
    }

    @Override
    public boolean checkIfPartACRRegistered(PartACR partACR) {
        return partACRDao.checkIfPartACRRegistered(partACR);
    }
}
