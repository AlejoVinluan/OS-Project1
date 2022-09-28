package Project1;

import java.io.*;
import java.util.Scanner;
import java.lang.Runtime;

public class main {
    public static void main(String[] args){
        /**
         * Check for errors just in case an input file was not provided.
         */
        if(args.length < 1){
            throw new Exception("Not enough arguments. Please provide input file.");
            Process.exit(0);
        }

        /**
         * Initialize the computer's memory using the input file.
         */
        Process memory = rt.exec("java Memory " + args[0]);
        

        /**
         * 
         */
    }
}