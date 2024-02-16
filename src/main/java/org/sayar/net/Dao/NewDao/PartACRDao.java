package org.sayar.net.Dao.NewDao;

import com.mongodb.client.result.UpdateResult;
import org.sayar.net.Model.PartACR;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PartACRDao {
    PartACR savePartACR(PartACR partACR);

    List<PartACR> getAllPartACR(String partId, Pageable pageable, Integer totalElement);

    PartACR getOnePartACR(String ACRID);

    UpdateResult updatePartACR(PartACR partACR);

    boolean checkIfPartACRRegistered(PartACR partACR);

    long countPartACRs(String partId);
}
