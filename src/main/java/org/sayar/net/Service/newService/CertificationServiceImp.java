package org.sayar.net.Service.newService;

import org.sayar.net.Dao.CertificationDao;
import org.sayar.net.General.service.GeneralServiceImpl;
import org.sayar.net.Model.Certification;
import org.sayar.net.Service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificationServiceImp extends GeneralServiceImpl<Certification> implements CertificationService {
    @Autowired
    private CertificationDao certificationDao;

    @Override
    public Certification createUserCertification(Certification certification) {
        return certificationDao.createUserCertification(certification);
    }

    @Override
    public List<Certification> getAllUserCertification() {
        return certificationDao.getAllUserCertification();
    }

    @Override
    public List<Certification> getOneCertification(String userId) {
        return certificationDao.getOneCertification(userId);
    }

    @Override
    public Certification getAnyCertification(String certificationId) {
        return certificationDao.getAnyCertification(certificationId);
    }

    @Override
    public boolean updateCertification(Certification certification) {
        return this.updateResultStatus(certificationDao.updateCertification(certification));
    }

    @Override
    public List<Certification> getUsersCertification(String userId) {
        return certificationDao.getUsersCertification(userId);
    }
}


