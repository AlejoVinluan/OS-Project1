import java.io.*;
import java.lang.Runtime;
import java.util.Random;
import java.util.Scanner;

public class Processor {

    /*
     * 1) Program counter fetches instruction from memory
     * 2) Execute carries out instruction
     * 3) After fetch, program counter is incremented
     * 4) Fetched instruction loads into IR
     * 5) Instruction in IR is decoded
     */

    /*
     * CPU registers
     *  PC - program counter (counts where we are in program)
     *  SP - 
     *  IR - instruction register (fetches )
     *  AC -
     *  X - 
     *  Y - 
     */
    static int PC, SP, IR, AC, X, Y;
    // Timer
    static int timer;
    // Input Stream will get inputs from Memory process
    static InputStream is;
    // Output Stream will output to Memory process
    static OutputStream os;
    // Print Writer used to write to memory file
    static PrintWriter pw;
    // Scanner to listen for input coming from Memory
    static Scanner sc;

    // Reads from memory table
    public static int readMemory(int index){
        // Check if user is attempting to access kernel memory
        if(index > 999){
            throw new Error("Unable to access system memory.");
        }
        // Print command of "read" and index
        //  - [0,indexRead,-1(default)]
        pw.printf("0,%d,-1",index);
        pw.flush();
        // Returns what was in the given index
        return Integer.parseInt(sc.nextLine());
    }

    public static void writeMemory(int index, int value){
        // Check if user is attempting to access kernel memory
        if(index > 999){
            throw new Error("Unable to write to system memory.");
        }
        // Print command of "write", index, and value
        // - [1,indexModified,valueStored]
        pw.printf("1,%d,%d",index,value);
        pw.flush();
    }

    // Read current PC (program counter)
    public static void fetch(){
        // Fetch will read next instruction in PC
        IR = readMemory(PC);
        PC++;
    }

    public static void execute(){
        switch(IR){
            // Load value into AC
            case 1:
                fetch();
                AC = IR;
                break;
            // Load value at address into AC
            case 2:
                fetch();
                AC = readMemory(IR);
                break;
            // Load value from address in given address into AC
            case 3:
                fetch();
                int temp = readMemory(IR);
                AC = readMemory(temp);
                break;
            // Load value at address + x into AC
            case 4:
                fetch();
                AC = readMemory(IR + X);
                break;
            // Load value address + y into AC
            case 5:
                fetch();
                AC = readMemory(IR + Y);
                break;
            // Loads adderss SP + x into AC
            case 6:
                AC = readMemory(SP + X);
                break;
            // Store AC into address
            case 7:
                fetch();
                writeMemory(IR,AC);
                break;
            case 8:
                Random rand = new Random();
                int randomInt = rand.nextInt(100)+1;
                AC = randomInt;
                break;
            case 9: 
                fetch();
                if(IR == 1)
                    System.out.print(AC);
                if (IR == 2)
                    System.out.print((char)AC);
                break;
            case 10:
                AC += X;
                break;
            case 11:
                AC += Y;
                break;
            case 12:
                AC -= X;
                break;
            case 13:
                AC -= Y;
                break;
            case 14:
                X = AC;
                break;
            case 15:
                AC = X;
                break;
            case 16:
                Y = AC;
                break;
            case 17:
                AC = Y;
                break;
            case 18:
                SP = AC;
                break;
            case 19:
                AC = SP;
                break;
            case 20:
                fetch();
                PC = IR;
                break;
            case 21:
                fetch();
                if(AC == 0)
                    PC = IR;
                break;
            case 22:
                fetch();
                if(AC != 0)
                    PC = IR;
                break;
            case 23:
                break;

        }
    }

    public static void main(String[] args) throws IOException{
        // Check that a file was given in args[0]
        if(args.length < 2){
            throw new Error("Not enough arguments. Please specify user file and timer.");
        }

        // Set time to 0 and timer to 
        int time = 0;
        timer = Integer.parseInt(args[1]);

        // Runtime will allow us to execute OS commands
        Runtime rt = Runtime.getRuntime();
        // proc will be the running Memory process
        //   Will also be used to pass in initial input file to memory
        Process proc = rt.exec("java Memory " + args[0]);
        // Input Stream will get inputs from Memory process
        is = proc.getInputStream();
        // Output Stream will output to Memory process
        os = proc.getOutputStream();
        // Used to write to memory file
        pw = new PrintWriter(os);
        // Used to listen for input from memory file
        sc = new Scanner(proc.getInputStream());

        // Begin running Processor
        boolean running = true;
        PC = 0;

        while(running || time < timer + 1){
            // fetch instruction and store in IR
            fetch();
            // execute instruction in IR
            execute();
        }



    }
}
