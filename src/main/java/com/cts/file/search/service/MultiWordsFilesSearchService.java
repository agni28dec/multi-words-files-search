package com.cts.file.search.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cts.file.search.util.FileSearchUtil;

@Component
public class MultiWordsFilesSearchService {

	private static final Logger logger = Logger.getLogger(MultiWordsFilesSearchService.class);


	/**
	 * Create set of all matched files where all words are present at least once.

	 * @param wordsToSearch

	 * @param directoryPath

	 * @return Set of all matched Files

	 * @throws IOException

	 */
	public Set<Path> searchFilesWithMultiWords(String wordsToSearch, String directoryPath) throws IOException {

		//** Start-Variable Initialization
		Boolean restWordsMatchFlag = true;

		List<Path> allFilesPathList = new ArrayList<Path>();

		List<Path> firtWordMatchFilesList = new ArrayList<Path>();

		Map<String,List<Path>> restWordsToPathListMap = new HashMap<String,List<Path>>();

		Set<Path> commonFileSet = new HashSet<Path>();

		logger.debug("wordsToSearch="+wordsToSearch+"directoryPath="+directoryPath);//Ends

		//**Start - Getting all files present under give file system
		Path path = Paths.get(directoryPath);
		Files.walk(path).filter(Files::isRegularFile).forEach(filePath -> allFilesPathList.add(filePath));

		logger.debug("allFilesPathList="+allFilesPathList);//Ends

		//**Start - Collecting words to Search
		wordsToSearch = FileSearchUtil.isNullOrBlank(wordsToSearch);

		String[] wordsList = wordsToSearch.trim().split("\\s+");

		String restWordsToSearch = wordsToSearch.substring(wordsToSearch.indexOf(' '),wordsToSearch.length());
		String[] restWordsList = restWordsToSearch.trim().split("\\s+");//Ends


		//** Start - Findings matched files with first word
		for (Path firstFilePath : allFilesPathList ) {

			Boolean firtWordMatchFlag = Files.lines(firstFilePath).anyMatch(match -> match.contains(wordsList[0]));

			if (firtWordMatchFlag == true) {

				logger.debug("firstFIlePath"+firstFilePath);
				firtWordMatchFilesList.add(firstFilePath);			
			}

		}//Ends

		//** Start- Findings matched files with remaining words
		for(String word:restWordsList) {

			List<Path> restWordMatchFilesList = new ArrayList<Path>();

			for (Path childFilePath : firtWordMatchFilesList ) {

				if(childFilePath.toFile().exists()) {

					restWordsMatchFlag = Files.lines(childFilePath).anyMatch(match -> match.contains(word));

					if(restWordsMatchFlag == true) {

						logger.debug("childFilePath"+childFilePath);
						restWordMatchFilesList.add(childFilePath);
					}
				}
			}
			if(restWordsMatchFlag == true) {
				logger.debug("placed in map"+restWordMatchFilesList + "for"+word);
				restWordsToPathListMap.put(word, restWordMatchFilesList);
			}
		}//Ends

		//** Start- Code to Iterate over map and identify common files among all matched files
		List<List<Path>> combinedListOfPathLists = new ArrayList<List<Path>>();
		combinedListOfPathLists.add(firtWordMatchFilesList);

		long perfectCount = restWordsToPathListMap.keySet().stream().distinct().count();
		logger.info("perfectCount : " +perfectCount);

		restWordsToPathListMap.forEach((k,v)->{

			logger.debug("Item : " + k + " Count : " + v);
			combinedListOfPathLists.add(v);

		});	//Ends

		if (perfectCount == (wordsList.length -1)) {

			commonFileSet = FileSearchUtil.getCommonElements(combinedListOfPathLists);
			logger.info("Common in all lists of file: " + FileSearchUtil.getCommonElements(combinedListOfPathLists));
		}
		return commonFileSet;
	}
}
