package com.maygekyatt;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

class VocabList {
	/**
	 * Nested inner class Morph
	 * Used to store all data associated with an entry in the vocab list
	 */
	class Morph {
		String morph, morphDef, word1, word1Def, word2, word2Def;
		int score;

		String getWordDef(int word) {
			if (word == 1) {
				return word1Def;
			} else {
				return word2Def;
			}
		}

		/**
		 * Constructor
		 *
		 * @param morph    Morph to be defined
		 * @param morphDef Definition of morph
		 * @param word1    First word that uses the morph
		 * @param word1Def Definition of word1
		 * @param word2    Second word that uses the morph
		 * @param word2Def Definition of word2
		 */
		Morph(String morph, String morphDef, String word1, String word1Def, String word2, String word2Def, int score) {
			this.morph = morph;
			this.morphDef = morphDef;
			this.word1 = word1;
			this.word1Def = word1Def;
			this.word2 = word2;
			this.word2Def = word2Def;
			this.score = score;
		}
	}

	ArrayList<Morph> getMorphList() { return morphList; }

	private ArrayList<Morph> morphList = new ArrayList<>();
	private Ini listIni = new Ini();
	private File file;

	int getMaxTries() { return maxTries; }

	private int maxTries;

	Ini getListIni() { return listIni; }

	File getFile() { return file; }
	
	VocabList(String filePath) {
		try {
			file = new File(filePath);
			listIni.load(file);
			Set<String> sectionNameList = listIni.keySet();

			//region Get starting_perfect_tries_req and max_perfect_tries_req
			int startingPerfectTriesReq = -Integer.parseInt(listIni.fetch("preferences", "starting_perfect_tries_req"));
			maxTries = -Integer.parseInt(listIni.fetch("preferences", "max_perfect_tries_req"));
			//endregion

			//region Add all morphs with score < 0 to morphList
			for (String section : sectionNameList) {
				if (!section.equals("preferences")) {
					if (listIni.fetch(section, "score") == null) {
						listIni.put(section, "score", startingPerfectTriesReq);
						listIni.store(file);
					}

					if (Integer.parseInt(listIni.fetch(section, "score")) < 0) {
						morphList.add(new Morph(section, listIni.fetch(section, "morphdef"), listIni.fetch(section, "word1"), listIni.fetch(section, "word1def"), listIni.fetch(section, "word2"), listIni.fetch(section, "word2def"), Integer.parseInt(listIni.fetch(section, "score"))));
					}
				}
			}
			//endregion

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
