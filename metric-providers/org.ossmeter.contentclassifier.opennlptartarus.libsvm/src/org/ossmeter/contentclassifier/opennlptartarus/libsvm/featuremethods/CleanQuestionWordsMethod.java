package org.ossmeter.contentclassifier.opennlptartarus.libsvm.featuremethods;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ossmeter.contentclassifier.opennlptartarus.libsvm.ClassificationInstance;
import org.ossmeter.contentclassifier.opennlptartarus.libsvm.Classifier;

public class CleanQuestionWordsMethod {

	private static Set<String> questionWordList;
	private static String questionWordsFileName = "classifierFiles/questionWords";
	
	public static int predict(ClassificationInstance xmlResourceItem) {
		if (questionWordList==null) {
			questionWordList = loadSetFromFile(questionWordsFileName);
		}
		if (containsQuestionWords(xmlResourceItem))
			return 1;	//	"Request"
		return 0;	//	"Reply"
	}

	private static Boolean containsQuestionWords(ClassificationInstance xmlResourceItem) {
		for (String questionWord: questionWordList) {
			String regularExpressionString = "[^\\d\\w]" + questionWord + "[^\\d\\w]";
		    Pattern pattern = Pattern.compile(regularExpressionString);
		    Matcher matcher = pattern.matcher(xmlResourceItem.getCleanText().toLowerCase());
			if (matcher.find())
				return true;
		}
		return false;
	}

	private static Set<String> loadSetFromFile(String filename) {
		String path = (new Classifier()).getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
		if (path.endsWith("bin/"))
			path = path.substring(0, path.lastIndexOf("bin/"));
		HashSet<String> hashSet = new HashSet<String>();
		File file = new File(path, filename);
		String content = readFileAsString(file);
		for (String stopword : content.split("\\n")) {
			hashSet.add(stopword.trim());
		}
		return hashSet;
	}

	private static String readFileAsString(File afile) {
	    byte[] buffer = new byte[(int) afile.length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(afile));
	        f.read(buffer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { 
	    	if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);
	}

}
