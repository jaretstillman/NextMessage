package js.nextmessage.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import js.nextmessage.structs.Investment;
import js.nextmessage.structs.User;

/*
 * Description: Utility class for managing how information is printed into csv files
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class Print
{	
	private HashMap<String,User> userMap;
	private ArrayList<Investment> investments;
	
	public Print(HashMap<String,User> userMap, ArrayList<Investment> investments)
	{
		this.userMap = userMap;
		this.investments = investments;
	}
	
	/*
	 * EFFECTS: Prints list of unique users and their information
	 */
	public void printUserList(String fileName) throws FileNotFoundException
	{	
		File file = new File(fileName);
		PrintWriter pw = new PrintWriter(file.getPath());
		StringBuilder sb = new StringBuilder();
		sb.append("NAME,PHONE_NUMBER,COMPANY,COMPANY_TYPE,AMOUNT_INVESTED_TOTAL\n");
		
		for(User user: userMap.values())
		{
			sb.append(user.getName() + "," + user.getPhoneNumber() + "," + user.getCompany() 
			+ "," + user.getCompanyType() + "," + (1000.00-user.getAmount()) + "\n");
		}
		
		pw.write(sb.toString());
		pw.close();
		pw.flush();
	}
	
	/*
	 * EFFECTS: Prints list of unique investments with user information
	 */
	public void printInvestmentList(String fileName) throws FileNotFoundException
	{
		File file = new File(fileName);
		PrintWriter pw = new PrintWriter(file.getPath());
		StringBuilder sb = new StringBuilder();
		sb.append("NAME,PHONE_NUMBER,USER_COMPANY,USER_COMPANY_TYPE,AMOUNT_INVESTED,COMPANY_INVESTED_IN\n");
		for(Investment inv : investments)
		{
			sb.append(inv.getUser().getName() + "," + inv.getUser().getPhoneNumber() + "," + inv.getUser().getCompany() 
			+ "," + inv.getUser().getCompanyType() + "," + inv.getAmt() + "," + inv.getCompany() + "\n");
		}
		
		pw.write(sb.toString());
		pw.close();
		pw.flush();
	}
}
