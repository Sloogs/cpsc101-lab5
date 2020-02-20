/**
* This file is part of a solution to
*     CPSC 101 Lab 5 Winter 2020
*
* This program...
*
* @author Nicholas Slugocki
* Student Number: 230082267
* @version 1
*/

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;


public class Main
{
	public static void main(String[] args)
			throws FileNotFoundException, UnsupportedEncodingException
	{
		try {
			int numConsonantsRequired = 0;
			String readFileString = "";
			String writeFileString = "";
			Scanner reader = new Scanner(System.in);
			PrintWriter writer = new PrintWriter(System.out);
			ArrayList<String> wordList = new ArrayList<String>();
			ArrayList<String> validWords = new ArrayList<String>();

			if (args.length == 3)
			{
				numConsonantsRequired = Integer.parseInt(args[0]);
				readFileString = args[1];
				writeFileString = args[2];
			}
			else if (args.length == 2)
			{
				numConsonantsRequired = Integer.parseInt(args[0]);
				readFileString = args[1];
			}
			else if (args.length == 1)
			{
				numConsonantsRequired = Integer.parseInt(args[0]);
			}
			else if (args.length == 0)
			{
				numConsonantsRequired = 6;
			}
			else
			{
				showUsage();
			}

			if (readFileString != "")
			{
				File readFile = new File(readFileString);
				reader = new Scanner(readFile, "UTF-8");
			} else {
				System.out.println("If no input was provided you may provide input below. "
						+ "Press enter when you are done.");
			}

			while (reader.hasNextLine())
			{
				String word = reader.nextLine();
				if (!word.equals("")) {
					wordList.add(word);
				} else {
					break;
				}
			}

			if (wordList.size() == 0)
			{
				showUsage();
			} else {
				if (writeFileString != "")
				{
					File writeFile = new File(writeFileString);
					writer = new PrintWriter(writeFile, "UTF-8");
				}

				for (String word: wordList)
				{
					if (numConstantsIsCorrect(word, numConsonantsRequired))
					{
						validWords.add(word);
					}
				}

				for (String word: validWords){
					if (writeFileString != "")
					{
						writer.println(word);
					} else {
						System.out.println(word);
					}
				}
			}

			reader.close();
			writer.flush();
			writer.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		catch (UnsupportedEncodingException e)
		{
			System.out.println("Error: " + e.getMessage());
		}
	}

	/**
	*
	* @param word
	*/
	public static boolean numConstantsIsCorrect(String word, int numberRequired)
	{
		boolean result = false;
		ArrayList<Character> consonants = new ArrayList<Character>();
		int pairCount = 0;
		int invalidCount = 0;

		// Remove accent marks to make comparing vowels easier.
		word = removeAccentMarks(word);

		// Remove all non-letters.
		word = word.replaceAll("[^a-zA-Z]+", "");

		// Adds valid letters to array. Stores them as lower case letters as
		// we do not wish for case to matter.
		for (int i = 0; i < word.length(); i++)
		{
			char letter = Character.toLowerCase(word.charAt(i));
			if (!(isVowel(letter)))
			{
				consonants.add(letter);
			}
		}

		// Compares pairs of letters in the "letters" ArrayList. Note that
		// letters were already converted to lower case in previous for loop
		// so we do not need to convert to lower case here.
		for (int i = 1; i < consonants.size(); i++)
		{
			// Casting to int gives the byte value of the letter so we can check if the letter
			// is a greater value than or equal value to the next letter, although likely
			// will only work with a "basic" (English) alphabet (no accents).
			if ((int)consonants.get(i - 1) >= (int)consonants.get(i))
			{
				pairCount++;
			}
			else if ((int)consonants.get(i - 1) < (int)consonants.get(i))
			{
				invalidCount++;
			}
		}

		if (pairCount >= numberRequired && !(invalidCount > 0))
		{
			result = true;
		} else {
			result = false;
		}

//		System.out.println("Word: " + word + ", num: " + pairCount
//				+ ", result: " + result);

		return result;
	}

	/**
	* 
	* @param letter
	* @return 
	*/
	public static boolean isVowel(char letter)
	{
		boolean result = false;
		if (letter == 'a' || letter == 'e' || letter == 'i' ||
			letter == 'o' || letter == 'u' || letter == 'y')
		{
			result = true;
		}

		return result;
	}

	public static String removeAccentMarks(String w)
	{
		String word = Normalizer.normalize(w, Normalizer.Form.NFD);
		word = word.replaceAll("\\p{M}", "");
		return word;
	}

	public static void showUsage()
	{
		System.out.println("Usage: java Main [n] [readfile] [writefile], where:");
		System.out.println("[n] is the number of consecutive non-increasing consonants "
				+ "(default = 6).");
		System.out.println("[readfile] is a text file to read input from (otherwise will "
				+ " take input piped via command line).");
		System.out.println("[writefile] is a text file to read input from (will output to "
				+ "System.out by default if no file name is supplied).");
	}
}