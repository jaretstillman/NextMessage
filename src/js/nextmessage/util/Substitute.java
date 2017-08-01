package js.nextmessage.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;

/*
 * Description: This class connects the ngrok url to the Twilio account, 
 * 		opening a temporary port from your computer to the internet and routing it through
 * 		your Twilio phone number
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class Substitute
{
	private String ACCOUNT_SID;
	private String AUTH_TOKEN;
	private String PHONE_SID;
	
	/*
	 * REQUIRES: as, at and ps are each valid for the Twilio Account, Ngrok is running
	 * MODIFIES: Twilio account phone number information
	 * EFFECTS: See class description
	 */
	public void substitute(String as, String at, String ps)
	{
		ACCOUNT_SID = as;
		AUTH_TOKEN = at;
		PHONE_SID = ps;
		changeSettings(getNgrokURL());
	}
	
	/*
	 * REQUIRES: Ngrok is running
	 * EFFECTS: Returns the ngrok url
	 */
	public String getNgrokURL()
	{
		Document doc;
		try
		{
			doc = Jsoup.connect("http://localhost:4040").get();
			System.out.println("CONNECTED TO LOCALHOST");
		} 
		catch (IOException e)
		{
			return null;
		}
		Elements elts = doc.select("script[type*='text/javascript']");
		for(Element elt : elts)
		{
			if(elt.html().contains("ngrok.io"))
			{
				String s = elt.html();
				String url = s.substring(s.indexOf("URL")+8,s.indexOf("ngrok.io")+8);
				return url;
			}
		}
		return null;
	}
	
	/*
	 * REQUIRES: url is a valid ngrok url
	 * EFFECTS: Sets phone sms link to ngrok url
	 */
	public void changeSettings(String url)
	{
		if(url == null)
		{
			System.out.println("NGROK NOT INITIATED PROPERLY");
			System.exit(0);
		}
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		IncomingPhoneNumber phoneNumber = IncomingPhoneNumber.updater(PHONE_SID).setSmsUrl(url).update();	
		System.out.println("PHONE NUMBER ROUTING TO: " + phoneNumber.getSmsUrl());
	}
}
