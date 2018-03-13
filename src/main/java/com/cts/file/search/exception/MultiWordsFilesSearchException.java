package com.cts.file.search.exception;

public class MultiWordsFilesSearchException extends Exception {


	private static final long	serialVersionUID	= 7267672899607797813L;

	/**
	 * Default constructor
	 */
	public MultiWordsFilesSearchException() {
		super();
	}

	/**
	 * Creates an instance of XDomainException
	 * 
	 * @param errorMessage The error message to be displayed
	 */
	public MultiWordsFilesSearchException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Creates an instance of XDomainException with error message and throwable as input
	 * 
	 * @param errorMessage The error message to be displayed
	 * 
	 * @param exception The throwable object
	 */
	public MultiWordsFilesSearchException(String errorMessage, Throwable exception) {
		super(errorMessage, exception);
	}



}
