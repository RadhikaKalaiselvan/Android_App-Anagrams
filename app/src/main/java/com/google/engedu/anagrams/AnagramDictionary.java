package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {


    private static int max_word_length = 7;
    private static final int MIN_NUM_ANAGRAMS=3;
    private Random random = new Random();
    private ArrayList<String> wordList=new ArrayList<String>();
    private HashSet<String> wordSet=new HashSet<String>();
    private static int defaultWordLength=4;
    private HashMap<String,ArrayList<String>> letterToWord=new HashMap<String,ArrayList<String>>();
    private HashMap<Integer,ArrayList<String>> sizeToWords=new HashMap<Integer,ArrayList<String>>();
    public AnagramDictionary(InputStream wordListStream) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while ((line = in.readLine()) != null) {
            String word = line.trim();
            int wordLength=word.length();
            if (wordLength> 1) {
                wordList.add(word);
                wordSet.add(word);
                if(sizeToWords.containsKey(wordLength)){
                    ArrayList oldlist=sizeToWords.get(wordLength);
                    oldlist.add(word);
                    sizeToWords.put(wordLength,oldlist);
                } else {
                    ArrayList<String> newList=new ArrayList<String>();
                    newList.add(word);
                    sizeToWords.put(wordLength,newList);
                }
                String sortedWord = sortLetters(word);
                if (letterToWord.containsKey(sortedWord)) {
                    ArrayList list = letterToWord.get(sortedWord);
                    list.add(word);
                    letterToWord.put(sortedWord, list);
                } else {
                    ArrayList<String> wordArrayList = new ArrayList<String>();
                    wordArrayList.add(word);
                    letterToWord.put(sortedWord, wordArrayList);
                }
            }
        }
        max_word_length=sizeToWords.size();
    }

    public boolean isGoodWord(String word, String base) {
        boolean goodWord=false;
        if(wordSet.contains(word)){
            if(!base.contains(word)){
              goodWord=true;
            }
        }
        return goodWord;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortedTargetString=sortLetters(targetWord);
        for( String currentWord : wordList)
        {
            if(currentWord.length()==sortedTargetString.length()) {
            String sortedCurrentWord=sortLetters(currentWord);
                if(!currentWord.equals(targetWord) && (sortedTargetString.equals(sortedCurrentWord))){
                    result.add(currentWord);

                }
            }
        }
        return result;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        List<String> anagramsList=letterToWord.get(sortLetters(word));
        for ( String anagram : anagramsList )
        {

            for (char ch = 'a' ; ch <= 'z' ; ch++ )
            {
                String anagramWithOneMoreLetter=ch+""+anagram;
              if(wordSet.contains(anagramWithOneMoreLetter) && !result.contains(anagramWithOneMoreLetter) && !anagramWithOneMoreLetter.contains(word)){
                  result.add(anagramWithOneMoreLetter);
              }
              anagramWithOneMoreLetter=anagram+""+ch;
                if (wordSet.contains(anagramWithOneMoreLetter) && !result.contains(anagramWithOneMoreLetter) && !anagramWithOneMoreLetter.contains(word)){
                    result.add(anagramWithOneMoreLetter);
              }
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {
        Random r = new Random();
        String word="stop";
        Log.d(" DEfault len",defaultWordLength+" max word size "+max_word_length);
        int i= r.nextInt( wordList.size()- 1) + 2;
        while (i<wordList.size())
        {
            word=wordList.get(i);
            if(word.length()==defaultWordLength) {
                ArrayList<String> temp = letterToWord.get(sortLetters(word));
                if (temp != null && temp.size() >= MIN_NUM_ANAGRAMS) {
                    if(defaultWordLength<max_word_length) {
                        defaultWordLength++;
                    } else {
                      defaultWordLength=4;
                    }
                    break;
                }
            }
            Log.d("Current word size",i+"");
            if(i==wordList.size()){
                i=0;
            }else {
                i++;
            }
        }
        return word;
    }

    public String sortLetters(String originalString) {
        char[] chars = originalString.toCharArray();
        java.util.Arrays.sort(chars);

        return new String(chars);
    }
}
