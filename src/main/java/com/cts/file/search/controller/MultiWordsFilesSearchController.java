package com.cts.file.search.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cts.file.search.model.FilesSearchList;
import com.cts.file.search.service.MultiWordsFilesSearchService;
import com.cts.file.search.util.FileSearchUtil;

@RestController
public class MultiWordsFilesSearchController
{
	private static final Logger logger = Logger.getLogger(MultiWordsFilesSearchController.class);

	FileSearchUtil fileSearchUtil = new FileSearchUtil();

	@Value("${dir.filepath}")
	protected String dirPath;

	@Autowired
	private MultiWordsFilesSearchService multiWordsFilesSearchService;

	/**
	 * Create set of all matched files where all words are present at least once.

	 * @param multiwords

	 * @return List of all matched Files and combined zip file location

	 * @throws IOException

	 */
	@RequestMapping("/files_search")
	public FilesSearchList searchFilesWithMultiWords(@RequestParam(value="multiwords") String multiwords) throws IOException
	{
		logger.info("multiwords for files search"+ multiwords);
		String userMessage = "All Matched files are placed in a zip file @  " + dirPath.toString();
		Set<Path> matchedFilesPathSet = new HashSet<Path>();

		try {
			matchedFilesPathSet = multiWordsFilesSearchService.searchFilesWithMultiWords(multiwords,dirPath);			
		} catch (Exception e) {
			logger.info("Exception occured during files search"+e);
		}
		logger.debug("MatchedFilesPath Set"+ matchedFilesPathSet);

		if(matchedFilesPathSet.size()==0) {
			
			return new FilesSearchList("There is no file with multiple words / search w/o words",matchedFilesPathSet.toString());

		}else {
			try {			
				fileSearchUtil.zipFileFromMatchedFiles(matchedFilesPathSet);			
			} catch (Throwable e) {
				logger.info("Exception occured during zip creation"+e);
			}
		}
		return new FilesSearchList(userMessage,matchedFilesPathSet.toString());
	}

}