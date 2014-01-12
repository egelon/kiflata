import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownloader 
{
	public void downloadImageFromURL(String urlString, String folderName, String imageName)
    {
        BufferedImage image =null;
        try{
        	 File theDir = new File(folderName);
        	 URL url =new URL(urlString);
        	 // read the url
        	 image = ImageIO.read(url);
        	 
        	 // for jpg
        	 ImageIO.write(image, "jpg",new File(folderName + "/" + imageName + ".jpg"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
	
	public void downloadFile(final String urlString, final String foldername, final String filename) throws Exception
	   {
	        try
	        {
	        	URL url =new URL(urlString);
	        	File destDir = new File(foldername);
	        	destDir.mkdirs();
	            File destFile = new File(foldername + "\\" +filename);
	            
	                  
	                  // open the web page
	                  URLConnection urlconnection = null;
	                  
	                  try
	                  {
	                      urlconnection = url.openConnection();
	                  }
	                  catch (IOException ioEx)
	                  {
	                      ioEx.printStackTrace();
	                  }
	                  
	                  urlconnection.setUseCaches(true);
	                  InputStream urlStream = urlconnection.getInputStream();
	                  
	                  FileOutputStream fos = new FileOutputStream(destFile);
	                  
	                  int bytesread = 0;
	                  byte[] buffer = new byte[2048];
	                  while ((bytesread = urlStream.read(buffer)) != -1)
	                  {
	                      fos.write(buffer, 0, bytesread);
	                  }
	                  fos.close();
	                  urlStream.close();
	        }
	        catch (IOException ioe)
	        {
	            //throw (new Exception("Failed : " + ioe.getMessage()));
	            ioe.printStackTrace();
	        }
	    }
}
