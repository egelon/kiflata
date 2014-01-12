import java.io.*;

public class Product extends ProductMaster
{
	private static String model;
	private static String sku;
	private static String quantity = "10";
	private static String stock_status_id = "5";
	private static String image;
	private static String manufacturer_id = "1";
	private static String shipping = "1";
	private static String price;
	private static String points = "0";
	private static String tax_class_id = "9";
	private static String date_available = "2013-08-14";
	private static String weight = "500.00000000";
	private static String weight_class_id = "2";
	private static String length;
	private static String width;
	private static String height;
	private static String length_class_id = "1";
	private static String substract = "0";
	private static String minimum = "1";
	private static String sort_order = "1";
	private static String status = "1";
	private static String date_added = "2013-12-12 14:20:32";
	private static String date_modified = "2013-12-12 14:20:32";
	private static String viewed = "10";

	private static String fileName = "product.csv";

	
	public Product() 
	{
		model = "";
		sku = "";
		image = "";
		price = "";
		length = "";
		width = "";
		height = "";
	}
	
	public boolean writeProduct (String model, String sku, String image, String price, String length, String width, String height) throws IOException
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
		  lineToAdd += model;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += sku;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += ";;;;;;";
		  
		  lineToAdd += "\"";
		  lineToAdd += quantity;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += stock_status_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += image;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += manufacturer_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += shipping;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += price;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += points;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += tax_class_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += date_available;
		  lineToAdd += "\"";
		  lineToAdd += ";";
		  
		  lineToAdd += "\"";
		  lineToAdd += weight;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += weight_class_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += length;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += width;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += height;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += length_class_id;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += substract;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += minimum;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += sort_order;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += status;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += date_added;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += date_modified;
		  lineToAdd += "\"";
		  lineToAdd += ";";

		  lineToAdd += "\"";
		  lineToAdd += viewed;
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
