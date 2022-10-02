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
    // Shut down Processor when needed
    static boolean processorRunning;
    // Enable kernel
    static boolean kernel;

    // Reads from memory table
    public static int read(int index){
        // Check if user is attempting to access kernel memory
        if(index > 999 && !kernel){
            throw new Error("Memory violation: accessing system address 1000 in user mode ");
        }
        // Print command of "read" and index
        //  - [0,indexRead,-1(default)]
        pw.printf("0,%d,-1\n",index);
        pw.flush();
        // Returns what was in the given index
        return Integer.parseInt(sc.nextLine());
    }

    public static void write(int index, int value){
        // Print command of "write", index, and value
        // - [1,indexModified,valueStored]
        //System.out.println("WRITE: " + index);
        pw.printf("1,%d,%d\n",index,value);
        pw.flush();
    }

    // Read current PC (program counter)
    public static void fetch(){
        // Fetch will read next instruction in PC
        IR = read(PC);
        PC++;
    }

    public static void execute(){
        //System.out.println("Executing instruction " + IR);
        switch(IR){
            // Load value into AC
            case 1:
                fetch();
                AC = IR;
                break;
            // Load value at address into AC
            case 2:
                fetch();
                AC = read(IR);
                break;
            // Load value from address in given address into AC
            case 3:
                fetch();
                int temp = read(IR);
                AC = read(temp);
                break;
            // Load value at address + x into AC
            case 4:
                fetch();
                AC = read(IR + X);
                break;
            // Load value address + y into AC
            case 5:
                fetch();
                AC = read(IR + Y);
                break;
            // Loads adderss SP + x into AC
            case 6:
                AC = read(SP + X);
                break;
            // Store AC into address
            case 7:
                fetch();
                write(IR,AC);
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
                fetch();
                SP--;
                write(SP,PC);
                PC = IR;
                break;
            case 24:
                PC = read(SP);
                SP++;
                break;
            case 25:
                X++;
                break;
            case 26:
                X--;
                break;
            case 27:
                SP--;
                write(SP,AC);
                break;
            case 28:
                AC = read(SP);
                SP++;
                break;
            case 29:
                if(!kernel){
                    kernel = true;
                    /*
                     * Save user state in stack as:
                     *  [SP, PC, IR, AC, X, Y]
                     */
                    int userSP = SP;
                    SP = 2000;
                    SP--;
                    write(SP, userSP);
                    SP--;
                    write(SP, PC);
                    SP--;
                    write(SP, IR);
                    SP--;
                    write(SP, AC);
                    SP--;
                    write(SP, X);
                    SP--;
                    write(SP, Y);
                    PC = 1500;
                }
                break;
            case 30:
                /*
                 * Take case 29 and read it backwards,
                 *  then increment SP after each read to retrieve user values.
                 */
                Y = read(SP);
                SP++;
                X = read(SP);
                SP++;
                AC = read(SP);
                SP++;
                IR = read(SP);
                SP++;
                PC = read(SP);
                SP++;
                SP = read(SP);
                SP++;
                // Exit kernel mode at the end to avoid violations reading SP
                kernel = false;
                break;
            case 50:
                pw.printf("2,-1,-1");
                processorRunning = false;
                break;
            }
            return;
        }

    public static void main(String[] args) throws IOException{
        // Check that a file was given in args[0]
        if(args.length < 2){
            throw new Error("Not enough arguments. Please specify user file and timer.");
        }

        //static int PC, SP, IR, AC, X, Y;
        PC = 0;
        SP = 1000;
        IR = 0;
        AC = 0;
        X = 0;
        Y = 0;

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

        PC = 0;
        processorRunning = true;
        while(processorRunning){
            // Increment timer
            time++;
            // fetch instruction and store in IR
            fetch();
            // execute instruction in IR
            execute();
            if(time >= timer && !kernel){
                kernel = true;
                int userSP = SP;
                SP = 1999;
                write(userSP, SP);
                SP--;
                write(PC, SP);
                SP--;
                write(IR, SP);
                SP--;
                write(AC, SP);
                SP--;
                write(X, SP);
                SP--;
                write(Y, SP);
                //PC = 1000;
            }
        }



    }
}
