import java.io.*;
public class Product_Image extends ProductMaster
{
	private static int product_image_id = 1;
	
	private static String mainFolder = "data/bh/products/";
	
	private static String fileName = "product_image.csv";

	public Product_Image() 
	{
		
	}
	
	public boolean writeProductImages (int imagesCount, String sku) throws IOException
	{
		PrintWriter out = null;
		try
		{
		  out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
		  
		  String[] images = new String [imagesCount];
//=========================IMPORTANT - made i=1 (was i=0), in order to remove the first duplicate image		  
		  for (int i=1; i< imagesCount; i++)
		  {
			  images[i] = mainFolder + sku + "/" + sku + "_" + i + ".jpg";
		  }
//=========================IMPORTANT - made i=1 (was i=0), in order to remove the first duplicate image			  
		  for(int i=1; i<images.length ; i++)
		  {
			  String lineToAdd = "";
			  
			  lineToAdd += "\"";
			  lineToAdd += product_image_id;
			  lineToAdd += "\"";
			  lineToAdd += ";";
			  product_image_id ++;
			  
			  lineToAdd += "\"";
			  lineToAdd += product_id;
			  lineToAdd += "\"";
			  lineToAdd += ";";
			  
			  lineToAdd += "\"";
			  lineToAdd += images[i];
			  lineToAdd += "\"";
			  lineToAdd += ";";
			  
			  lineToAdd += "\"";
			  lineToAdd += i;
			  lineToAdd += "\"";
			  
			  out.println(lineToAdd);
		  }
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
