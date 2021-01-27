package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    public Set<String> words = new TreeSet<>();
    public StringBuilder key = new StringBuilder();
    public SortedSet<Character> usedGuesses = new TreeSet<>();

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {

        words.clear();
        key.setLength(0);

        try
        {
            Scanner sc = new Scanner(dictionary);
            while (sc.hasNext())
            {
                String temp = sc.next().toLowerCase(); //Case insensitive
                if (temp.length() == wordLength)
                {
                    words.add(temp);
                }
            }
        }
        catch (IOException e)
        {
            throw new IOException();
        }

        if (words.isEmpty())
        {
            throw new EmptyDictionaryException();
        }

        //Initialize key
        for (int i = 0; i < wordLength; ++i)
        {
            key.append("-");
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {

        guess = Character.toLowerCase(guess);

        if (usedGuesses.contains(guess))
        {
            throw new GuessAlreadyMadeException();
        }
        else
        {
            usedGuesses.add(guess);
        }

        //Make partitions for letter guess
        //Add each word to map
        Map<String, Set<String>> partitions = new HashMap<>();

        for (String w : words)
        {
            String tempKey = makePartitionKey(w, guess);

            if (!partitions.containsKey(tempKey))
            {
                SortedSet<String> newSet = new TreeSet<>();
                newSet.add(w);
                partitions.put(tempKey, newSet);
            }
            else
            {
                partitions.get(tempKey).add(w);
            }
        }

        //Begin process of choosing best set
        Map.Entry<String, Set<String>> best = null;
        Set<Map.Entry<String, Set<String>>> entries = partitions.entrySet();

        //Choose largest group as new set
        int maxCount = 0;
        int frequencyMaxCount = 0;

        for (Map.Entry<String, Set<String>> e : entries)
        {
            if (e.getValue().size() > maxCount)
            {
                maxCount = e.getValue().size();
                frequencyMaxCount = 1;
            }
            else if (e.getValue().size() == maxCount)
            {
                ++frequencyMaxCount;
            }
        }

        //Case: there was only one set with the maximum count
        //That is the best set
        if (frequencyMaxCount == 1)
        {
            for (Map.Entry<String, Set<String>> e : entries)
            {
                if (e.getValue().size() == maxCount)
                {
                    best = e;
                }
            }
        }

        //There were more than one sets with the maximum count
        else
        {
            //Find the group where the letter doesn't appear at all
            boolean noLetters = false;
            for (Map.Entry<String, Set<String>> e : entries)
            {
                if (e.getKey().indexOf(guess) == -1)
                {
                    best = e;
                    noLetters = true;
                }
            }

            //All sets had the letter.
            //Choose one with the fewest letters
            if (noLetters == false)
            {
                int minCount = key.length() + 1; //this ensures the first pass through will succeed
                int frequencyMinCount = 0;
                for (Map.Entry<String, Set<String>> e : entries)
                {
                    int tempCounter = 0;

                    for (int i = 0; i < e.getKey().length(); ++i)
                    {
                        if (e.getKey().charAt(i) == guess)
                        {
                            tempCounter++;
                        }
                    }
                    if (tempCounter < minCount)
                    {
                        minCount = tempCounter;
                        frequencyMinCount = 1;
                    }
                    else if (tempCounter == minCount)
                    {
                        frequencyMinCount++;
                    }
                }

                //Case: there was only one set with the min letter count
                //Use that set
                if (frequencyMinCount == 1)
                {
                    for (Map.Entry<String, Set<String>> e : entries)
                    {
                        int tempCounter = 0;

                        for (int i = 0; i < e.getKey().length(); ++i)
                        {
                            if (e.getKey().charAt(i) == guess)
                            {
                                tempCounter++;
                            }
                        }
                        if (tempCounter == minCount)
                        {
                            best = e;
                        }
                    }
                }

                //More than one set has the same min count of letter
                //Find the set with the rightmost letter
                else
                {
                    Map.Entry<String, Set<String>> currentEntry = entries.iterator().next();
                    for (Map.Entry<String, Set<String>> e : entries)
                    {
                        for (int i = e.getKey().length() - 1; i >= 0; --i)
                        {
                            if ( (currentEntry.getKey().charAt(i) == guess) && (e.getKey().charAt(i) != guess) )
                            {
                                break;
                            }
                            else if ( (currentEntry.getKey().charAt(i) != guess) && (e.getKey().charAt(i) == guess) )
                            {
                                currentEntry = e;
                                break;
                            }
                        }
                    }

                    best = currentEntry;
                }
            }
        }

        //Update key
        for (int i = 0; i < best.getKey().length(); ++i)
        {
            if (best.getKey().charAt(i) == guess)
            {
                key.replace(i, i + 1, Character.toString(guess));
            }
        }

        words = best.getValue();
        return words;
    }

    private String makePartitionKey(String w, Character g){
        //TODO implement
        StringBuilder tempKey = new StringBuilder(w);

        for (int i = 0; i < w.length(); ++i)
        {
            if (tempKey.charAt(i) != g)
            {
                tempKey.replace(i, i + 1, "-");
            }
        }

        return tempKey.toString();
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return null;
    }
}
