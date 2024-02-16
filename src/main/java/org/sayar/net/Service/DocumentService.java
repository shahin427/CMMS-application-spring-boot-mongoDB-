package org.sayar.net.Service;

import org.sayar.net.General.service.GeneralService;
import org.sayar.net.Model.newModel.Document.DocumentFile;

import java.util.List;

public interface DocumentService extends GeneralService<DocumentFile>  {

    List<DocumentFile> getAllByExtraId(String extraId);

    List<DocumentFile> getAllByUserId(String userId);

    List<DocumentFile> getAllDocumentsOfWorkOrderByExtraId(List<String> documentsList);

    List<DocumentFile> getDocumentsOfRepository(List<String> documentsList);
}
