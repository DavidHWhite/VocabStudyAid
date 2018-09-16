package com.maygekyatt;

import org.ini4j.Ini;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
	    try {

		    Ini vocabList = new Ini(new File("list1.ini"));
		    Morph m1 = new Morph("Anthropo-", "human being", "anthropology", "the study of human culture", "philanthropic", "seeking to provide for the welfare of others");


	    } catch (IOException e) {
		    e.printStackTrace();
	    }
    }
}
