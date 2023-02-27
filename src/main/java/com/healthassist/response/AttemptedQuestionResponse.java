package com.healthassist.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AttemptedQuestionResponse {

	String question;
	Integer answer;

	public AttemptedQuestionResponse(String question, Integer answer) {
		this.question = question;
		this.answer = answer;
	}
}
