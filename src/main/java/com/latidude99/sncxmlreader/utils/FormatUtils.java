/**Copyright (C) 2017  Piotr Czapik.
 * @author Piotr Czapik
 * @version 2.3
 *
 *  This file is part of SncXmlReaderFXDB.
 *  SncXmlReaderFXDB is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  SncXmlReaderFXDB is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with SncXmlReaderFXDB.  If not, see <http://www.gnu.org/licenses/>
 *  or write to: latidude99@gmail.com
 */

package com.latidude99.sncxmlreader.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/*
 * Converts user input String from the search field into a Set<String>
 * Rules:
 *  - one Set entry = one chart number to be searched
 *  - individual chart numbers and ranges of numbers are separated by comma
 *  - all the whitespaces are removed
 *  - chart numbers may contain A-Z/a-z characters and, as they may be valid,
 * 		are not removed
 *  - a range of charts may be entered as min-max or max-min, both valid
 *  - a range of charts may include A-Z/a-z chars only in the first part
 *      (eg. nz343 - 356) or both (eg. aus146 - aus172). For the moment,
 *      a case of only the second part of the range having A-Z/a-z chars,
 *      is not supported.
 *
 *  The special characters for defining search type ('=', '+', '-') are
 *  removed from the input String before this class is used so any characters
 *  that accompany numbers are treated as part of charts number
 *  (eg. AUS345, DE54 but also $43 or =764).
 *  Searched chart numbers are displayed as a part of the search result
 *  making it easy to check if there were any typos in the search entered by a user
 */

public class FormatUtils {
	
	public static Set<String> parseInput(String input) {
        List<String> inputList = new ArrayList<>();
        Set<String> outputSet = new TreeSet<>();
                	        
        String[] numbers = input.split(",");
		for(String s: numbers) {
	        s= s.trim().replaceAll(" ", "").replaceAll("\u00A0", "");
        	inputList.add(s.toUpperCase());
        }
        for(String n: inputList) {
        	if(n.contains("-") && n.matches("[0-9]+")) {
        		String[] range = n.split("-");
        		if(range.length == 2){
					if(!range[0].equals("") && !range[1].equals("")) {
						int rangeMin = Integer.parseInt(range[0]);
						int rangeMax = Integer.parseInt(range[1]);
						if( rangeMin < rangeMax) {
							for(int i = rangeMin; i <rangeMax + 1; i++) {
								outputSet.add(i + "");
							}
						}else {
							for (int i = rangeMax; i < rangeMin + 1; i++) {
								outputSet.add(i + "");
							}
						}
					}else if(range[0].equals("")){
						outputSet.add(range[1]);
					}else if(range[1].equals("")) {
						outputSet.add(range[0]);
					}

				}else if(range.length == 1){
					outputSet.add(range[0]);
				}


			}else if(n.contains("-")){
				String lettersLeft = "";
				String lettersRight = "";
				String[] range = n.split("-");
				if(range.length == 2){
					if(!range[0].equals("") && !range[1].equals("")){
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
					}else if(range[0].equals("")){
						outputSet.add(range[1]);
					}else if(range[1].equals("")) {
						outputSet.add(range[0]);
					}

				}else if(range.length == 1){
					outputSet.add(range[0]);
				}


        	}else if(!n.equals("") && !n.equals(" ")){
        		outputSet.add(n);
        	}
		}
		return outputSet;
    }

	/*
	 * Formats displaying chart numbers.
	 */
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

	/*
	 * Used when saving search result (the TextArea content) to a file.
	 */
	public static List<String>  stringToList(String content){
		List<String> contentList = new ArrayList<>();
		String newline = System.getProperty("line.separator");
		boolean hasNewLine = content.contains(newline);
		if((content.trim().length() > 0) && (!hasNewLine)){
			String[] lines = content.split("\n");
			contentList = new ArrayList<>(Arrays.asList(lines));
		} else {
			MessageBox.show("Nothing to save yet.", "Input error");
		}
		return contentList;
	}
	
	
	


}






















