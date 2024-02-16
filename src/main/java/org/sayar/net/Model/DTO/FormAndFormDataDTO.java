package org.sayar.net.Model.DTO;

import lombok.Data;
import org.sayar.net.Model.Mongo.poll.model.form.FormData;
import org.sayar.net.Model.NewForm;

import java.util.ArrayList;
import java.util.List;

@Data
public class FormAndFormDataDTO {
    private NewForm form;
    private FormData formData;

    public static FormAndFormDataDTO map(NewForm form, FormData formData) {
        FormAndFormDataDTO formAndFormDataDTO = new FormAndFormDataDTO();
        if (form != null)
            formAndFormDataDTO.setForm(form);
        if (formData != null)
            formAndFormDataDTO.setFormData(formData);
        return formAndFormDataDTO;
    }

    public static List<FormAndFormDataDTO> map2(List<NewForm> formList, List<FormData> formDataList) {

        List<FormAndFormDataDTO> formAndFormDataDTOS = new ArrayList<>();

        formList.forEach(form1 -> {
            FormAndFormDataDTO formAndFormDataDTO = new FormAndFormDataDTO();
            formAndFormDataDTO.setForm(form1);
            formDataList.forEach(formData1 -> {
                if (formData1 != null && form1.getId().equals(formData1.getFormId())) {
                    formAndFormDataDTO.setFormData(formData1);
                }
            });
            formAndFormDataDTOS.add(formAndFormDataDTO);
        });
        return formAndFormDataDTOS;
    }
}
