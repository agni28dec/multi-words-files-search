package com.cts.file.search.model;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlSeeAlso({ FilesSearchResult.class })
@XmlRootElement(name = "filessearchresult")
@ApiModel(value = "filessearchresult")
public class FilesSearchResultList<T> {

	@ApiModelProperty(required = true, name = "matchCount")
	@JsonProperty("matchCount")
	private int matchCount;

	@ApiModelProperty(required = true, name = "matchedFiles")
	@JsonProperty("matchedFiles")
	private List<T> matchedFiles = new ArrayList<>();

	@XmlElement(name = "matchCount")
	public int getMatchCount() {
		return matchCount;
	}

	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}

	@XmlElement(name = "matchedFiles")
	public List<T> getMatchedFiles() {
		return matchedFiles;
	}

	public void setMatchedFiles(List<T> matchedFiles) {
		this.matchedFiles = matchedFiles;
	}

}
