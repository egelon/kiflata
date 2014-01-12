import java.io.*;
public class Product_To_Category extends ProductMaster
{
private static String fileName = "product_to_category.csv";
	
	public boolean writeProductToCategory (String category_id) throws IOException
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
		  lineToAdd += category_id;
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
