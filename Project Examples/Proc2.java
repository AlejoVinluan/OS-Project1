
import java.io.*;
import java.util.Scanner;
import java.lang.Runtime;

public class Proc2 
{
   public static void main(String args[])
   {
      try
      {            
	 int x;
	 Runtime rt = Runtime.getRuntime();

	 Process proc = rt.exec("java HelloYou");

    /*
     * InputStream is what we use to read from our children
     */
	 InputStream is = proc.getInputStream();
    /**
     * OutputStream is what outputs strings into console.
     */
	 OutputStream os = proc.getOutputStream();

    /*
     * PrintWriter is printing things into the OutputStream "os" console
     */
         PrintWriter pw = new PrintWriter(os);
	 pw.printf("Greg\n");
         pw.flush();

   /*
    * Scanner is reading what is coming out from the child process "HelloYou"
    */
	 Scanner sc = new Scanner(is);
	 String line = sc.nextLine();

         for (int i=0; i<line.length(); i++)
	    System.out.println(line.charAt(i)); 
	      
       System.out.println(line);
	 proc.waitFor();

         int exitVal = proc.exitValue();

         System.out.println("Process exited: " + exitVal);

      } 
      catch (Throwable t)
      {
	 t.printStackTrace();
      }
   }
}

