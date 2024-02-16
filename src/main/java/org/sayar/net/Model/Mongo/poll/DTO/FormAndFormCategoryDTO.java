package org.sayar.net.Model.Mongo.poll.DTO;

import lombok.Data;
import org.sayar.net.Model.Mongo.poll.model.form.Form;
import org.sayar.net.Model.Mongo.poll.model.form.FormCategory;

import java.util.ArrayList;
import java.util.List;

@Data
public class FormAndFormCategoryDTO {
    private Form form;
    private FormCategory formCategory;

    public static List<FormAndFormCategoryDTO> map(List<Form> forms, List<FormCategory> formCategoryList) {
        List<FormAndFormCategoryDTO> formAndFormCategoryDTOS = new ArrayList<>();
        forms.forEach(singleForm -> {
            FormAndFormCategoryDTO formAndFormCategoryDTO = new FormAndFormCategoryDTO();
            formAndFormCategoryDTO.setForm(singleForm);
            formCategoryList.forEach(singleFormCategory -> {
                if (singleFormCategory.getId().equals(singleForm.getFormCategoryId())) {
                    formAndFormCategoryDTO.setFormCategory(singleFormCategory);
                }
            });
            formAndFormCategoryDTOS.add(formAndFormCategoryDTO);
        });
        return formAndFormCategoryDTOS;
    }
}
