import java.io.*;
import java.net.IDN;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        generateHelper("",letters);
        System.out.print(words);
    }

    public void generateHelper(String gen, String word)
    {
        if (!gen.equals(""))
        {
            words.add(gen);
        }

        //base case
        if (word.length() == 0)
        {
            return;
        }

        for (int i = 0; i<word.length();i++)
        {
            generateHelper(gen+word.charAt(i),word.substring(0,i)+word.substring(i+1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        mergeSort(0,words.size()-1);
        System.out.println("\n"+words);
    }

    public void mergeSort(int l, int r) {
        //base case with 1 element
        if (l == r)
        {
            return;
        }

        //split area looking at
        int m = (l+r)/2;
        mergeSort(l,m);
        mergeSort(m+1,r);

        //merge the two halves together
        merge(l,r,m);
    }

    public void merge(int l, int r, int m)
    {
        //merge two halves
        int leftLen = m-l+1;
        int rightLen = r-m;
        ArrayList<String> left = new ArrayList<>();
        ArrayList<String> right = new ArrayList<>();

        //fill temp arraylists
        for (int i = 0; i < leftLen; i++)
        {
            left.add(words.get(l+i));
        }
        for (int i = 0; i <rightLen; i++)
        {
            right.add(words.get(m+1+i));
        }

        int i =0;
        int j = 0;
        int a = l;
        //compare the elements at i and j
        //the one that comes before the other gets put back into words first
        while (i < leftLen && j < rightLen) {
            if (left.get(i).compareTo(right.get(j)) < 0)
            {
                words.set(a, left.get(i));
                i++;
            }
            else
            {
                words.set(a, right.get(j));
                j++;
            }
            a++;
        }

        //put remaining stuff from left into words
        while (i < leftLen)
        {
            words.set(a, left.get(i));
            i++;
            a++;
        }

        //put remaining stuff from right into words
        while (j < rightLen)
        {
            words.set(a, right.get(j));
            j++;
            a++;
        }
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        for (int i = 0; i < words.size(); i++)
        {
            if (!isInDictionary(words.get(i),0,DICTIONARY_SIZE-1))
            {
                words.remove(i);
                i--;
            }
        }
        System.out.println(words);
    }

    public boolean isInDictionary(String word, int start, int end)
    {
        int mid = (start+end)/2;
        //base case
        if (word.equals(DICTIONARY[mid]))
        {
            return true;
        }
        else if (start >= end)
        {
            return false;
        }

        //recursive
        //if the word comes before the dictionary word
        if (word.compareTo(DICTIONARY[mid]) < 0)
        {
            return isInDictionary(word,start,mid-1);
        }
        if (word.compareTo(DICTIONARY[mid]) > 0)
        {
            return isInDictionary(word,mid+1,end);
        }

        return true;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}