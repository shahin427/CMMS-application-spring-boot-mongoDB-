package org.sayar.net.Model;

import lombok.Data;
import org.sayar.net.Enumes.ImageStatus;
import org.sayar.net.Model.newModel.BaseOne.BaseModel1;
import org.sayar.net.Model.newModel.Enum.ImageSaveStatus;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;

//@Entity
@Data
public class TestImg extends BaseModel1{

//    @Column(columnDefinition = "LONGBLOB NOT NULL")
    private byte[] imageData;
    private double size;
    private long width;
    private long height;
//    @Enumerated(EnumType.STRING)
    private ImageStatus imageStatus = ImageStatus.WITHOUT_IMAGE;
//    @Enumerated(EnumType.STRING)
    private ImageSaveStatus saveStatus = ImageSaveStatus.SAVED;
    private String extension;

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public ImageStatus getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(ImageStatus imageStatus) {
        this.imageStatus = imageStatus;
    }

    public ImageSaveStatus getSaveStatus() {
        return saveStatus;
    }

    public void setSaveStatus(ImageSaveStatus saveStatus) {
        this.saveStatus = saveStatus;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
