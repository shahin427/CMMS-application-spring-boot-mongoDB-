package org.sayar.net.Service;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.Certification;

import java.util.List;

public interface CertificationService extends GeneralService<Certification> {

    Certification createUserCertification(Certification certification);

    List<Certification> getAllUserCertification();

    List<Certification> getOneCertification(String userId);

    Certification getAnyCertification(String certificationId);

    boolean updateCertification(Certification certification);

    List<Certification> getUsersCertification(String userId);
}
