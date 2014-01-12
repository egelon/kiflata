import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Program {

	public static void main(String[] args) throws Exception
	{
		int count = 0;
		ArrayList<String> urlFiles = new ArrayList<String>();
		String fileName;
		
		fileName = "boohoo-dresses-urls.txt";
		buildUrlList("http://www.boohoo.com/europe/clothing/dresses/icat/dresses#esp_hitsperpage=80&esp_pg=", 13, fileName);
		urlFiles.add(fileName);

		fileName = "boohoo-tops-urls.txt";
		buildUrlList("http://www.boohoo.com/europe/clothing/tops/icat/tops#esp_hitsperpage=80&esp_pg=", 9, fileName);
		urlFiles.add(fileName);
	
		fileName="boohoo-coats-urls.txt";
		buildUrlList("http://www.boohoo.com/europe/clothing/coats+jackets/icat/coatsandjackets#esp_hitsperpage=80&esp_pg=", 3, fileName);
		urlFiles.add(fileName);

		fileName="boohoo-knitwear-urls.txt";
		buildUrlList("http://www.boohoo.com/europe/clothing/knitwear/icat/knitwear#esp_hitsperpage=80&esp_pg=", 6, fileName);
		urlFiles.add(fileName);
		
		fileName="boohoo-skirts-urls.txt";
		buildUrlList("http://www.boohoo.com/europe/clothing/skirts/icat/skirts#esp_hitsperpage=80&esp_pg=", 5, fileName);
		urlFiles.add(fileName);

		fileName="boohoo-shorts-urls.txt";
		buildUrlList("http://www.boohoo.com/europe/clothing/shorts/icat/shorts#esp_hitsperpage=80&esp_pg=", 2, fileName);
		urlFiles.add(fileName);
		
		fileName="boohoo-trousers-urls.txt";
		buildUrlList("http://www.boohoo.com/europe/clothing/trousers/icat/trousers#esp_hitsperpage=80&esp_pg=", 2, fileName);
		urlFiles.add(fileName);

		fileName="boohoo-jeans-urls.txt";
		buildUrlList("http://www.boohoo.com/europe/clothing/jeans+denim/icat/jeansdenim#esp_hitsperpage=80&esp_pg=", 1, fileName);
		urlFiles.add(fileName);
		
		fileName="boohoo-jumpsuits-urls.txt";
		buildUrlList("http://www.boohoo.com/europe/clothing/jumpsuits+dungarees/icat/jumpsuits#esp_hitsperpage=80&esp_pg=", 1, fileName);
		urlFiles.add(fileName);
	
		WebDriver driver = new FirefoxDriver();
		
		Dimension windowMinSize = new Dimension(1000,600);
		Point targetPosition = new Point(0, 0);
		driver.manage().window().setSize(windowMinSize);
		driver.manage().window().setPosition(targetPosition);
		
		BoohooParser myParser = new BoohooParser(driver);

		for(String file : urlFiles)
		{
			BufferedReader br = new BufferedReader(new FileReader(file ));
	
			String buffer;
			//For each URL in the text file
			while ((buffer = br.readLine()) != null) 
			{
				if(!myParser.parseURL(buffer))
					print("Error parsing %s\n", buffer);
				else
				{	
					if(myParser.currentProduct > myParser.numProducts)
					{
						driver.close();
						myParser.currentProduct = 0;
						driver = new FirefoxDriver();
						
						driver.manage().window().setSize(windowMinSize);
						driver.manage().window().setPosition(targetPosition);
						
						myParser.theDriver = driver;
					}
					myParser.currentProduct ++;
					
					if(myParser.exception)
					{
						myParser.exception = false;
					}
					else
					{
						System.out.println("Current item is " + count);
						count++;

						//write things to csv files here
						Product myProduct = new Product();
						Product_Attribute myProductAttribute = new Product_Attribute();
						Product_Description myProductDescription = new Product_Description();
						Product_Image myProductImage = new Product_Image();
						Product_Option myProductOption = new Product_Option();
						Product_Option_Value myProductOptionValue = new Product_Option_Value();
						Product_Reward myProductReward = new Product_Reward();
						Product_To_Category myProductToCategory = new Product_To_Category();
						Product_To_Store myProductToStore = new Product_To_Store();
						

						//first download the product images
						ImageDownloader myImageDownloader = new ImageDownloader();
						Object[] objectArray =  myParser.getImageURLS();
						String[] productImages = Arrays.copyOf(objectArray, objectArray.length, String[].class);
						
						int i=0;
						for(String productImageURL : productImages)
						{
							String folderName = myParser.getFinalProductSKU();
							String imageName = myParser.getFinalProductSKU() + "_" + i + ".jpg";
							
							myImageDownloader.downloadFile(productImageURL, folderName, imageName);
							i++;
						}
						System.out.println("Product images downloaded");
						
						
						//then download the color images
						Object[] objectArr =  myParser.getColorImageURLS();
						String[] productColors = Arrays.copyOf(objectArr, objectArr.length, String[].class);
						for(String productColorURL : productColors)
						{
							String folderName = "colors";
							String[] colorNamesFromURL = productColorURL.split("/");
						
							String imageName = "B_" + fixWhiteSpace(colorNamesFromURL[colorNamesFromURL.length-1]);
							
							myImageDownloader.downloadFile(productColorURL, folderName, imageName);
						}
						System.out.println("Color images downloaded");
						
						
						//now add the new colors to the csv files
						Object[] colorObjectArr =  myParser.getAvailableColors();
						String[] availableColors = Arrays.copyOf(colorObjectArr, colorObjectArr.length, String[].class);
						
						Color_Option_Value optionValueWriter = new Color_Option_Value();
						Color_Option_Value_Description optionValueDescriptionWriter = new Color_Option_Value_Description();
						
						
						//write the product size option
						myProductOption.writeProductOption("size");
						
						Object[] sizesObjectArr =  myParser.getAvailableSizes();
						String[] availableSizes = Arrays.copyOf(sizesObjectArr, sizesObjectArr.length, String[].class);
						for(String availableSize : availableSizes)
						{
	//=====================================================================================================
							//I M P O R T A N T!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
							
							myProductOptionValue.writeProductOptionValue("" + myProductOption.getProduct_option_id(), "13", "" + getSizeID(availableSize));
	
						}
						myProductOption.increaseProduct_option_id();
	//+++++++++++++=====================================================+++++++++++++++++++++++++++++++++++++++					
						
						myProductOption.writeProductOption("color");
						for(String color : availableColors)
						{
							optionValueWriter.writeColor(myParser.getFinalProductSKU() + "_" + fixWhiteSpace(color.toLowerCase()));
							optionValueDescriptionWriter.writeColorDescription(color.toLowerCase(), 1);
							optionValueDescriptionWriter.writeColorDescription(color.toLowerCase(), 2);
							
							//FIXED TO BE THE LAST COLOR ADDED
							myProductOptionValue.writeProductOptionValue("" + myProductOption.getProduct_option_id(), "14", "" + optionValueWriter.getOption_value_id());
							
							
							optionValueDescriptionWriter.syncIDs();
						}
						myProductOption.increaseProduct_option_id();
	//+++++++++++++=====================================================+++++++++++++++++++++++++++++++++++++++						
						
						
						//now add the product with all it's information to the csv files
						
						
						//first write the product itself
						String sku = myParser.getFinalProductSKU();
	//=============FIX THE MEASUREMENTS					
						myProduct.writeProduct(sku, sku, "data/bh/products/" + sku + "/"+ sku + "_0.jpg", myParser.getFinalProductPrice(), "90", "60", "90");
						
						//write the material attribute
						myProductAttribute.writeProductAttribute("1", "1", myParser.getFinalProductMaterial());
						myProductAttribute.writeProductAttribute("1", "2", myParser.getBgFinalProductMaterial());
						
						//write the size attribute
	//=============FIX THE MEASUREMENTS					
						myProductAttribute.writeProductAttribute("2","1",myParser.getProductDescription());
						myProductAttribute.writeProductAttribute("2", "2", myParser.getBgProductDescription());
	//=============FIX THE MEASUREMENTS						
						
						//write shipping cost attribute
						myProductAttribute.writeProductAttribute("3", "1", "5.00 BGN for Bulgaria");
						myProductAttribute.writeProductAttribute("3", "2", "5.00 лв за цяла България");
						
						//write shipping details attribute
						myProductAttribute.writeProductAttribute("4", "1", "Between 5 and 10 work days. Orders, placed after 17:30h on Saturday will be completed on Monday.");
						myProductAttribute.writeProductAttribute("4", "2", "Между 5 и 10 работни дни. Поръчки, заявени след 17:30 часа в Събота ще бъдат обработени в Понеделник.");
						
						//write the description
						myProductDescription.writeProductAttributeDescription("1", myParser.getFinalProductName(), "");
						//String bgName = "Рокля " + myParser.getFinalProductName().replace(" Dress", "");
						String bgName = myParser.getFinalProductName();
						myProductDescription.writeProductAttributeDescription("2", bgName, "");
						
						//write the images
						myProductImage.writeProductImages(myParser.getImageURLS().length, sku);
						
						//write the product reward points
						myProductReward.writeProductReward();
						
						//write the product-to-category
						if(file.equals("boohoo-dresses-urls.txt"))
							myProductToCategory.writeProductToCategory("" + getCategoryID("dresses"));
						if(file.equals("boohoo-tops-urls.txt"))
							myProductToCategory.writeProductToCategory("" + getCategoryID("tops"));
						if(file.equals("boohoo-coats-urls.txt"))
							myProductToCategory.writeProductToCategory("" + getCategoryID("coatsandjackets"));
						if(file.equals("boohoo-knitwear-urls.txt"))
							myProductToCategory.writeProductToCategory("" + getCategoryID("knitwear"));
						if(file.equals("boohoo-skirts-urls.txt"))
							myProductToCategory.writeProductToCategory("" + getCategoryID("skirts"));
						if(file.equals("boohoo-shorts-urls.txt"))
							myProductToCategory.writeProductToCategory("" + getCategoryID("shorts"));	
						if(file.equals("boohoo-trousers-urls.txt"))
							myProductToCategory.writeProductToCategory("" + getCategoryID("trousers"));
						if(file.equals("boohoo-jeans-urls.txt"))
							myProductToCategory.writeProductToCategory("" + getCategoryID("jeansdenim"));
						if(file.equals("boohoo-jumpsuits-urls.txt"))
							myProductToCategory.writeProductToCategory("" + getCategoryID("jumpsuits"));	
					
						myProductToCategory.writeProductToCategory("" + getCategoryID(myParser.getProductCategory()));
						myProductToCategory.writeProductToCategory("" + getCategoryID(myParser.getProductSubCategory()));
						myProductToStore.writeProductToStore();
						
						//update the ID
						myProduct.syncIDs();
					}
				}
				try 
				{
					//add a small timeout so we aren't DDoS-ing the server and it doesn't block our requests
					//WARNING: THIS CAUSES A SEVERE PERFORMACE HIT
					Thread.sleep(3500);
				}
				catch (InterruptedException ie) 
				{
					ie.printStackTrace();
				}
			}
			br.close();
		}
    }
	

    private static void print(String msg, Object... args) 
    {
        System.out.println(String.format(msg, args));
    }
    
    private static String fixWhiteSpace(String cName)
    {
    	String fixedColor = cName;
    	
    	if(fixedColor.contains("%20"))
		{
			fixedColor = fixedColor.replace("%20", "_");
		}
		if(fixedColor.contains(" "))
		{
			fixedColor = fixedColor.replace(" ", "_");
		}
		
    	return fixedColor;
    }

    private static int getSizeID (String size)
    {
    	if(size.equals("4"))
    		return 1;
    	if(size.equals("6"))
    		return 2;
    	if(size.equals("8"))
    		return 3;
    	if(size.equals("10"))
    		return 4;
    	if(size.equals("12"))
    		return 5;
    	if(size.equals("14"))
    		return 6;
    	if(size.equals("16"))
    		return 7;
    	if(size.equals("18"))
    		return 8;
    	//ADDED EXTRA SIZES
    	if(size.equals("20"))
    		return 9;
    	if(size.equals("22"))
    		return 10;
    	if(size.equals("24"))
    		return 11;
    	
    	
    	
    	if(size.equals("XS"))
    		return 12;
    	if(size.equals("S"))
    		return 13;
    	if(size.equals("M"))
    		return 14;
    	if(size.equals("L"))
    		return 15;
    	if(size.equals("XL"))
    		return 16;
    	if(size.equals("XXL"))
    		return 17;
    	
    	if(size.equals("S/M"))
    		return 18;
    	if(size.equals("M/L"))
    		return 19;
    	
    	if(size.equals("ONE SIZE"))
    		return 20;
    	
		return 0;
    }
    
    private static int getCategoryID (String category)
    {
    	if(category.equals("dresses"))
    		return 1;
    	if(category.equals("maxi-dresses"))
    		return 2;
    	if(category.equals("peplum-dresses"))
    		return 3;
    	if(category.equals("bodycon-dresses"))
    		return 4;
    	if(category.equals("going-out-dresses"))
    		return 5;
    	if(category.equals("skater-dresses"))
    		return 6;
    	
    	
    	if(category.equals("tops"))
    		return 7;
    	if(category.equals("day-tops"))
    		return 8;
    	if(category.equals("evening-tops"))
    		return 9;
    	if(category.equals("peplum-tops"))
    		return 10;
    	if(category.equals("shirts-and-blouses"))
    		return 11;
    	if(category.equals("t-shirts-and-hoodies"))
    		return 12;
    	if(category.equals("crop-tops"))
    		return 13;
    	if(category.equals("tunics"))
    		return 14;
    	if(category.equals("cami-swing-tops"))
    		return 15;
    	if(category.equals("kimonos"))
    		return 16;
    	
    	
    	if(category.equals("coatsandjackets"))
    		return 17;
    	if(category.equals("blazers"))
    		return 18;
    	if(category.equals("biker-jackets"))
    		return 19;
    	if(category.equals("all-coats") || category.equals("luxegrunge"))
    		return 20;
    	
    	
    	if(category.equals("knitwear"))
    		return 21;
    	if(category.equals("jumpers"))
    		return 22;
    	if(category.equals("cardigans"))
    		return 23;

    	
    	if(category.equals("skirts"))
    		return 24;
    	if(category.equals("day-skirts") || category.equals("dayskirts"))
    		return 25;
    	if(category.equals("eveningskirts") || category.equals("evening-skirts"))
    		return 26;
    	if(category.equals("maxiskirts") || category.equals("maxi-skirts"))
    		return 27;
    	if(category.equals("midiskirts") || category.equals("midi-skirts"))
    		return 28;
    	if(category.equals("miniskirts") || category.equals("mini-skirts"))
    		return 29;
    	if(category.equals("skaterskirts") || category.equals("skater-skirts"))
    		return 30;
    	
    	
    	if(category.equals("shorts"))
    		return 31;
    	if(category.equals("smart-shorts") || category.equals("smartshorts"))
    		return 32;
    	

    	if(category.equals("jeansdenim") || category.equals("jeans-denim"))
    		return 37;
    	if(category.equals("skinny-jeans") || category.equals("skinnyjeans"))
    		return 38;
    	if(category.equals("coloured-jeans") || category.equals("colouredjeans") || category.equals("fashion-denim") || category.equals("fashiondenim"))
    		return 39;
    	

    	if(category.equals("jumpsuits"))
    		return 40;
    	

    	if(category.equals("trousers"))
    		return 41;
    	
		return 7;
    }

    private static void buildUrlList(String base, int numLinks, String fname)
    {
		WebDriver driver = new FirefoxDriver();
		
		PrintWriter urlFile = null;
		try 
		{
			urlFile = new PrintWriter(new BufferedWriter(new FileWriter(fname, true)));
		}
		catch (IOException e)
		{
		    System.out.printf("IO Error: %s\n", e.getMessage());
		    return;
		}
		for(int i=1; i<= numLinks; i++)
		{
			String URL = base + i;
		
			try
			{
				driver.get(URL);
		        List<WebElement> links = driver.findElements(By.cssSelector("#comp-imageprince a"));
		        for(WebElement link : links)
		        {
		        	urlFile.println(link.getAttribute("href"));
		        }
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		urlFile.close();
		driver.close();
    }
}
