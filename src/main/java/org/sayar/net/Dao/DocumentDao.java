package org.sayar.net.Dao;

import org.sayar.net.Model.newModel.Document.DocumentFile;

import java.util.List;

public interface DocumentDao {

    List<DocumentFile> getAllByExtraId(String extraId);

    List<DocumentFile> getAllByUserId(String userId);

    List<DocumentFile> getAllDocumentsOfWorkOrderByExtraId(List<String> documentsList);

    List<DocumentFile> getDocumentsOfRepository(List<String> documentsList);
}
