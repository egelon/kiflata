import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class Color_Option_Value_Description extends Color_Option
{
	
	private static String fileName = "option_value_description.csv";
	
	private String colorName;
	
	public Color_Option_Value_Description() 
	{
		colorName = "";
	}
	
	public boolean writeColorDescription (String cName, int langID) throws IOException
	{
		colorName = cName;
		
		PrintWriter out = null;
		//if(checkIfColorWithLangIDExists(fileName, cName, "" + langID))
		//	return false;

		try 
		{
		  out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		  
		  String lineToAdd = "";
		  
		  lineToAdd += "\"";
		  lineToAdd += option_value_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += langID;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += color_option_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += colorName;
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
