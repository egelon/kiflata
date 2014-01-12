import java.io.*;
public class Product_Option extends ProductMaster
{
	private static int product_option_id = 1;
	//==========================================================
	private static String fileName = "product_option.csv";
	
	public boolean writeProductOption (String optionType) throws IOException
	{
		PrintWriter out = null;
		try
		{
		  out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		  
		  String lineToAdd = "";
			  
		  lineToAdd += "\"";
		  lineToAdd += product_option_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
//==============		  product_option_id ++;
			  
		  lineToAdd += "\"";
		  lineToAdd += product_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
			  
		  lineToAdd += "\"";
		  if(optionType.equals("size"))
			  lineToAdd += "13";
		  if(optionType.equals("color"))
			  lineToAdd += "14";
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += ";";
			  
		  lineToAdd += "\"";
		  lineToAdd += "1";
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
	
	public int getProduct_option_id() 
	{
		return product_option_id;
	}
	
	public void increaseProduct_option_id()
	{
		product_option_id = product_option_id+1;
	}
}
