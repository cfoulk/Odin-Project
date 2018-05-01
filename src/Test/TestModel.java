package Test;
import Model.OdinModel;
import java.io.PrintWriter;
import java.io.File;
import java.io.*;
import java.util.ArrayList;


public class TestModel {

    public static void main(String []args) throws Exception
    {
       try
       {
           PrintWriter printWriter = new PrintWriter("ModelServerTest");
           printWriter.print("");
           printWriter.close();

           FileWriter fileWriter = new FileWriter("ModelServerTest");

           BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);



       }

       catch (IOException e)
       {
           System.out.println(e);
       }




    }


}
