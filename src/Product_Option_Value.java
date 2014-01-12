import java.io.*;
public class Product_Option_Value extends ProductMaster
{
	private static int product_option_value_id = 1;	
	private static String fileName = "product_option_value.csv";
	
	public boolean writeProductOptionValue (String product_option_id, String option_id, String option_value_id) throws IOException
	{
		PrintWriter out = null;
		try
		{
		  out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		  
		  String lineToAdd = "";
			  
		  lineToAdd += "\"";
		  lineToAdd += product_option_value_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  product_option_value_id ++;
			  
		  lineToAdd += "\"";
		  lineToAdd += product_option_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += product_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
			  
		  lineToAdd += "\"";
		  lineToAdd += option_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
			  
		  lineToAdd += "\"";
		  lineToAdd += option_value_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"1\";\"0\";\"0.0000\";\"+\";\"0\";\"+\";\"0.00000000\";\"+\"";
		  
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
