import java.io.*;

public class Color_Option_Value extends Color_Option
{
	private static String colorOptionsImageDir = "data/bh/options/colors/";
	private static int color_sort_order = 1;
	
	private static String fileName = "option_value.csv";
	
	private String fullImagePath;
	
	public Color_Option_Value() 
	{
		fullImagePath = "";
	}
	
	public boolean writeColor (String cName) throws IOException
	{
		fullImagePath = colorOptionsImageDir + cName + ".jpg";
		
		PrintWriter out = null;
		//if(checkIfColorExists(fileName, cName))
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
		  lineToAdd += color_option_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += fullImagePath;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += color_sort_order;
		  lineToAdd += "\"";
		  color_sort_order ++;

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
	public int getColorOptionIDFromFile(String colorName) throws FileNotFoundException
	{
		BufferedReader br;
		br = new BufferedReader(new FileReader(fileName));
		 
		String line;  
		try 
		{
			while ((line = br.readLine()) != null) 
			{   
				if(line.contains(colorName))
				{
					String[] splitLine = line.substring(1).split("\"");
					return Integer.parseInt(splitLine[0]);
				}
			}
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
		return 0;
	}
}
