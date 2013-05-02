package amazon_api_stuff;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

public class AmznTest {

	// These are special codes I had to sign up in order to get
	// They grant access to the API and indicate where to send commission when people buy stuff
	// Technically these shouldn't be shared I guess, but I trust you guys haha
	private static final String AWS_ACCESS_KEY_ID = "AKIAIPZPVGH3EPQ5S3QA";
	private static final String AWS_SECRET_KEY = "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";
	private static final String ASSOCIATE_TAG = "osmihicom-20";
	private static final String ENDPOINT = "ecs.amazonaws.com";

	private static final String ITEM_ID = "0545010225";			// a Harry Potter book, used as an example in the ItemLookup code
	
	private SignedRequestsHelper helper;
	private DocumentBuilderFactory dbf;
    private DocumentBuilder db;

	public AmznTest() {
		// Set up the signed requests helper, some code that was provideed by Amazon
        try {
            helper = SignedRequestsHelper.getInstance(ENDPOINT, AWS_ACCESS_KEY_ID, AWS_SECRET_KEY);
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
        } catch (Exception e) {return;}

        String requestUrl = null;

        // Assemble the request parameters
        Map<String, String> params = new HashMap<String, String>();
        params.put("Service", "AWSECommerceService");
        params.put("Version", "2009-03-31");
        params.put("AssociateTag", ASSOCIATE_TAG);

        // Use ItemLookup to look up a single item by its ID
//      params.put("Operation", "ItemLookup");
//      params.put("ItemId", ITEM_ID);
//      params.put("ResponseGroup", "Small");

        // Use ItemSearch to find a bunch of items by a search term
        params.put("Operation", "ItemSearch");
        params.put("Keywords", "The Pixies");		// Feel free to try searching for other stuff
        params.put("SearchIndex", "Music");			// you can also try "Books" "Video" and others
        params.put("ResponseGroup", "Small");        
        
        // Use the request helper to make the proper url.
        requestUrl = helper.sign(params);
        
        System.out.println("Signed URL: " + requestUrl); // You can copy/paste this URL into your browser to see the XML that gets returned
        System.out.println();

        try {
            Document doc = db.parse(requestUrl);
            NodeList itemList = doc.getElementsByTagName("ItemAttributes");
            for (int i = 0; i < itemList.getLength(); i++) {
            	Node k = itemList.item(i).getFirstChild();

            	do {	
            		System.out.println(k.getNodeName() + " : " + k.getTextContent());
            		k = k.getNextSibling();
            	} while (k != null);
            	
            	System.out.println();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

	}

	public static void main(String[] args) {
		AmznTest at = new AmznTest();
	}
}
