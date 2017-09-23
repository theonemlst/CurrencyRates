package com.example.a1.currencyrates;

import org.json.*;
import java.io.*;
import java.net.*;

class GetJson {

	public String getRate() {
		
		URL url;
		InputStream is = null;
		BufferedReader br = null;
		String line;
		String price = "";
		String result = "";
	//	PrintWriter os = null;

		try {
			url = new URL("https://api.cryptonator.com/api/ticker/eth-usd");
			is = url.openStream();  // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));

			while ((line = br.readLine()) != null) {								
				result += line;
			}
		} catch (MalformedURLException mue) {
			 mue.printStackTrace();
		} catch (IOException ioe) {
			 ioe.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
		try {
			JSONObject obj = new JSONObject(result);
			price = obj.getJSONObject("ticker").getString("price");
		} catch (JSONException exc) {}

		return price;
	}

}