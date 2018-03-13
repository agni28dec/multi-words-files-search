package com.cts.file.search.model;

public class FilesSearchList {

	private String userMessage;
	private String fileList;


	public FilesSearchList(String userMessage,String fileList) {
		this.userMessage = userMessage;
		this.fileList = fileList;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public void setFileList(String fileList) {
		this.fileList = fileList;
	}

	public String getFileList() {
		return fileList;
	}

}
