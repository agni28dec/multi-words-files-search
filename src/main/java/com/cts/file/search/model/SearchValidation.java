package com.cts.file.search.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "validation-error")
@ApiModel(value="validation-error")
public class SearchValidation{

	@ApiModelProperty(required = true)
	@JsonProperty("error-validation-message")
	private String errorValidationMessages;

	@XmlElement(name = "error-validation-message")
	public String getValidateErrorMessage() {
		return errorValidationMessages;
	}


	public void setValidateErrorMessage(String errorValidationMessages) {
		this.errorValidationMessages = errorValidationMessages;
	}

	public SearchValidation(String errorValidationMessages) {		
		this.errorValidationMessages = errorValidationMessages;
	}
}
