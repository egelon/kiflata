import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Color_Option 
{
	protected static int option_value_id = 21;
	protected static int color_option_id = 14;
	
	protected void syncIDs()
	{
		option_value_id = option_value_id + 1;
	}
	
	public int getOption_value_id() 
	{
		return option_value_id;
	}
	
	protected boolean checkIfColorExists(String fileName, String colorName) throws FileNotFoundException
	{
		BufferedReader br;
		br = new BufferedReader(new FileReader(fileName));
		 
		String line;  
		try 
		{
			while ((line = br.readLine()) != null) 
			{   
				if(line.contains(colorName))
					return true;
			}
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	//1";"14";"white
	protected boolean checkIfColorWithLangIDExists(String fileName, String colorName, String langID) throws FileNotFoundException
	{
		BufferedReader br;
		br = new BufferedReader(new FileReader(fileName));
		 
		String line;  
		try 
		{
			while ((line = br.readLine()) != null) 
			{   
				if(line.contains(langID + "\";\"14\";\"" + colorName))
					return true;
			}
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
}
