/*
 * Please see submission instructions for what to write here. 
 */

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SentAnalysis {

	final static File TRAINFOLDER = new File("train");
	public static HashMap<String, Integer> Positive = new HashMap<String,Integer>();
	public static HashMap<String, Integer> Negative = new HashMap<String,Integer>();
		
	public static void main(String[] args) throws IOException, FileNotFoundException
	{	
		ArrayList<String> files = readFiles(TRAINFOLDER);		
		
		train(files);
		//if command line argument is "evaluate", runs evaluation mode
		if (args.length==1 && args[0].equals("evaluate")){
			evaluate();
		}
		else{//otherwise, runs interactive mode
			@SuppressWarnings("resource")
			Scanner scan = new Scanner(System.in);
			System.out.print("Text to classify>> ");
			String textToClassify = scan.nextLine();
			System.out.println("Result: "+classify(textToClassify));
		}
		
	}
	

	
	/*
	 * Takes as parameter the name of a folder and returns a list of filenames (Strings) 
	 * in the folder.
	 */
	public static ArrayList<String> readFiles(File folder){
		
		System.out.println("Populating list of files");
		
		//List to store filenames in folder
		ArrayList<String> filelist = new ArrayList<String>();
		
	
		for (File fileEntry : folder.listFiles()) {
	        String filename = fileEntry.getName();
	        filelist.add(filename);
		}		
		
		
		return filelist;
	}

	public static ArrayList<String> readText(String fileName) throws FileNotFoundException {
				
		//List to store text
		ArrayList<String> text = new ArrayList<String>();
		

		Scanner fileReader = new Scanner(new File(fileName));

		while (fileReader.hasNextLine()) {
			String[] line = (fileReader.nextLine()).split(" ");
			//System.out.println(line);

			for (String word : line) {
				text.add(word);
			}		
		}

		fileReader.close();
		
		return text;
	}
	
	

	
	/*
	 * TO DO
	 * Trainer: Reads text from data files in folder datafolder and stores counts 
	 * to be used to compute probabilities for the Bayesian formula.
	 * You may modify the method header (return type, parameters) as you see fit.
	 */
	public static void train(ArrayList<String> files) throws FileNotFoundException {


		Positive.put("count", 0);
		Negative.put("count", 0);

		for (int i = 0; i < files.size(); i++) {
			String file = files.get(i);
			int index = file.indexOf("-");

			ArrayList<String> words = readText("train/"+file);
			//t.println(words);

			if (file.charAt(index+1) == '5' || file.charAt(index+1) == '4' || file.charAt(index+1) == '3') {
				for (int j = 0; j < words.size(); j++) {
					String cleanWord = words.get(j).replace(",","").replace(";", "").replace(".","").replace("/","").replace(":","").replace("(","").replace(")","");
					if (Positive.containsKey(cleanWord)) {
						Positive.replace(cleanWord, Positive.get(cleanWord)+1);
					} else {
						Positive.put(cleanWord, 1);
					}
				}
				Positive.replace("count", Positive.get("count")+1);
			} else {
				for (int j = 0; j < words.size(); j++) {
					String cleanWord = words.get(j).replace(",","").replace(";", "").replace(".","").replace("/","").replace(":","").replace("(","").replace(")","").replace("'","");
					if (Negative.containsKey(cleanWord)) {
						Negative.replace(cleanWord, Negative.get(cleanWord)+1);
					} else {
						Negative.put(cleanWord, 1);
					}
				}
				Negative.replace("count", Negative.get("count")+1);
			}
		}
		
		//System.out.println("print");
		System.out.println(Positive);
	}


	/*
	 * Classifier: Classifies the input text (type: String) as positive or negative
	 */
	public static double classify(String text) throws FileNotFoundException {
		
		ArrayList<String> words = readText("test/"+text);
		double posCount = (Positive.get("count"));
		double negCount = (Negative.get("count"));
		double posSize = Positive.size();
		double bayesProb = posCount/(posCount+negCount);
		//System.out.println(bayesProb);
		for(int i = 0; i < words.size(); i++){
			String word = words.get(i);
			if(Positive.containsKey(word)){
				double freq = Positive.get(word);
				System.out.println(bayesProb);
				bayesProb = bayesProb + log2(freq/posSize);

			}
			

		}

		//String result="";
		System.out.println(text+bayesProb);
		return bayesProb;
		
	}


	/**	
	 * Computes log_2(d) (To be used by the getEntropy() method)
	 * @param d - value
	 * @return log_2(d)
	 */
	public static double log2(double d){
		if(d==0){
			return 0;
		} else{
			return Math.log(d)/Math.log(2);
		}
	}
	
	
	

	/*
	 * TO DO
	 * Classifier: Classifies all of the files in the input folder (type: File) as positive or negative
	 * You may modify the method header (return type, parameters) as you like.
	 */
	public static void evaluate() throws FileNotFoundException 
	{
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Enter folder name of files to classify: ");
		String foldername = scan.nextLine();
		File folder = new File(foldername);
		
		ArrayList<String> filesToClassify = readFiles(folder);
		
		for (int i = 0; i<filesToClassify.size(); i++){

			System.out.println(filesToClassify.get(i)+" " + classify(filesToClassify.get(i)));


		}
		
	}
	
	
	
}
