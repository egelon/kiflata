
public class ProductMaster 
{
	protected static int product_id = 1;
	
	protected void syncIDs()
	{
		product_id = product_id + 1;
	}
}
