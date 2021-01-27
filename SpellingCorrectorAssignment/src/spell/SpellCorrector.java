package spell;

import java.io.*;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {

    private Trie trie;

    public SpellCorrector() {}

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {

        //Add each word from the dictionary to the Trie
        String temp;
        trie = new Trie();
        File file = new File(dictionaryFileName);
        Scanner in = new Scanner(file);

        while (in.hasNext()){

            temp = in.next();
            trie.add(temp);
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {

        inputWord = inputWord.toLowerCase();

        if (inputWord.equals("")){

            return null;
        }

        Set<String> editDistanceOneWords = new TreeSet<>();
        Set<String> editDistanceTwoWords = new TreeSet<>();
        Vector<stringObjects> editDistanceOneWords_Vec = new Vector<>();
        Vector<stringObjects> editDistanceTwoWords_Vec = new Vector<>();
        stringObjects similarWord = null;

        if (trie.find(inputWord) != null){

            return inputWord;
        }
        else {

            //Edit distance one
            editDistanceOneWords.addAll(editDistanceOneCalc(inputWord));
            editDistanceOneWords_Vec.addAll(createStringObjects(editDistanceOneWords));
            removeWordsNotInTrie(editDistanceOneWords_Vec);

            //There are no edit distance one suggestions, go on to edit distance 2
            if (editDistanceOneWords_Vec.size() == 0){

                //Compute edit distance two words
                editDistanceTwoWords.addAll( findEditDistanceTwoWords(editDistanceOneWords, editDistanceTwoWords) );
                editDistanceTwoWords_Vec.addAll(createStringObjects(editDistanceTwoWords));
                removeWordsNotInTrie(editDistanceTwoWords_Vec);

                if (editDistanceTwoWords_Vec.size() == 0){

                    return null;
                }

                else {

                    similarWord = findMax(editDistanceTwoWords_Vec);
                }
            }

            else {

                similarWord = findMax(editDistanceOneWords_Vec);
            }
        }

        return similarWord.getString();
    }

    private Set<String> editDistanceOneCalc(String inputWord){

        Set<String> words = new TreeSet<>();
        StringBuilder temp = new StringBuilder();
        char tempChar;
        String tempString;
        char alphaChar;

        //Deletion
        for (int i = 0; i < inputWord.length(); ++i){

            temp.setLength(0);
            temp.append(inputWord);
            temp.deleteCharAt(i);
            words.add(temp.toString());
        }

        //Transposition
        for (int i = 0; i < inputWord.length(); ++i){

            temp.setLength(0);
            temp.append(inputWord);

            if (i < inputWord.length() - 1){

                tempChar = temp.charAt(i + 1);
                temp.replace(i + 1, i + 2, String.valueOf(temp.charAt(i)));
                tempString = Character.toString(tempChar);
                temp.replace(i, i + 1, tempString);
                words.add(temp.toString());
            }
        }

        //Alteration
        for (int i = 0; i < inputWord.length(); ++i){

            temp.setLength(0);
            temp.append(inputWord);

            for (int j = 0; j < 26; ++j){

                temp.setLength(0);
                temp.append(inputWord);
                alphaChar = (char) ('a' + j);

                if (temp.charAt(i) != alphaChar){

                    temp.replace(i, i + 1, Character.toString(alphaChar));
                    words.add(temp.toString());
                }
            }
        }

        //Insertion
        for (int i = 0; i <= inputWord.length(); ++i){

            temp.setLength(0);
            temp.append(inputWord);

            if (i < inputWord.length()) {

                for (int j = 0; j < 26; ++j) {

                    temp.setLength(0);
                    temp.append(inputWord);
                    alphaChar = (char) ('a' + j);

                    temp.insert(i, alphaChar);
                    words.add(temp.toString());
                }
            }

            else if (i == inputWord.length()){

                for (int j = 0; j < 26; ++j){

                    temp.setLength(0);
                    temp.append(inputWord);
                    alphaChar = (char) ('a' + j);
                    tempString = Character.toString(alphaChar);

                    temp.append(tempString);
                    words.add(temp.toString());
                }
            }
        }

        return words;
    }

    private Vector<stringObjects> createStringObjects(Set<String> words){

        Vector<stringObjects> returnSet = new Vector<>();

        for (String word : words){

            returnSet.add(new stringObjects(word));
        }
        return returnSet;
    }

    private stringObjects findMax(Vector<stringObjects> words){

        //First stringObject
        stringObjects temp = words.firstElement();

        for (stringObjects ob : words){

            if (ob.getTrieCount() > temp.getTrieCount()){

                temp = ob;
            }
        }
        return temp;
    }

    private void removeWordsNotInTrie(Vector<stringObjects> vec){

        for (int i = vec.size() - 1; i >= 0; --i){

            if (!vec.elementAt(i).inTrie){

                vec.remove(i);
            }
        }
    }

    private Set<String> findEditDistanceTwoWords(Set<String> editDistanceOneWords, Set<String> editDistanceTwoWords){

        Set<String> temp = new TreeSet<>();

        if (editDistanceOneWords.size() == 0){

            return null;
        }

        for (String ob : editDistanceOneWords){

            temp.clear();
            temp.addAll(editDistanceOneCalc(ob));
            editDistanceTwoWords.addAll(temp);
        }
        return editDistanceTwoWords;
    }
    /*--------------------------------------------------*/

    private class stringObjects extends SpellCorrector{

        private int trieCount;
        private String string;
        boolean inTrie;

        stringObjects(String string){

            this.string = string;

            INode node = trie.find(this.string);
            if (node != null && node.getValue() > 0){

                    this.trieCount = node.getValue();
                    inTrie = true;
            }

            else {

                trieCount = 0;
                inTrie = false;
            }
        }

        public int getTrieCount() {
            return trieCount;
        }

        public String getString() {
            return string;
        }
    }
}