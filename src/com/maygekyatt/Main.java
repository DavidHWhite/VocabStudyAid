package com.maygekyatt;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		//Create scanner looking for text input from user
		Scanner scan = new Scanner(System.in);

		String s;
		if (args.length == 0) {
			System.out.println("What is the name of the list you'd like to use?");
			s = scan.nextLine();
		} else
			s = args[0];

		VocabList vocabList = new VocabList(s + ".ini");

		while (vocabList.getMorphList().size() != 0) {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			quiz(vocabList, scan, vocabList.getMaxTries());
		}
	}


	private static void quiz(@NotNull VocabList vocabList, @NotNull Scanner scan, int maxTries) {
		//region Choose a random morph
		Random rand = new Random();
		VocabList.Morph quizSubject;
		ArrayList<VocabList.Morph> morphs = vocabList.getMorphList();
		quizSubject = morphs.get(rand.nextInt(morphs.size()));
		boolean isEverythingPerfect = true;
		boolean isThisCorrect;
		//endregion

		//region Ask for and check definition of the morph
		do {
			System.out.println("What is the definition of \"" + quizSubject.morph + "\"?");
			String in = scan.nextLine().toLowerCase();
			if (in.equals(quizSubject.morphDef.toLowerCase())) {
				System.out.print("Correct! ");
				isThisCorrect = true;
			} else {
				System.out.println("Unfortunately, the answer we were looking for was \"" + quizSubject.morphDef + "\". Please try again.");
				changeScore(isEverythingPerfect, quizSubject, vocabList, maxTries);
				isEverythingPerfect = false;
				isThisCorrect = false;
			}
		} while (!isThisCorrect);
		//endregion

		//region Ask for and check each of the 2 words and their definitions
		int prevWord = 0;
		for (int goTwice = -2; goTwice < 0; goTwice++) {
			//region Ask for and check one word
			do {
				if (goTwice == -2) System.out.println("What is one of the words that uses this morph?");
				else System.out.println("What is the other word that uses this morph?");

				String in = scan.nextLine().toLowerCase();
				if (in.equals(quizSubject.word1.toLowerCase()) && prevWord != 1) {
					isThisCorrect = true;
					prevWord = 1;
				} else if (in.equals(quizSubject.word2.toLowerCase()) && prevWord != 2) {
					isThisCorrect = true;
					prevWord = 2;
				} else {
					System.out.println("Unfortunately, that answer was incorrect. Make sure that you remember both \"" + quizSubject.word1 + "\" or \"" + quizSubject.word2 + "\". Please try again.");
					changeScore(isEverythingPerfect, quizSubject, vocabList, maxTries);
					isEverythingPerfect = false;
					isThisCorrect = false;
				}
			} while (!isThisCorrect);
			//endregion

			//region Ask for and check the word's definition
			System.out.print("Correct!");
			System.out.print(" Do you know this word's definition? Press enter once you've thought of it.");
			scan.nextLine();

			System.out.print("The definition is \"" + quizSubject.getWordDef(prevWord) + "\". Did you get it right? (y/n) ");
			String in = scan.nextLine();
			if (in.toLowerCase().equals("y")) {
				System.out.println("Great job!");
			} else {
				System.out.println("Make sure you remember next time!");
				changeScore(isEverythingPerfect, quizSubject, vocabList, maxTries);
				isEverythingPerfect = false;
			}
			//endregion
		}
		//endregion

		//region If no mistakes were made, increase score by 1
		if (isEverythingPerfect) {
			quizSubject.score++;
			vocabList.getListIni().put("score", quizSubject.morph, quizSubject.score);
			try {
				vocabList.getListIni().store(vocabList.getFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//endregion

		//region If score has reached 0, remove from morphList
		if (quizSubject.score >= 0)
			morphs.remove(quizSubject);
		//endregion

		System.out.println("Press enter to continue...");
		scan.nextLine();
	}

	private static void changeScore(boolean isCorrect, VocabList.Morph quizSubject, VocabList vocabList, int maxTries) {
		try {
			if (isCorrect) {
				quizSubject.score = Math.max(maxTries, --quizSubject.score);
				vocabList.getListIni().put("score", quizSubject.morph, quizSubject.score);
				vocabList.getListIni().store(vocabList.getFile());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
