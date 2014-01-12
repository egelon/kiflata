import java.io.*;
public class Product_To_Store extends ProductMaster
{
	private static String fileName = "product_to_store.csv";
	
	public boolean writeProductToStore () throws IOException
	{
		PrintWriter out = null;
		try
		{
		  out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		  
		  String lineToAdd = "";
		  
		  lineToAdd += "\"";
		  lineToAdd += product_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
			  
		  lineToAdd += "\"";
		  lineToAdd += "0";
		  lineToAdd += "\"";
		  
		  out.println(lineToAdd);
		} 
		catch (IOException e)
		{
		    System.out.printf("IO Error: %s\n", e.getMessage());
		    return false;
		}
		finally
		{
		    if (out != null)
		    {
		        out.close();
		        return true;
		    }
		}
		return false;
	}
}
