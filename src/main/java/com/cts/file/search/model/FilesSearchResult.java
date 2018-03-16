package com.cts.file.search.model;

import io.swagger.annotations.ApiModelProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "file")
public class FilesSearchResult {

	@ApiModelProperty(required = true)
	@JsonProperty("file-name")	
	private String fileName;

	@ApiModelProperty(required = true)
	@JsonProperty("file-path")
	private String filePath;


	public String getFileName() {
		return fileName;
	}

	@XmlElement(name="file-name")
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	@XmlElement(name="file-path")
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}	

}
