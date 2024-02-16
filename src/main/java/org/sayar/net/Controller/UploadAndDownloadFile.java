package org.sayar.net.Controller;

import org.sayar.net.Model.newModel.Document.DocumentFile;
import org.sayar.net.Model.newModel.WorkOrder.ScheduleMaintanance.service.ScheduleMaintenanceBackupService;
import org.sayar.net.Service.DocumentService;
import org.sayar.net.exceptionHandling.ApiInputIsInComplete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/")
public class UploadAndDownloadFile {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private ScheduleMaintenanceBackupService scheduleMaintenanceBackupService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        DocumentFile document = new DocumentFile();
        document.setFileByte(multipartFile.getBytes());
        document.setFileName(multipartFile.getOriginalFilename());
        document.setFileContentType(multipartFile.getContentType());
        return ResponseEntity.ok().body(documentService.save(document));
    }

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("ورودی فرستاده شده صحیح نمیباشد", httpStatus);
        } else {
            String fileName = multipartFile.getOriginalFilename();
            if (fileTypeValidation(fileName)) {
                DocumentFile document = new DocumentFile();
                document.setFileByte(multipartFile.getBytes());
                document.setFileName(multipartFile.getOriginalFilename());
                document.setFileContentType(multipartFile.getContentType());
                return ResponseEntity.ok().body(documentService.save(document));
            } else {
                return ResponseEntity.ok().body("\" لطفا فایل فرستاده شده در فرمت عکس باشد \"");
            }
        }
    }

    @PostMapping("upload-with-extra-id")
    public DocumentFile upload(@RequestParam("file") MultipartFile multipartFile,
                               @RequestParam("referenceId") String referenceId,
                               @PathParam("showName") String showName) throws IOException {
        DocumentFile document = new DocumentFile();
        document.setFileByte(multipartFile.getBytes());
        document.setFileName(multipartFile.getOriginalFilename());
        document.setFileContentType(multipartFile.getContentType());
        document.setExtraId(referenceId);
        document.setShowName(showName);
        DocumentFile documentFile = documentService.save(document);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(documentFile.getId()));
        query.fields().exclude("fileByte");
        return mongoOperations.findOne(query, DocumentFile.class);
    }

    @PostMapping("/upload-image-with-extra-id")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile multipartFile,
                                         @RequestParam("referenceId") String referenceId,
                                         @PathParam("showName") String showName) throws IOException {

        if (multipartFile == null || referenceId == null || showName == null) {
            HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
            throw new ApiInputIsInComplete("ورودی فرستاده شده صحیح نمیباشد", httpStatus);
        } else {
            String fileName = multipartFile.getOriginalFilename();
            if (fileTypeValidation(fileName)) {
                DocumentFile document = new DocumentFile();
                document.setFileByte(multipartFile.getBytes());
                document.setFileName(multipartFile.getOriginalFilename());
                document.setFileContentType(multipartFile.getContentType());
                document.setExtraId(referenceId);
                document.setShowName(showName);
                DocumentFile documentFile = documentService.save(document);
                Query query = new Query();
                query.addCriteria(Criteria.where("deleted").ne(true));
                query.addCriteria(Criteria.where("id").is(documentFile.getId()));
                query.fields()
                        .exclude("fileByte");
                return ResponseEntity.ok().body(mongoOperations.findOne(query, DocumentFile.class));
            } else {
                return ResponseEntity.ok().body("\" لطفا فایل فرستاده شده در فرمت عکس باشد \"");
            }
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllDocumentFileByCompanyId(@PathParam("companyId") String companyId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("companyId").is(companyId));
        List<DocumentFile> documents = mongoOperations.find(query, DocumentFile.class);
        return ResponseEntity.ok().body(documents);
    }

    @GetMapping("download")
    public void downloadDocumentFiles(@PathParam("documentId") String documentId, HttpServletResponse response) throws IOException {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(documentId));
        DocumentFile document = mongoOperations.findOne(query, DocumentFile.class);
        if (document != null) {
            response.setContentType(document.getFileContentType());
        }
        response.addHeader("Content-disposition", "attachment; filename=" + document.getFileName());
        response.getOutputStream().write(document.getFileByte());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteDocuments(@PathParam("Id") String Id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(Id));
        return ResponseEntity.ok().body(mongoOperations.remove(query, DocumentFile.class));
    }

    @PostMapping("/upload-with-user-id")
    public ResponseEntity<?> uploadWithUserId(@RequestParam("file") MultipartFile
                                                      multipartFile, @RequestParam("userId") String userId) throws IOException {
        DocumentFile document = new DocumentFile();
        document.setFileByte(multipartFile.getBytes());
        document.setFileName(multipartFile.getOriginalFilename());
        document.setFileContentType(multipartFile.getContentType());
        document.setUserId(userId);
        DocumentFile documentFile = documentService.save(document);
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(documentFile.getId()));
        query.fields().exclude("fileByte");
        return ResponseEntity.ok().body(mongoOperations.findOne(query, DocumentFile.class));
    }

    @GetMapping("get-all-documents-by-user-id")
    public ResponseEntity<?> getAllDocumentsByuUserId(@PathParam("userId") String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(userId));
        return ResponseEntity.ok().body(mongoOperations.findOne(query, DocumentFile.class));
    }

    private boolean fileTypeValidation(String fileName) {
        boolean isValid;
        List<String> splitList = new ArrayList<>(Arrays.asList(fileName.split("\\.")));
        if (splitList.size() == 1) {
            isValid = false;
        } else {
            String type = splitList.get(splitList.size() - 1);
            switch (type) {
                case "jpg":
                    isValid = true;
                    break;
                case "png":
                    isValid = true;
                    break;
                case "psd":
                    isValid = true;
                    break;
                case "tiff":
                    isValid = true;
                    break;
                case "JPG":
                    isValid = true;
                    break;
                case "JPEG":
                    isValid = true;
                    break;
                case "PNG":
                    isValid = true;
                    break;
                case "jpeg":
                    isValid = true;
                    break;
                case "PSD":
                    isValid = true;
                    break;
                case "TIFF":
                    isValid = true;
                    break;
                default:
                    isValid = false;
            }
        }
        return isValid;
    }

    @PostMapping("upload-with-extra-id-for-schedule")
    public DocumentFile uploadForSchedule(@RequestParam("file") MultipartFile multipartFile,
                                          @RequestParam("referenceId") String referenceId,
                                          @PathParam("showName") String showName) throws IOException {
        DocumentFile document = new DocumentFile();
        document.setFileByte(multipartFile.getBytes());
        document.setFileName(multipartFile.getOriginalFilename());
        document.setFileContentType(multipartFile.getContentType());
        document.setExtraId(referenceId);
        document.setShowName(showName);
        document.setForSchedule(true);
        DocumentFile documentFile = documentService.save(document);
        scheduleMaintenanceBackupService.addDocumentIdToBackupSchedule(referenceId, documentFile.getId());
        Query query = new Query();
        query.addCriteria(Criteria.where("deleted").ne(true));
        query.addCriteria(Criteria.where("id").is(documentFile.getId()));
        query.fields()
                .exclude("fileByte");
        return mongoOperations.findOne(query, DocumentFile.class);
    }
}
