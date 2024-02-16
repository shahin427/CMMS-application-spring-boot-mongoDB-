package org.sayar.net.Model.newModel.file.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FileAsByte {

    @Id
    private String id;
    @Indexed(direction = IndexDirection.DESCENDING)
    private String uploaderId;
    private byte [] originalByte;
    private byte [] smallByte;
    @Indexed(direction = IndexDirection.ASCENDING)
    private FileCategory category;

    private Date creationDate;
    private String originalFileName;
    private String extension;
    private String categoryId;

    public FileAsByte() {
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public byte[] getOriginalByte() {
        return originalByte;
    }

    public void setOriginalByte(byte[] originalByte) {
        this.originalByte = originalByte;
    }

    public byte[] getSmallByte() {
        return smallByte;
    }

    public void setSmallByte(byte[] smallByte) {
        this.smallByte = smallByte;
    }

    public FileCategory getCategory() {
        return category;
    }

    public void setCategory(FileCategory category) {
        this.category = category;
    }



    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}
