import java.io.*;
public class Product_Reward extends ProductMaster
{
	private static int product_reward_id = 11;
	
	private static String fileName = "product_reward.csv";
	
	public boolean writeProductReward () throws IOException
	{
		PrintWriter out = null;
		try
		{
		  out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		  
		  String lineToAdd = "";
			  
		  lineToAdd += "\"";
		  lineToAdd += product_reward_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  product_reward_id ++;
		  
		  lineToAdd += "\"";
		  lineToAdd += product_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
			  
		  lineToAdd += "\"";
		  lineToAdd += "1";
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
