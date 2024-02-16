package org.sayar.net.Model.Mongo.poll.model.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 
 * @author yaqub
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Data
public class QuestionAnswer {
	private String questionId;
	private String questionElementType;
	private List<Object> answerIdList;
}
