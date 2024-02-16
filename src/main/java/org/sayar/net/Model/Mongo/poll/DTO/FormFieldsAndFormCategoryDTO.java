package org.sayar.net.Model.Mongo.poll.DTO;

import lombok.Data;
import org.sayar.net.Model.Mongo.poll.model.form.FormCategory;

import java.util.ArrayList;
import java.util.List;

@Data
public class FormFieldsAndFormCategoryDTO {
    private String id;
    private String name;
    private String description;
    private FormCategory formCategory;

    public static List<FormFieldsAndFormCategoryDTO> paginationMap(List<FormCategory> formCategoryList, List<FormSearchDTO> searchDTOList) {
        List<FormFieldsAndFormCategoryDTO> formAndFormCategoryDTOS = new ArrayList<>();
        searchDTOList.forEach(formSearchDTO -> {
            FormFieldsAndFormCategoryDTO fieldsAndFormCategoryDTO = new FormFieldsAndFormCategoryDTO();
            if (formSearchDTO.getId() != null)
                fieldsAndFormCategoryDTO.setId(formSearchDTO.getId());
            if (formSearchDTO.getName() != null)
                fieldsAndFormCategoryDTO.setName(formSearchDTO.getName());
            if (formSearchDTO.getDescription() != null)
                fieldsAndFormCategoryDTO.setDescription(formSearchDTO.getDescription());
            for (FormCategory formCategory : formCategoryList) {
                if (formCategory.getId().equals(formSearchDTO.getFormCategoryId())) {
                    fieldsAndFormCategoryDTO.setFormCategory(formCategory);
                }
            }
            formAndFormCategoryDTOS.add(fieldsAndFormCategoryDTO);
        });
        return formAndFormCategoryDTOS;
    }
}
