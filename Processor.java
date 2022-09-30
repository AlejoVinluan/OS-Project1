import java.io.*;
import java.lang.Runtime;

public class Processor {

    int PC, SP, IR, AC, X, Y;

    public static void main(String[] args) throws IOException{
        // Check that a file was given in args[0]
        if(args.length < 1){
            throw new Error("Not enough arguments. Please specify user file.");
        }

        // Runtime will allow us to execute OS commands
        Runtime rt = Runtime.getRuntime();
        // proc will be the running Memory process
        //   Will also be used to pass in initial input file to memory
        Process proc = rt.exec("java Memory " + args[0]);
        // Input Stream will get inputs from Memory process
        InputStream is = proc.getInputStream();
        // Output Stream will output to Memory process
        OutputStream os = proc.getOutputStream();



    }
}
