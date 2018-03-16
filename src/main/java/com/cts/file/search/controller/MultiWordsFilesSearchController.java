package com.cts.file.search.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.file.search.exception.MultiWordsFilesSearchException;
import com.cts.file.search.model.FilesSearchResult;
import com.cts.file.search.model.FilesSearchResultList;
import com.cts.file.search.model.SearchValidation;
import com.cts.file.search.service.MultiWordsFilesSearchService;

@RestController
@Api(value = "Multi-Words-Search")
public class MultiWordsFilesSearchController
{
	private static final Logger logger = Logger.getLogger(MultiWordsFilesSearchController.class);

	@Value("${dir.filepath}")
	protected String dirPath;

	@Autowired
	@Qualifier("multiWordsFilesSearchService")
	private MultiWordsFilesSearchService multiWordsFilesSearchService;


	@SuppressWarnings("rawtypes")
	@CrossOrigin
	@ApiOperation(value = "Search words in the files and return the matched files", response = FilesSearchResultList.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "List of files (File Name and File Location) matched", response = FilesSearchResultList.class),
			@ApiResponse(code = 500, message = "Error Fetching", response = MultiWordsFilesSearchException.class),
			@ApiResponse(code = 400, message = "Validation Failed", response = SearchValidation.class) })

	/**
	 * Create set of all matched files where all words are present at least once.

	 * @param multiwords entered by client of rest service

	 * @return Count and Path of matched files

	 * @throws IOException

	 */
	@RequestMapping(value = "/files_search",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})

	public ResponseEntity<?> searchFilesWithMultiWords(@ApiParam(value = "Please enter words delimited by space with each other!!", required = false) 
	@RequestParam(value = "multiwords", required = false) String multiwordsParams,
	@ApiParam(value = "Please select option for case sensitivity. Allowed values are Y or y for yes and N or n for No", required = false, 
	defaultValue = "Y", allowableValues = "Y,N") @RequestParam(value = "casesensitive", required = false) String caseSensitiveSearch) throws Exception
	{
		logger.info("multiwords for files search"+ multiwordsParams);


		if (StringUtils.isBlank(multiwordsParams))
			return new ResponseEntity<SearchValidation>(
					new SearchValidation("Please enter one word at least !!"),
					HttpStatus.BAD_REQUEST);

		//Setting additional preferences for search
		if (!StringUtils.isEmpty(caseSensitiveSearch) && (caseSensitiveSearch.trim().equalsIgnoreCase("Y")
				|| caseSensitiveSearch.trim().equalsIgnoreCase("N")))
			multiWordsFilesSearchService.setCaseSensitiveSearch(caseSensitiveSearch.trim().equalsIgnoreCase("Y") ? true : false);


		String[] multiwords = multiwordsParams.replaceAll("\\s{2,}", " ").trim().split(" ");

		FilesSearchResultList<FilesSearchResult> matchdedFilesList ;

		try {

			matchdedFilesList = multiWordsFilesSearchService.searchFilesWithMultiWords(multiwords,dirPath);
			return new ResponseEntity<FilesSearchResultList>(matchdedFilesList, HttpStatus.OK);

		} catch (Exception ex) {

			return new ResponseEntity<MultiWordsFilesSearchException>(new MultiWordsFilesSearchException("", ex), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}