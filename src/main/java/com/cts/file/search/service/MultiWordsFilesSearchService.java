package com.cts.file.search.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cts.file.search.model.FilesSearchResult;
import com.cts.file.search.model.FilesSearchResultList;

@Component
@Service("multiWordsFilesSearchService")
public class MultiWordsFilesSearchService {

	private static final Logger logger = Logger.getLogger(MultiWordsFilesSearchService.class);

	@Value("${case-sensitive}")
	private boolean caseSensitiveSearch;

	/**
	 * Set the value for case sensitive search
	 * 
	 * @param caseSensitiveSearch
	 *            True if the search is case sensitive
	 */
	public void setCaseSensitiveSearch(boolean caseSensitiveSearch) {
		this.caseSensitiveSearch = caseSensitiveSearch;
	}
	/**
	 * It returns the files which contain the multiple words entered by client
	 * 
	 * @param wordsToMatch
	 *            words to match
	 * @return Search Result containing number of files matched with details of
	 *         the file like name and path
	 * @throws Exception
	 *             Throw an exception which is handled in the controller to show
	 *             appropriate message.
	 */
	public FilesSearchResultList<FilesSearchResult> searchFilesWithMultiWords(String[] wordsToMatch, String directoryPath ) throws Exception {

		//parallel search of words using fork join pool with multiple processors

		ForkJoinPool forkJoinPoolForMultiWordsSearch = new ForkJoinPool(Runtime.getRuntime().availableProcessors() * 1000);
		FilesSearchResultList<FilesSearchResult> searchResults = new FilesSearchResultList<FilesSearchResult>();
		try {
			List<Path> allFilesToSearch = getAllFilesFromFileSystem(directoryPath);
			List<FilesSearchResult> matchedFiles = forkJoinPoolForMultiWordsSearch
					.submit(() -> allFilesToSearch.parallelStream()
							.filter(file -> isFileContainsMultiWords(file.toFile(), wordsToMatch)).map(matchedFile -> {
								FilesSearchResult fileResult = new FilesSearchResult();
								fileResult.setFileName(matchedFile.toFile().getName());
								fileResult.setFilePath(matchedFile.toString());
								return fileResult;
							}).collect(Collectors.toList()))
							.get();
			searchResults.setMatchedFiles(matchedFiles);

			searchResults.setMatchCount(searchResults.getMatchedFiles().size());
			return searchResults;

		} catch (Exception e) {

			logger.error(ExceptionUtils.getStackFrames(e));
			throw e;
		}
	}

	/**
	 * This method searches a single file for the words which were searched for
	 * 
	 * @param fileToConsider
	 *            File in which search will take place
	 * @param wordsToSearch
	 *            Words which will be searched
	 * @return true or false
	 */
	public boolean isFileContainsMultiWords(File fileToConsider, String[] wordsToSearch) {
		boolean result = Arrays.stream(wordsToSearch).parallel().filter(p -> isFileContainsSingleWord(fileToConsider, p))
				.count() == wordsToSearch.length;
		if (!result)
			logger.info("The file did not matched" + fileToConsider.getName());
		return result;
	}

	/**
	 * This method looks for a word in the file supplied
	 * 
	 * @param fileToConsider
	 *            File in which search will take place
	 * @param wordToMatch
	 *            Word to search
	 * @return Whether the word is found or not
	 */
	public boolean isFileContainsSingleWord(File fileToConsider, String wordToMatch) {
		try {
			// Search within a text file
			//All the lines are searched in parallel to find a match

			if (FilenameUtils.getExtension(fileToConsider.getName()).equalsIgnoreCase("txt"))
				return Files.readAllLines(fileToConsider.toPath()).parallelStream().anyMatch(line -> {
					if (caseSensitiveSearch)
						return (line.indexOf(wordToMatch) != -1);
					else
						return line.toLowerCase().indexOf(wordToMatch.toLowerCase()) != -1;
				});
			else if (FilenameUtils.getExtension(fileToConsider.getName()).equalsIgnoreCase("pdf")) {
				logger.info("Implementation yet to done for pdf file");
				return false;
			}
		} catch (IOException e) {

			logger.error("Processsing of file stopped abruptly--" + fileToConsider.getName()+ ". It is because of --");
			logger.error(ExceptionUtils.getStackFrames(e));
		}
		return false;
	}

	/**
	 * This method returns all the files under a directory
	 * 
	 * @param directory
	 *            Directory to search
	 * @return All the files under that directory (depends upon a parameter
	 *         whether the search will be recursive or at that directory level
	 *         only
	 */
	public List<Path> getAllFilesFromFileSystem(String pathOfdirectory) {
		final List<Path> files = new ArrayList<>();
		Path path = Paths.get(pathOfdirectory);

		try {

			Files.walk(path).filter(Files::isRegularFile).forEach(filePath -> files.add(filePath)); // it would cater recursive or non-recursive both

		} catch (IOException e) {
			logger.error("Skipped processsing of the directory--" + path.getFileSystem()+ ". It is failing due to --");
			logger.error(ExceptionUtils.getStackFrames(e));
		}

		logger.debug("allFilesPathList="+files);
		return files;
	}
}

