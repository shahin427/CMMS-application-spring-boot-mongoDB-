package org.sayar.net.Dao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Model.Certification;

import java.util.List;

public interface CertificationDao {

    Certification createUserCertification(Certification certification);

    List<Certification> getAllUserCertification();

    List<Certification> getOneCertification(String userId);

    Certification getAnyCertification(String certificationId);

    UpdateResult updateCertification(Certification certification);

    List<Certification> getUsersCertification(String userId);
}
