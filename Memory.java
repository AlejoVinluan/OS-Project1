package Project1;

import java.util.Scanner;

public class Memory {
    static int[] memory;
    static int userCount;

    public Memory(){
        memory = new int[2000];
        userCount = 0;
    }

    public static void write(int val){
        memory[userCount] = val;
        userCount++;
    }

    public static void main(String[] args){
        /*
         * When memory class is initialized, 
         *   it reads incoming stream from Processor.java 
         */ 
        Scanner sc = new Scanner(System.in);
        if(sc.hasNext()){
            // Get command integer while ignoring comments
            int command = Integer.parseInt(sc.nextLine().split(" ")[0]);

            // Write each command into memory (located 0-999)
            write(command);
        }
    }

}
