import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BoohooParser 
{
	public static boolean exception;
	public int numProducts = 25;
	public int currentProduct = 0;
	
	private static int sizesStartingIndex = 2;
	
	private static String productCategory;
	private static String productSubCategory;
	private static String productSKU;

	private static String parserBaseURL = "http://www.boohoo.com/restofworld/invt/";
	private static String logFileName = "parser_log.txt";
	
	private static String finalProductName;
	private static String finalProductSKUBase = "B_";
	
	//changed price convertion rates
	private static double conversionRate = 2;
	private static double addedValue = 15.00;
	private static double finalProductPrice;
	
	private static Object[] availableSizes;
	private static Object[] availableColors;
	private static Object[] colorImageURLS;
	private static Object[] imageURLS;

	public WebDriver theDriver;

	private String finalProductMaterial;
	private static String productDescription;
	
	public String getProductDescription() {
		return productDescription;
	}
	
private static String bgProductDescription;
	
	public String getBgProductDescription() {
		return bgProductDescription;
	}

	public String getFinalProductMaterial() {
		return finalProductMaterial;
	}
	
	private String bgFinalProductMaterial;
	public String getBgFinalProductMaterial() {
		return bgFinalProductMaterial;
	}
	public  Object[] getColorImageURLS() 
	{
		return colorImageURLS;
	}
	
	public Object[] getImageURLS() 
	{
		return imageURLS;
	}
	
	public Object[] getAvailableSizes() 
	{
		return availableSizes;
	}
	
	public Object[] getAvailableColors() 
	{
		return availableColors;
	}
	
	public String getProductCategory() 
	{
		return productCategory;
	}
	
	public String getProductSubCategory() 
	{
		return productSubCategory;
	}
	
	public String getFinalProductPrice() 
	{
		return String.valueOf(finalProductPrice);
	}

	public String getFinalProductSKU() 
	{
		return finalProductSKUBase + productSKU;
	}

	public String getFinalProductName() 
	{
		return finalProductName;
	}

	public BoohooParser(WebDriver driv)
	{
		theDriver = driv;
		exception = false;
	}

	
	@SuppressWarnings("resource")
	public boolean parseURL(String theURL) throws Exception
	{
		PrintWriter log = null;
		try 
		{
		  log = new PrintWriter(new BufferedWriter(new FileWriter(logFileName, true)));
		}
		catch (IOException e)
		{
		    System.out.printf("IO Error: %s\n", e.getMessage());
		    exception = true;
		    return false;
		}
		
		String[] parts = theURL.split("/");
			
		productCategory = parts[parts.length-5];
		productSubCategory = parts[parts.length-4];
		productSKU = parts[parts.length-1];

		String fullProductURL = parserBaseURL + productSKU;
		
		try
		{
	        //theDriver.get(fullProductURL);
			theDriver.get(theURL);
	        //obrabotka na snimkite
	        
	        List<WebElement> imagesRow = theDriver.findElements(By.cssSelector("div#productdetail-altview div table[class*='hide'] td a"));
	        
	        ArrayList<String> imageURLSList = new ArrayList<String>();
	        for(WebElement imageURL : imagesRow)
	        {
	        	imageURLSList.add(imageURL.getAttribute("href"));
	        }
	        
	        imageURLS = sanitiseArrayList(imageURLSList);
	        
	        System.out.printf("Images: ");
	        for (int i=0; i<imageURLS.length; i++)
	        	System.out.printf("%s\n", imageURLS[i]);
	        
	        //obrabotka na imeto
	        
	        WebElement parsedProductName = theDriver.findElement(By.cssSelector("h1[id^=comp-name]"));
	        
	        finalProductName = parsedProductName.getText();
	        System.out.println("Product name: " + finalProductName);
	        
	        //obrabotka na cenata
	        
	        WebElement parsedProductPrice = theDriver.findElement(By.cssSelector("span.atrPrice"));
	        
	        finalProductPrice = calculateFinalPrice(parsedProductPrice.getText().substring(1));
	        System.out.println("Product price: " + finalProductPrice);
	        
	        
	        //obrabotka na razmerite
	        
	        WebElement theSizesRow = theDriver.findElement(By.cssSelector("table#gridtable tr"));
	        
	        String[] sizes = theSizesRow.getText().split(" ");
	        ArrayList<String> onlyTheSizes = new ArrayList<String>();
	        for(int i=sizesStartingIndex; i<sizes.length; i++)
	        	onlyTheSizes.add(sizes[i]);
	        availableSizes = onlyTheSizes.toArray();
	            
	        System.out.printf("Available sizes: ");
	        for (int i=0; i<availableSizes.length; i++)
	        	System.out.printf("%s ", availableSizes[i]);
	        System.out.printf("\n");
	        
	        
	        //obrabotka na imenata na cvetovete
	        
	        List<WebElement> theColorsTable = theDriver.findElements(By.cssSelector("table#gridtable tbody tr th"));
	        
	        ArrayList<String> onlyTheColors = new ArrayList<String>();
	        for(WebElement color : theColorsTable)
	        {
	        	onlyTheColors.add(color.getText());
	        }
	        
	        availableColors = sanitiseArrayList (onlyTheColors);
	        
	        System.out.printf("Available colors: \n");
	        for (int i=0; i<availableColors.length; i++)
	        	System.out.println(availableColors[i] + " ");
	        System.out.println("------");
	        
	        //obrabotka na kartinkite na cvetovete
	        
	        List<WebElement> theColorsImagesTable = theDriver.findElements(By.cssSelector("div[id^='selected-azz'] p a img"));
	        
	        ArrayList<String> onlyTheColorImagesURLS = new ArrayList<String>();
	        for(WebElement colorImageURL : theColorsImagesTable)
	        {
	        	onlyTheColorImagesURLS.add(colorImageURL.getAttribute("src"));
	        }
	        
	        colorImageURLS = sanitiseArrayList(onlyTheColorImagesURLS);
	        
	        System.out.printf("Colors Images: ");
	        for (int i=0; i<colorImageURLS.length; i++)
	        	System.out.printf("%s\n", colorImageURLS[i]);
	        
	        //obrabotka na materiala i razmerite na roklqta
	        WebElement productMaterialTab = theDriver.findElement(By.cssSelector("a.tabcompo"));
	        productMaterialTab.click();
	        
	        WebElement parsedProductMaterialFromDescription = theDriver.findElement(By.cssSelector("div.yui-content div#tab2"));
	        String tab2Text = parsedProductMaterialFromDescription.getText();
	        
	        
	        String[] SplittedMaterial = tab2Text.split("\\.");
	        finalProductMaterial = SplittedMaterial[0];
	        bgFinalProductMaterial = bulgarianMaterial();
	        System.out.println("Product material: " + finalProductMaterial);
	        
	        if(SplittedMaterial.length > 1)
	        {
		        productDescription = tab2Text.substring(finalProductMaterial.length() + 2);
		        sanitiseDescription();
		        bgProductDescription = bulgarianDescription();
		        System.out.println("Product description: " + productDescription);
	        }
	        else
	        {
	        	productDescription = "";
	        	bgProductDescription = productDescription;
		        System.out.println("Product description: " + productDescription);
	        }
	        
		} 
		catch (Exception e) 
		{
            e.printStackTrace();
            log.println("Error on: " + fullProductURL);
            log.println("URL given to parser: " + theURL);
            log.println("Exception message: ");
            log.println(e.getMessage());
            log.println("*====================*");
            exception = true;
        }
		
		if (log != null)
	    {
	        log.close();
	        return true;
	    }
		return false;
    }
	
	private static double RoundTo2Decimals(double val) 
	{
        DecimalFormat df2 = new DecimalFormat("###,##");
        return Double.valueOf(df2.format(val));
	}
	
	private static double calculateFinalPrice (String boohooPrice)
	{
		double parsedPriceValue =  Double.valueOf(boohooPrice).doubleValue();
    	double priceInLevs = parsedPriceValue * conversionRate;
    	return RoundTo2Decimals(priceInLevs + addedValue);
	}

	private void sanitiseDescription()
	{
		if(productDescription.contains("\""));
			productDescription = productDescription.replaceAll("\"", "in");
	}

	
	private Object[] sanitiseArrayList(ArrayList<String> arrlist)
	{
		//hack to clear repeated entries in the colors array
	     HashSet hs = new HashSet();
	     hs.addAll(arrlist);
	     for (Iterator<String> i = hs.iterator(); i.hasNext();) 
	     {
	    	    String element = i.next();
	    	    if (element.equals(""))
	    	    {
	    	        i.remove();
	    	    }
	    }
	    arrlist.clear();
	    arrlist.addAll(hs);

	    return arrlist.toArray();
	}
	
	private static String doubleSize(String originalSize)
	{
		String result = new String();
		try
		{
			double originalValue = Double.parseDouble(originalSize);
			result = String.valueOf(originalValue * 2.0);
		}
		catch(Exception e)
		{
			exception = true;
		}
		return result;
	}
    
	private static String bulgarianDescription()
	{
		String bgDesc = productDescription;
		
		if(bgDesc.contains("Flat Measurement Not Worn"))
		{
			bgDesc = bgDesc.replace("Flat Measurement Not Worn", "Размер");
		}
		if(bgDesc.contains("Flat Measurement of Garment Not Worn"))
		{
			bgDesc = bgDesc.replace("Flat Measurement of Garment Not Worn", "Размер");
		}
		if(bgDesc.contains("Flat Measurement Of Garment Not Worn"))
		{
			bgDesc = bgDesc.replace("Flat Measurement Of Garment Not Worn", "Размер");
		}
		if(bgDesc.contains("Flat Measurement of the Garment Not Worn"))
		{
			bgDesc = bgDesc.replace("Flat Measurement of the Garment Not Worn", "Размер");
		}
		if(bgDesc.contains("Flat Measurement of The Garment Not Worn"))
		{
			bgDesc = bgDesc.replace("Flat Measurement of The Garment Not Worn", "Размер");
		}
		if(bgDesc.contains("Flat measurement of the garment not worn"))
		{
			bgDesc = bgDesc.replace("Flat measurement of the garment not worn", "Размер");
		}
		if(bgDesc.contains("Flat Measurements of the Garment NOt Worn"))
		{
			bgDesc = bgDesc.replace("Flat Measurements of the Garment NOt Worn", "Размер");
		}
		
		
		if(bgDesc.contains("Length:"))
		{
			bgDesc = bgDesc.replace("Length:", "Дължина:");
		}
		
		
		if(bgDesc.contains("Total Length"))
		{
			bgDesc = bgDesc.replace("Total Length", "Дължина");
		}
		if(bgDesc.contains("Total length"))
		{
			bgDesc = bgDesc.replace("Total length", "Дължина");
		}
		
		

		if(bgDesc.contains("Inside Leg Length"))
		{
			bgDesc = bgDesc.replace("Inside Leg Length", "Вътрешна дължина от чатала");
		}
		if(bgDesc.contains("Inside leg Length"))
		{
			bgDesc = bgDesc.replace("Inside leg Length", "Вътрешна дължина от чатала");
		}
		if(bgDesc.contains("Inside leg length"))
		{
			bgDesc = bgDesc.replace("Inside leg length", "Вътрешна дължина от чатала");
		}
		
		if(bgDesc.contains("Inside Leg"))
		{
			bgDesc = bgDesc.replace("Inside Leg", "Вътрешна дължина от чатала");
		}
		if(bgDesc.contains("Inside leg"))
		{
			bgDesc = bgDesc.replace("Inside leg", "Вътрешна дължина от чатала");
		}
		
		
		
		if(bgDesc.contains("Shoulder to Hem"))
		{
			bgDesc = bgDesc.replace("Shoulder to Hem", "Рамо до подгъв");
		}
		if(bgDesc.contains("Shoulder to hem"))
		{
			bgDesc = bgDesc.replace("Shoulder to hem", "Рамо до подгъв");
		}
		if(bgDesc.contains("Shuolder to Hem"))
		{
			bgDesc = bgDesc.replace("Shuolder to Hem", "Рамо до подгъв");
		}

		if(bgDesc.contains("Centre Back To Hem"))
		{
			bgDesc = bgDesc.replace("Centre Back To Hem", "Център на гръб до подгъв");
		}
		if(bgDesc.contains("Centre Back to Hem"))
		{
			bgDesc = bgDesc.replace("Centre Back to Hem", "Център на гръб до подгъв");
		}
		if(bgDesc.contains("Centre Back to hem"))
		{
			bgDesc = bgDesc.replace("Centre Back to hem", "Център на гръб до подгъв");
		}
		if(bgDesc.contains("Centre back to Hem"))
		{
			bgDesc = bgDesc.replace("Centre back to Hem", "Център на гръб до подгъв");
		}
		if(bgDesc.contains("Centre back to hem"))
		{
			bgDesc = bgDesc.replace("Centre back to hem", "Център на гръб до подгъв");
		}
		
		
		
		if(bgDesc.contains("Bust"))
		{
			String[] bust = bgDesc.split("Bust ");
			String[] theSize = bust[1].split("cm");
			String fixedSize = doubleSize(theSize[0]);
			bgDesc = bgDesc.replace(theSize[0], fixedSize);

			
			bgDesc = bgDesc.replace("Bust", "Бюст");
		}
		
		if(bgDesc.contains("Waist"))
		{
			String[] waist = bgDesc.split("Waist ");
			String[] theSize = waist[1].split("cm");
			String fixedSize = doubleSize(theSize[0]);
			bgDesc = bgDesc.replace(theSize[0], fixedSize);
			
			bgDesc = bgDesc.replace("Waist", "Талия");
		}
		
		if(bgDesc.contains("Hips"))
		{
			String[] hip = bgDesc.split("Hips ");
			String[] theSize = hip[1].split("cm");
			String fixedSize = doubleSize(theSize[0]);
			bgDesc = bgDesc.replace(theSize[0], fixedSize);
			
			bgDesc = bgDesc.replace("Hips", "Ханш");
		}
		if(bgDesc.contains("Hip"))
		{
			String[] hip = bgDesc.split("Hip ");
			String[] theSize = hip[1].split("cm");
			String fixedSize = doubleSize(theSize[0]);
			bgDesc = bgDesc.replace(theSize[0], fixedSize);
			
			bgDesc = bgDesc.replace("Hip", "Ханш");
		}

		
		if(bgDesc.contains("Thighs"))
		{
			String[] tigh = bgDesc.split("Thighs ");
			String[] theSize = tigh[1].split("cm");
			String fixedSize = doubleSize(theSize[0]);
			bgDesc = bgDesc.replace(theSize[0], fixedSize);
			
			bgDesc = bgDesc.replace("Thighs", "Бедрa");
		}
		if(bgDesc.contains("Thigh"))
		{
			String[] tigh = bgDesc.split("Thigh ");
			String[] theSize = tigh[1].split("cm");
			String fixedSize = doubleSize(theSize[0]);
			bgDesc = bgDesc.replace(theSize[0], fixedSize);
			
			bgDesc = bgDesc.replace("Thigh", "Бедро");
		}
		
		if(bgDesc.contains("Hem Width"))
		{
			String[] hem = bgDesc.split("Hem Width ");
			String[] theSize = hem[1].split("cm");
			String fixedSize = doubleSize(theSize[0]);
			bgDesc = bgDesc.replace(theSize[0], fixedSize);
			
			bgDesc = bgDesc.replace("Hem Width", "Ширина при подгъв");
		}
		if(bgDesc.contains("Hem width"))
		{
			String[] hem = bgDesc.split("Hem width ");
			String[] theSize = hem[1].split("cm");
			String fixedSize = doubleSize(theSize[0]);
			bgDesc = bgDesc.replace(theSize[0], fixedSize);
			
			bgDesc = bgDesc.replace("Hem width", "Ширина при подгъв");
		}
		
		
		if(bgDesc.contains("Hem"))
		{
			String[] hem = bgDesc.split("Hem ");
			String[] theSize = hem[1].split("cm");
			String fixedSize = doubleSize(theSize[0]);
			bgDesc = bgDesc.replace(theSize[0], fixedSize);
			
			bgDesc = bgDesc.replace("Hem", "Подгъв");
		}
		
		
		if(bgDesc.contains("Armhole"))
		{
			bgDesc = bgDesc.replace("Armhole", " Ръкавна извивка");
		}
		
		
		
		if(bgDesc.contains("Sleeve Length"))
		{
			bgDesc = bgDesc.replace("Sleeve Length", "Дължина на ръкав");
		}
		if(bgDesc.contains("Sleeve length"))
		{
			bgDesc = bgDesc.replace("Sleeve length", "Дължина на ръкав");
		}
		

		if(bgDesc.contains("Measured from size"))
		{
			bgDesc = bgDesc.replace("Measured from size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured From Size"))
		{
			bgDesc = bgDesc.replace("Measured From Size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured from a size"))
		{
			bgDesc = bgDesc.replace("Measured from a size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured from a Size"))
		{
			bgDesc = bgDesc.replace("Measured from a Size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured on size"))
		{
			bgDesc = bgDesc.replace("Measured on size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured on Size"))
		{
			bgDesc = bgDesc.replace("Measured on Size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured on a size"))
		{
			bgDesc = bgDesc.replace("Measured on a size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured on a Size"))
		{
			bgDesc = bgDesc.replace("Measured on a Size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured from a UK size"))
		{
			bgDesc = bgDesc.replace("Measured from a UK size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured from a UK Size"))
		{
			bgDesc = bgDesc.replace("Measured from a UK Size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured on a UK size"))
		{
			bgDesc = bgDesc.replace("Measured on a UK size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured on a UK Size"))
		{
			bgDesc = bgDesc.replace("Measured on a UK Size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured on UK Size"))
		{
			bgDesc = bgDesc.replace("Measured on UK Size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured from Size"))
		{
			bgDesc = bgDesc.replace("Measured from Size", "Измерено от мостра с размер");
		}
		
		if(bgDesc.contains("Do Not Wash"))
		{
			bgDesc = bgDesc.replace("Do Not Wash", "Да не се пере");
		}
		if(bgDesc.contains("Do NOt Wash"))
		{
			bgDesc = bgDesc.replace("Do NOt Wash", "Да не се пере");
		}
		if(bgDesc.contains("Do not Wash"))
		{
			bgDesc = bgDesc.replace("Do not Wash", "Да не се пере");
		}
		
		
		if(bgDesc.contains("Machine Washable"))
		{
			bgDesc = bgDesc.replace("Machine Washable", "Може да се пере в пералня");
		}
		if(bgDesc.contains("Machine washable"))
		{
			bgDesc = bgDesc.replace("Machine washable", "Може да се пере в пералня");
		}
		if(bgDesc.contains("Machine Wash"))
		{
			bgDesc = bgDesc.replace("Machine Wash", "Може да се пере в пералня");
		}
		if(bgDesc.contains("Machine wash"))
		{
			bgDesc = bgDesc.replace("Machine wash", "Може да се пере в пералня");
		}
		
		
		if(bgDesc.contains("Cool Hand Wash"))
		{
			bgDesc = bgDesc.replace("Cool Hand Wash", "Да се пере на ръка с хладка вода");
		}
		
		if(bgDesc.contains("Hand Wash"))
		{
			bgDesc = bgDesc.replace("Hand Wash", "Да се пере на ръка");
		}
		if(bgDesc.contains("Hand wash"))
		{
			bgDesc = bgDesc.replace("Hand wash", "Да се пере на ръка");
		}
		if(bgDesc.contains("Hand Washable"))
		{
			bgDesc = bgDesc.replace("Hand Washable", "Да се пере на ръка");
		}
		if(bgDesc.contains("Hand washable"))
		{
			bgDesc = bgDesc.replace("Hand washable", "Да се пере на ръка");
		}
		
		
		if(bgDesc.contains("seperately"))
		{
			bgDesc = bgDesc.replace("seperately", "отделно");
		}
		
		
		//Dry Clean Only
		if(bgDesc.contains("Dry Clean Only"))
		{
			bgDesc = bgDesc.replace("Dry Clean Only", "Химическо чистене");
		}
		
		
		if(bgDesc.contains("Model wears size"))
		{
			bgDesc = bgDesc.replace("Model wears size", "Манекенът носи размер");
		}
		if(bgDesc.contains("Model Wears Size"))
		{
			bgDesc = bgDesc.replace("Model Wears Size", "Манекенът носи размер");
		}
		if(bgDesc.contains("Model wears Size"))
		{
			bgDesc = bgDesc.replace("Model wears Size", "Манекенът носи размер");
		}
		if(bgDesc.contains("Model Wears a Size"))
		{
			bgDesc = bgDesc.replace("Model Wears a Size", "Манекенът носи размер");
		}
		//Model wears a size
		if(bgDesc.contains("Model wears a size"))
		{
			bgDesc = bgDesc.replace("Model wears a size", "Манекенът носи размер");
		}
		if(bgDesc.contains("Model Wears a UK Size"))
		{
			bgDesc = bgDesc.replace("Model Wears a UK Size", "Манекенът носи размер");
		}
		if(bgDesc.contains("Model Wears UK Size"))
		{
			bgDesc = bgDesc.replace("Model Wears UK Size", "Манекенът носи размер");
		}
		if(bgDesc.contains("Model wears UK Size"))
		{
			bgDesc = bgDesc.replace("Model wears UK Size", "Манекенът носи размер");
		}
		//Model wears UK size
		if(bgDesc.contains("Model wears UK size"))
		{
			bgDesc = bgDesc.replace("Model wears UK size", "Манекенът носи размер");
		}
		
		//Measured from UK size
		if(bgDesc.contains("Measured from UK size"))
		{
			bgDesc = bgDesc.replace("Measured from UK size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured from UK Size"))
		{
			bgDesc = bgDesc.replace("Measured from UK Size", "Измерено от мостра с размер");
		}
		if(bgDesc.contains("Measured from a UK Size"))
		{
			bgDesc = bgDesc.replace("Measured from a UK Size", "Измерено от мостра с размер");
		}
		
		if(bgDesc.contains("Adjustable Straps"))
		{
			bgDesc = bgDesc.replace("Adjustable Straps", "Регулируеми презрамки");
		}
		if(bgDesc.contains("Adjustable straps"))
		{
			bgDesc = bgDesc.replace("Adjustable straps", "Регулируеми презрамки");
		}
		
		
		if(bgDesc.contains("Slip Included"))
		{
			bgDesc = bgDesc.replace("Slip Included", "Включва лента за затягане");
		}
		if(bgDesc.contains("Slip included"))
		{
			bgDesc = bgDesc.replace("Slip included", "Включва лента за затягане");
		}
		if(bgDesc.contains("Includes Slip"))
		{
			bgDesc = bgDesc.replace("Includes Slip", "Включва лента за затягане");
		}
		if(bgDesc.contains("Includes slip"))
		{
			bgDesc = bgDesc.replace("Includes slip", "Включва лента за затягане");
		}
		
		if(bgDesc.contains("Belt Included"))
		{
			bgDesc = bgDesc.replace("Belt Included", "Включва колан");
		}
		if(bgDesc.contains("Belt included"))
		{
			bgDesc = bgDesc.replace("Belt included", "Включва колан");
		}
		if(bgDesc.contains("Includes Belt"))
		{
			bgDesc = bgDesc.replace("Includes Belt", "Включва колан");
		}
		if(bgDesc.contains("Includes belt"))
		{
			bgDesc = bgDesc.replace("Includes belt", "Включва колан");
		}
		
		
		//Elasticated
		if(bgDesc.contains("Elasticated"))
		{
			bgDesc = bgDesc.replace("Elasticated", "Еластична");
		}

		return bgDesc;
	}
	
	public String bulgarianMaterial()
	{
		String bgMat = finalProductMaterial;

		
		if(bgMat.contains("Fabric "))
		{
			bgMat = bgMat.replace("Fabric", "Плат");
		}
		
		if(bgMat.contains("Fabric:"))
		{
			bgMat = bgMat.replace("Fabric:", "Плат:");
		}
		
		
		
		if(bgMat.contains("Body:"))
		{
			bgMat = bgMat.replace("Body:", "Тяло:");
		}
		
		if(bgMat.contains("Shell Fabric"))
		{
			bgMat = bgMat.replace("Shell Fabric", "Плат на лицевата страна:");
		}
		
		
		if(bgMat.contains("Lining"))
		{
			bgMat = bgMat.replace("Lining", "Обшивка");
		}
		if(bgMat.contains("Linin"))
		{
			bgMat = bgMat.replace("Linin", "Обшивка");
		}

		if(bgMat.contains("Filler"))
		{
			bgMat = bgMat.replace("Filler", "Подплата");
		}
		
		if(bgMat.contains("Cotton twill"))
		{
			bgMat = bgMat.replace("Cotton twill", "Памучна плетка");
		}
		
		if(bgMat.contains("Cotton"))
		{
			bgMat = bgMat.replace("Cotton", "Памук");
		}
		
		if(bgMat.contains("Acrylic "))
		{
			bgMat = bgMat.replace("Acrylic", "Акрил");
		}
		
		if(bgMat.contains("Nylon "))
		{
			bgMat = bgMat.replace("Nylon", "Найлон");
		}

		if(bgMat.contains("Lycra"))
		{
			bgMat = bgMat.replace("Lycra", "Ликра");
		}
		
		if(bgMat.contains("Elastane"))
		{
			bgMat = bgMat.replace("Elastane", "Еластан");
		}
		
		if(bgMat.contains("Spandex"))
		{
			bgMat = bgMat.replace("Spandex", "Еластан");
		}
		
		if(bgMat.contains("Polyester"))
		{
			bgMat = bgMat.replace("Polyester", "Полиестер");
		}
		
		if(bgMat.contains("Viscose"))
		{
			bgMat = bgMat.replace("Viscose", "Вискоза");
		}
		
		if(bgMat.contains("Hemp"))
		{
			bgMat = bgMat.replace("Hemp", "Коноп");
		}
		
		if(bgMat.contains("Wool"))
		{
			bgMat = bgMat.replace("Wool", "Вълна");
		}
		
		if(bgMat.contains("Cashmere"))
		{
			bgMat = bgMat.replace("Cashmere", "Кашмир");
		}
		
		if(bgMat.contains("Denim"))
		{
			bgMat = bgMat.replace("Denim", "Дънков плат");
		}
		
		if(bgMat.contains("Artificial Leather"))
		{
			bgMat = bgMat.replace("Artificial Leather", "Изкуствена Кожа");
		}
		if(bgMat.contains("Artificial leather"))
		{
			bgMat = bgMat.replace("Artificial leather", "Изкуствена Кожа");
		}
		if(bgMat.contains("Leather"))
		{
			bgMat = bgMat.replace("Leather", "Кожа");
		}
		
		if(bgMat.contains("Silk"))
		{
			bgMat = bgMat.replace("Silk", "Коприна");
		}
		
		if(bgMat.contains("Satin"))
		{
			bgMat = bgMat.replace("Satin", "Сатен");
		}
		
		if(bgMat.contains("Suede"))
		{
			bgMat = bgMat.replace("Suede", "Велур");
		}
		
		if(bgMat.contains("Rubber"))
		{
			bgMat = bgMat.replace("Rubber", "Гума");
		}
		
		if(bgMat.contains("Polyurethane"))
		{
			bgMat = bgMat.replace("Polyurethane", "Полиуретан");
		}
		
		
		
		return bgMat;
	}
}
