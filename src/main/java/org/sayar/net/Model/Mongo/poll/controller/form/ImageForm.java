package org.sayar.net.Model.Mongo.poll.controller.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.sayar.net.Enumes.ImageStatus;
import org.sayar.net.Model.Mongo.poll.model.elements.BasicElement;
import org.sayar.net.Model.newModel.Enum.ImageSaveStatus;
import org.sayar.net.Tools.Base64Converter;

import java.util.ArrayList;
import java.util.Base64;

//import org.hibernate.annotations.GenericGenerator;
//import javax.persistence.*;

//@Entity
public class ImageForm {


//    @Transient
    public String imageData;
    @JsonIgnore
//    @Column(columnDefinition = "LONGBLOB NOT NULL")
    private byte[] IMAGE;
    private double size;
    private long width;
    private long height;
//    @Enumerated(EnumType.STRING)
    private ImageStatus imageStatus = ImageStatus.WITHOUT_IMAGE;
//    @Enumerated(EnumType.STRING)
    private ImageSaveStatus saveStatus = ImageSaveStatus.SAVED;
    private String extension;
    private ArrayList<BasicElement> subQuestionList = new ArrayList<>();
    @JsonIgnore
    private long organId;

    public ArrayList<BasicElement> getSubQuestionList() {
        return subQuestionList;
    }

    public void setSubQuestionList(ArrayList<BasicElement> subQuestionList) {
        this.subQuestionList = subQuestionList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
//    @Column(name = "id")
    private String id;


//    @Transient
    private String path;
//    @Transient
    private String smallPath;
//    @Transient
    private String alt;
//    @Transient
    private String caption;


//    @Transient



    // refrence

//    @OneToOne(mappedBy = "file")
//    private User user;
//
//
//    @OneToOne(mappedBy = "file")
//    private Category category;

    public ImageForm() {
    }



    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSmallPath() {
        return smallPath;
    }

    public void setSmallPath(String smallPath) {
        this.smallPath = smallPath;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageData() {
        return "download/file/"+this.id;
    }

    public void setImageData(String imageData) {
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

    @JsonIgnore
    public ImageSaveStatus getSaveStatus() {
        return saveStatus;
    }

    public void setSaveStatus(ImageSaveStatus saveStatus) {
        this.saveStatus = saveStatus;
    }

    public long getOrganId() {
        return organId;
    }

    public void setOrganId(long organId) {
        this.organId = organId;
    }

    @JsonIgnore
    public byte[] getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(byte[] IMAGE) {
        this.IMAGE = IMAGE;
    }

    public void createImageBlob(){

        if (Base64Converter.base64Validation(this.imageData)){

            System.out.println(imageData);

//            try {
            byte[] bytes = Base64.getMimeDecoder().decode(this.imageData);
            this.setIMAGE(bytes);
//            } catch (UnsupportedEncodingException e) { }

        }


    }
}
