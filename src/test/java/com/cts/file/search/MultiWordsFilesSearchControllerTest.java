package com.cts.file.search;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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


	@Test
	public void testSearchFilesWithMultiWords() {

		Set<Path> combinedFilesList = new HashSet<Path>();

		try {

			combinedFilesList = multiWordsFilesSearchService.searchFilesWithMultiWords("word1 word2 word3 word4 word5", dirPath);

		} catch (IOException e) {

			logger.info("Exception occured during words search"+ e);
		}

		logger.info("combinedList is"+ combinedFilesList.size());
	}


}
