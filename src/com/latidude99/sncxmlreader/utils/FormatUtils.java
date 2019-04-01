package com.latidude99.sncxmlreader.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FormatUtils {
	
	public static Set<String> parseInput(String input) {
        List<String> inputList = new ArrayList<>();
        Set<String> outputSet = new TreeSet<>();
                	        
        String[] numbers = input.split(",");
		for(String s: numbers) {
	        s= s.trim().replaceAll(" ", "").replaceAll("\u00A0", "");
        	inputList.add(s);
        }
        for(String n: inputList) {
        	if(n.contains("-") && n.matches("[0-9]+")) {
        		String[] range = n.split("-");
        		int rangeMin = Integer.parseInt(range[0]);
        		int rangeMax = Integer.parseInt(range[1]);
        		if( rangeMin < rangeMax) {
        			for(int i = rangeMin; i <rangeMax + 1; i++) {
        				outputSet.add(i + "");
        			}
        		}else {
        			for(int i = rangeMax; i < rangeMin + 1; i++) {
            			outputSet.add(i + "");
        			}
        		}
			}else if(n.contains("-")){
				String lettersLeft = "";
				String lettersRight = "";
				String[] range = n.split("-");
				for (int i=0; i<range[0].length(); i++){
					if(!range[0].substring(i, i+1).matches("[0-9]+"))
						lettersLeft = lettersLeft + range[0].charAt(i);
				}
				for (int i=0; i<range[1].length(); i++){
					if(!range[1].substring(i, i+1).matches("[0-9]+"))
						lettersRight = lettersRight + range[0].charAt(i);
				}
				if(lettersLeft.equals(lettersRight)){
					int rangeMin = Integer.parseInt(range[0].substring(lettersLeft.length()));
					int rangeMax = Integer.parseInt(range[1].substring(lettersRight.length()));
					if( rangeMin < rangeMax) {
						for(int i = rangeMin; i <rangeMax + 1; i++) {
							outputSet.add(lettersLeft.toUpperCase() + i);
						}
					}else {
						for(int i = rangeMax; i < rangeMin + 1; i++) {
							outputSet.add(lettersLeft.toUpperCase() + i);
						}
        			}
				}else if(!lettersLeft.equals(lettersRight) && lettersRight.equals("")){
					int rangeMin = Integer.parseInt(range[0].substring(lettersLeft.length()));
					int rangeMax = Integer.parseInt(range[1].substring(lettersRight.length()));
					if( rangeMin < rangeMax) {
						for(int i = rangeMin; i <rangeMax + 1; i++) {
							outputSet.add(lettersLeft.toUpperCase() + i);
						}
					}else {
						for(int i = rangeMax; i < rangeMin + 1; i++) {
							outputSet.add(lettersLeft.toUpperCase() + i);
						}
        			}
				}
        	}else {
        		outputSet.add(n);
        	}
		}
		return outputSet;
    }
	

	public static String printList20Cols(List<String> list) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for(String s : list) {
			sb.append(s + "  ");
			count++;
			if(count == 5 || count == 10 || count ==15) {
				sb.append("   ");
			}
			if(count == 20) {
				sb.append("\n");
				count = 0;
			}
		}
		return sb.toString();
	}
	
	public static String printSet20Cols(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for(String s : set) {
			sb.append(s + "  ");
			count++;
			if(count == 5 || count == 10 || count ==15) {
				sb.append("   ");
			}
			if(count == 20) {
				sb.append("\n");
				count = 0;
			}
		}
		return sb.toString();
	}
	
	public static List<String>  stringToList(String content){
		List<String> contentList = new ArrayList<>();
		String newline = System.getProperty("line.separator");
		boolean hasNewLine = content.contains(newline);
		if((content.trim().length() > 0) && (!hasNewLine)){
			String[] lines = content.split("\n");
			contentList = new ArrayList<String>(Arrays.asList(lines));
		} else {
			MessageBox.show("The text area is empty!", "Input error");
		}
		return contentList;
	}
	
	
	


}






















