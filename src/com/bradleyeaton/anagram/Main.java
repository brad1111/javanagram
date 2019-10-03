package com.bradleyeaton.anagram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Main {

    public static void main(String[] args) {
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
	    System.out.println("What word/phrase do you want an anagram for?"); //Output to user

        try {
            String stringToAnagram = inputReader.readLine(); //Get user input
            stringToAnagram = stringToAnagram.trim().replace(" ", "+");

            URL anagramURL = new URL("https://new.wordsmith.org/anagram/anagram.cgi?anagram=" + stringToAnagram);
            URLConnection httpConnection = anagramURL.openConnection();

            BufferedReader httpReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

            try{
                String currentLine = null;
                int noOfLinesLeft = -1;
                boolean containsLines = false;
                do { //Go through each line until you find the one containing 'x found. Displaying all:' or if it doesnt find that there are none to list
                     //And then continue with x + 1 amount of lines to get all of the possible anagrams
                    containsLines = true;
                    currentLine = httpReader.readLine();
                    if(currentLine.contains("found. Displaying all:")){
                        currentLine = currentLine.replaceAll("<script>(\\w||\\.||'||=||;)*<\\/script>",""); //Remove the script tag in front
                        currentLine = currentLine.replaceAll("<\\w>","");//Remove any other tags
                        String noString = currentLine.substring(0, currentLine.length() - 23); //Get only the number part
                        noOfLinesLeft = Integer.parseInt(noString) + 1; //Parse into int
                    }
                    if(noOfLinesLeft > 0){

                        currentLine =currentLine.replaceAll("<\\/?(\\w)+>","");//Remove any tags
                        System.out.println(currentLine);
                        noOfLinesLeft--;
                    }
                } while (currentLine != null && noOfLinesLeft != 0);


                if(!containsLines){
                    System.out.println("No anagrams found");
                }
            }
            finally {
                httpReader.close();
            }
        }
        catch (Exception ex){
            System.out.println("Possible issue with word/phrase, couldn't get an anagram");
            ex.printStackTrace();
        }

    }
}
