import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * NOTES:
 *  1) When memory object is made, Memory object is created.
 *    a) Memory object will take input file and write it to user memory.
 * 2) Memory will then wait for inputs coming from processor
 */

public class Memory {
    static int[] memory;
    static int memoryCount;
    static String fileName;

    public static void loadMemory() throws FileNotFoundException{
        /*
         * Initialize memory of size 2000
         *  index 0-999 for user program
         *  1000-1999 are for system
         */
        memory = new int[2000];
        // Pointer to local index.
        memoryCount = 0;

        /*
         * When memory class is initialized, 
         *   it reads incoming stream from Processor.java 
         */ 
        File file = new File(fileName);
        Scanner sc = new Scanner(file);
        while(sc.hasNextLine()){
            // Get command integer while ignoring comments
            String command = sc.nextLine();
            command.trim();

            // Check for empty line
            if(command.length() <= 0){
                continue;
            }

            // Change command so that we only access the first value
            command = command.split(" ")[0];

            // Check if line starts with a comment
            if(command.charAt(0) == '/'){
                continue;
            }

            // If the command starts with a period, change index
            if(command.charAt(0) == '.'){
                String newIndex = command.substring(1,command.length()-1);
                memoryCount = Integer.parseInt(newIndex);
                continue;
            }
    
            // Write to memory
            memory[memoryCount] = Integer.parseInt(command);
            memoryCount++;
        }
        sc.close();
    }

    public static int read(int index){
        // Check if user is attempting to access system data.
        return memory[index];
    }

    public static void write(int index, int val){
        // Check if user wants to write to system code.
        memory[memoryCount] = val;
    }

    public static void main(String[] args) throws FileNotFoundException{
        if(args.length < 1){
            throw new Error("Input file not provided.");
        }
        fileName = args[0];
        loadMemory();

        // Read incoming stream from Processor.java
        Scanner sc = new Scanner(System.in);

        boolean running = true;
        while(running){
            /*
             * Listen to input from Processor.java and execute commands
             *  0 - read
             *  1 - write
             *  2 - end
             * 
             * Commands from Processor.java seperated by commas
             *  - command[0] - command
             *  - command[1] - index
             *  - command[2] - value
             * 
             *  If command[index] is not used, we will send -1 instead to prevent error
             *  (for example, read does not use command[2], so -1 will be sent)
             */
            String[] command_whole = sc.nextLine().split(",");
            int command = Integer.parseInt(command_whole[0]);
            int index = Integer.parseInt(command_whole[1]);
            int value = Integer.parseInt(command_whole[2]);
            if(command == 0){
                System.out.println(read(index));
            }   else if (command == 1){
                write(index,value);
            }   else if (command == 2){
                running = false;
            }
        }
        sc.close();
        System.exit(0);
    }

}
