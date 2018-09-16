package com.maygekyatt;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

public class VocabList {
	Ini listIni = new Ini();

	public VocabList(String filePath) {
		try {

			listIni = new Ini(new File(filePath));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
