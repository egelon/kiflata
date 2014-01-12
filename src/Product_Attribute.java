import java.io.*;
public class Product_Attribute extends ProductMaster
{
	private static int attribute_id = 1;
	
	private static String fileName = "product_attribute.csv";

	
	public Product_Attribute() 
	{
		
	}
	
	public void increaseAttributeID()
	{
		attribute_id = attribute_id + 1;
	}
	
	public boolean writeProductAttribute (String attribute_id, String language_id, String text) throws IOException
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
		  lineToAdd += attribute_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += language_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += text;
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
