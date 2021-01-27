package hangman;

import java.io.*;
import java.util.*;

public class EvilHangman {

    public static void main(String[] args) {

        String fileName = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        File file = new File(fileName);

        EvilHangmanGame game = new EvilHangmanGame();

        try
        {
            game.startGame(file, wordLength);
        }
        catch (IOException e)
        {
            System.out.println(e.toString());
        }
        catch (EmptyDictionaryException e)
        {
            System.out.println(e.toString());
        }

        //Play the game
        do
        {
            System.out.println();
            System.out.printf("You have %d guesses left.\n", guesses);
            System.out.printf("Used letters: " + game.usedGuesses.toString() + "\n");
            System.out.printf("Word: " + game.key.toString() + "\n");

            boolean validGuess = false;
            String input;
            do
            {
                System.out.printf("Enter guess: ");
                Scanner sc = new Scanner(System.in);
                input = sc.nextLine();

                if (input.length() > 1 || !Character.isAlphabetic(input.charAt(0)))
                {
                    System.out.printf("Invalid input! ");
                }
                else if (game.usedGuesses.contains(Character.toLowerCase(input.charAt(0))))
                {
                    System.out.printf("Guess already made! ");
                }
                else
                {
                    validGuess = true;
                }
            } while (validGuess == false);

            try
            {
                game.makeGuess(input.charAt(0));
            }
            catch (GuessAlreadyMadeException e){}

            if (game.key.indexOf( String.valueOf(Character.toLowerCase(input.charAt(0))) ) == -1)
            {
                System.out.println("Sorry, there are no " + Character.toLowerCase(input.charAt(0)));
                --guesses;
            }
            else
            {
                //Count how many times that letter occurs
                int tempCount = 0;
                for (int i = 0; i < game.key.length(); ++i)
                {
                    if (game.key.charAt(i) == Character.toLowerCase(input.charAt(0)))
                    {
                        ++tempCount;
                    }
                }

                System.out.printf("Yes, there is " +
                                  String.valueOf(tempCount) +
                                  " " + Character.toLowerCase(input.charAt(0)) +
                                  "\n");
            }

            if (guesses == 0)
            {
                System.out.println("Sorry, you lost! The word was: " + game.words.iterator().next());
            }

            if (game.key.indexOf("-") == -1)
            {
                System.out.println("You win! You guessed the word: " + game.words.iterator().next());
                break;
            }
        } while (guesses > 0);
    }
}
