package com.cts.file.search.util;

import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


public class FileSearchUtil {

	private static final Logger logger = Logger.getLogger(FileSearchUtil.class);

	//**Start-Method to create single zip file for all matched files
	public URI zipFileFromMatchedFiles(Set<Path>matchedFilesPathSetArg) throws Throwable {

		logger.info("Creating Zip file");
		Map<String, String> env = new HashMap<>();
		env.put("create", "true");

		URI uri = URI.create("jar:file:/MatchedFiles.zip");

		for(Path matchedSetPath:matchedFilesPathSetArg) {

			try (FileSystem zipfs = FileSystems.newFileSystem(uri, env)) {

				Path externalTxtFile = Paths.get(matchedSetPath.toUri());
				Path pathInZipfile = zipfs.getPath(matchedSetPath.getFileName().toString());

				// copy a file into the zip file
				Files.copy( externalTxtFile,pathInZipfile,
						StandardCopyOption.REPLACE_EXISTING );
			}
		}
		logger.info("Zip file Created");
		return uri;
	}//Ends

	//** Start- Generic Method to identify common lists and return a Set
	public static <T> Set<T> getCommonElements(Collection<? extends Collection<T>> collections) {

		Set<T> common = new LinkedHashSet<T>();
		if (!collections.isEmpty()) {
			Iterator<? extends Collection<T>> iterator = collections.iterator();
			common.addAll(iterator.next());
			while (iterator.hasNext()) {
				common.retainAll(iterator.next());
			}
		}
		return common;
	}//End

	//**Start-String Null Check
	public static String isNullOrBlank(String wordSearch)
	{
		if(wordSearch != null && wordSearch.length() > 0){

			return wordSearch;
		}
		return "empty-string";
	}//Ends

}
