
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;




public class WebCrawler {
	
	static ArrayList<String> nameArr;
	
	
	public static void main(String[] args) {	
		
		HashMap<String, String> searchResultMap = new HashMap<String, String>();

		String basePage = "https://www.packagingproducts.co.nz/products/";
		String subPage = "bags/";
		int NUMPRODPERPAGE = 20;
		
		ArrayList<String> categoryArr = new ArrayList<String>();
		nameArr = new ArrayList<String>();
		
//        JSONParser parser = new JSONParser();
//
//        try {
//        	FileReader fr = new FileReader("retrieveJson.json");
//        	Object obj = parser.parse(fr);
//        	fr.close();
//        	
//        	JSONArray arr = (JSONArray) obj;
//        	
//        	System.out.println(arr.get(0).toString());
//        	
//        	for (int i = 0; i < arr.size(); i++) {
//        		JSONObject jsonObj = (JSONObject) arr.get(i);
//        		if(jsonObj.containsKey("sugar")) {
//	        		if (Double.parseDouble((String)jsonObj.get("sugar")) == 0.0) {
//	        			nameArr.add((String) jsonObj.get("name"));
//	        			System.out.println((String) jsonObj.get("name"));
//	        		}
//        		}
//        	}
//        	
//        }catch(Exception e) {
//        	System.out.println("couldnt suss the json");
//        }
		categoryArr.add("miscellaneous/");
		categoryArr.add("boxes-cases-and-cartons/");
		categoryArr.add("food-service-and-catering/");
		categoryArr.add("health-and-safety/");
		categoryArr.add("labels-and-tags/");
		categoryArr.add("machinery-and-equipment/");
		categoryArr.add("bags/");
		categoryArr.add("office-and-marking/");
		categoryArr.add("paper-products/");
		categoryArr.add("polythene-and-film/");
		categoryArr.add("protective-packaging/");
		categoryArr.add("retail-and-gift/");
		categoryArr.add("strapping-and-bundling/");
		categoryArr.add("tapes-and-adhesives/");
		categoryArr.add("washroom-and-cleaning/");
		
		
		
        try {
        	
        	for (int i = 0; i < categoryArr.size(); i++) {
        		
    		subPage = categoryArr.get(i);
        	
        	int numOfPages = 0;
        	
        	System.out.println(basePage + subPage); 
        	Document doc1 = Jsoup.connect(basePage + subPage).get();
        	
        	
        	Elements numOfProd = doc1.selectFirst(".pagination").select("a");
        	int counter = 0;
        	int index = 0;
        	for(Element boxes: numOfProd) {
        		
        		if(boxes.html().equals("Next")) {
        			index = counter - 1;
        		}else {
        		counter++;
        		}
        		
        	}
        	
        	Element pageBox = numOfProd.get(index);
        	
        	numOfPages = Integer.parseInt(pageBox.html());
        	
        	
        	System.out.println(numOfPages);
        	
        	
        	HashMap<String, String> arr = getAllProducts(doc1);
        	
        	for (int j = 2; j < numOfPages; j++) {
        		Document doc2 = Jsoup.connect(basePage + subPage + "?start=" + j*NUMPRODPERPAGE).get();
        		arr.putAll(getAllProducts(doc2));
        		System.out.println("New page completed");
        		Thread.sleep(1500);
        		
        	}
        	
        	System.out.println(arr.size());
        	
        	List<String> codes = new ArrayList<String>(arr.keySet());
        	List<String> URLs = new ArrayList<String>(arr.values());
        	
        	
        	for (int j = 0; j < arr.size(); j++) {
        		try {
	        		System.out.println("https://www.packagingproducts.co.nz" + URLs.get(j));
	        		Document doc3 = Jsoup.connect("https://www.packagingproducts.co.nz" + URLs.get(j)).get();
	        		System.out.println("Connected");
//	        		ArrayList<String> arr2 = getAllData(doc3, codes.get(j));
	        		insertFoodData(getAllData(doc3, codes.get(j)));
	        		Thread.sleep(2000);
	        		
        		}catch(Exception e) {
        			System.out.println("Failed: " + codes.get(j));
        		}
        		
        		}          
        	}
        	


        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
        
		
		
        
	}
	
	
	public static HashMap<String, String> getAllProducts(Document doc) {
		
		HashMap<String,String> arr = new HashMap<String,String>();
		
		Elements URLs = doc.select(".list_item");
		Elements Codes = doc.select(".product_meta_code");
		
		int counter = 0;
		
		for(Element item:URLs) {
			String productCode = Codes.get(counter).html();
			
			String productURL = URLs.get(counter).attr("data-target");
			arr.put(productCode, productURL);
			counter++;
		}
		return arr;
	}
	
	
	
	
	public static ArrayList<String> getAllData(Document doc, String code) {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add(code);
		
		 String title = doc.select("h1").first().html();
		 System.out.println(title);
         String imageURL = doc.selectFirst(".product_image").selectFirst("img").attr("src");
         
         arr.add(title);
         arr.add(imageURL);
         
         //code 0
         //title 1
         //imageURL 2
         
         System.out.println("Found: " + arr.toString());
		
		return arr;
	}
	
	public static void insertFoodData(ArrayList<String> arr) {
		
		
		String url = "http://kate.ict.op.ac.nz/~toolnj1/PPL/insertProduct.php";
	

        CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
		HttpPost httpPost = new HttpPost(url);
		String JSON = "";
		
		JSON = "{\"productInsert\": true, \"products\": [{\"code\": \""+arr.get(0)+"\",\"title\": \""+ arr.get(1)+ "\", \"imgURL\": \""+ arr.get(2) + "\" }]}";         

		StringEntity entity = new StringEntity(JSON);
		
		System.out.println(JSON);
		
		httpPost.setEntity(entity);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-type", "application/json");
		
		CloseableHttpResponse response2 = httpclient.execute(httpPost);

		    System.out.println(response2.getStatusLine());
		    HttpEntity entity2 = response2.getEntity();
		
		    response2.close();
		}catch(Exception e) {
			
		}
		
		System.out.println("Added product");
		
	}

}
