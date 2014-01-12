import java.io.*;
public class Product_Description extends ProductMaster
{
	private static String fileName = "product_description.csv";

	
	public Product_Description() 
	{
		
	}
	
	public boolean writeProductAttributeDescription (String language_id, String name, String description) throws IOException
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
		  lineToAdd += language_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += name;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += description;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += ";;";
		  
		  String[] tags = name.split(" ");
		  lineToAdd += "\"";
		  for(int i=0; i<tags.length-1; i++)
			  lineToAdd += tags[i] + "-";
		  lineToAdd += tags[tags.length-1];
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
