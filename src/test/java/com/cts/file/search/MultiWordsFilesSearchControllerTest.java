package com.cts.file.search;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cts.file.search.model.FilesSearchResult;
import com.cts.file.search.model.FilesSearchResultList;
import com.cts.file.search.service.MultiWordsFilesSearchService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MultiWordsFilesSearchControllerTest {

	@Value("${dir.filepath}")
	protected String dirPath;

	private static final Logger logger = Logger.getLogger(MultiWordsFilesSearchControllerTest.class);

	private MultiWordsFilesSearchService multiWordsFilesSearchService = new MultiWordsFilesSearchService();;

	String multiwordsParams= "word1 word2 word3 word4 word5";
	String[] multiwords = multiwordsParams.replaceAll("\\s{2,}", " ").trim().split(" ");

	//File fileSystem = new File(dirPath);

	File file = new File("I://temp//FileOne.txt");



	@Test
	public void testGetAllFilesFromFileSystem() {

		List<Path> filesList  = new ArrayList<Path>();

		try {

			filesList = multiWordsFilesSearchService.getAllFilesFromFileSystem(dirPath);

		} catch (Exception e) {

			logger.info("Exception occured during words search"+ e);
		}

		logger.info("filesList is"+ filesList.size());
	}


	@Test
	public void testIsFileContainsMultiWords() {

		Boolean fileContainsMultiWords  = true;


		try {

			fileContainsMultiWords = multiWordsFilesSearchService.isFileContainsMultiWords(file,multiwords);

		} catch (Exception e) {

			logger.info("Exception occured during words search"+ e);
		}

		logger.info("fileContainsMultiWords is"+ fileContainsMultiWords);
	}

	@Test
	public void tesIsFileContainsSingleWord() {

		Boolean fileContainsSingleWord  = true;

		try {

			fileContainsSingleWord = multiWordsFilesSearchService.isFileContainsSingleWord(file, "word1");

		} catch (Exception e) {

			logger.info("Exception occured during words search"+ e);
		}

		logger.info("fileContainsSingleWord is"+ fileContainsSingleWord);
	}

	@Test
	public void testsearchFilesWithMultiWords() {

		FilesSearchResultList<FilesSearchResult> matchdedFilesList  = new FilesSearchResultList<FilesSearchResult>();



		try {

			matchdedFilesList = multiWordsFilesSearchService.searchFilesWithMultiWords(multiwords, dirPath);

		} catch (Exception e) {

			logger.info("Exception occured during words search"+ e);
		}

		logger.info("matchdedFilesList is"+ matchdedFilesList.getMatchCount());
	}


}
