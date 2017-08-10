package js.nextmessage.statistics;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import js.nextmessage.constants.Constants;
import js.nextmessage.structs.Investment;
import js.nextmessage.structs.User;

/*
 * Description: Utility class for managing how information is printed into csv files
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class Print
{	

	/*
	 * MODIFIES: file from filename
	 * EFFECTS: Prints list of unique users and their information
	 */
	public static void printUserList(String fileName) throws FileNotFoundException
	{	
		File file = new File(fileName);
		PrintWriter pw = new PrintWriter(file.getPath());
		StringBuilder sb = new StringBuilder();
		sb.append("NAME,PHONE_NUMBER,COMPANY,COMPANY_TYPE,AMOUNT_INVESTED_TOTAL\n");
		
		for(User user: Constants.USERMAP.values())
		{
			double amt = user.getRegistered() ? user.getAmount() : Constants.PRINCIPLE_AMOUNT; //if user is not registered, they have spent 0 money
			sb.append(user.getName().replace(",", "") + "," + user.getPhoneNumber() + "," + user.getCompany().replace(",", "") 
			+ "," + user.getCompanyType() + "," + (Constants.PRINCIPLE_AMOUNT-amt) + "\n");
		}
		
		pw.write(sb.toString());
		pw.close();
		pw.flush();
	}
	
	/*
	 * MODIFIES: file from filename
	 * EFFECTS: Prints list of unique investments with user information
	 */
	public static void printInvestmentList(String fileName) throws FileNotFoundException
	{
		File file = new File(fileName);
		PrintWriter pw = new PrintWriter(file.getPath());
		StringBuilder sb = new StringBuilder();
		sb.append("NAME,PHONE_NUMBER,USER_COMPANY,USER_COMPANY_TYPE,AMOUNT_INVESTED,COMPANY_INVESTED_IN\n");
		for(Investment inv : Constants.INVESTMENTS)
		{
			sb.append(inv.getUser().getName().replace(",", "") + "," + inv.getUser().getPhoneNumber() + "," + inv.getUser().getCompany().replace(",", "")
			+ "," + inv.getUser().getCompanyType() + "," + inv.getAmt() + "," + inv.getCompany().replace(",", "") + "\n");
		}
		
		pw.write(sb.toString());
		pw.close();
		pw.flush();
	}
	
	/*
	 * MODIFIES: file from filename
	 * EFFECTS: Writes the autosave_investment file
	 */
	public static void printAutoSaveInvestmentList(String fileName) throws FileNotFoundException
	{
		File file = new File(fileName);
		PrintWriter pw = new PrintWriter(file.getPath());
		StringBuilder sb = new StringBuilder();
		sb.append("PHONE_NUMBER,AMOUNT_INVESTED,COMPANY_INVESTED_IN\n");
		for(Investment inv : Constants.INVESTMENTS)
		{
			sb.append(inv.getUser().getPhoneNumber() + "," + inv.getAmt() + "," + inv.getCompany().replace(",", "") + "\n");
		}
		
		pw.write(sb.toString());
		pw.close();
		pw.flush();
	}
	
	/*
	 * MODIFIES: file from filename
	 * EFFECTS: Writes the autosave_user file
	 */
	public static void printAutoSaveUserList(String fileName) throws FileNotFoundException
	{
		File file = new File(fileName);
		PrintWriter pw = new PrintWriter(file.getPath());
		StringBuilder sb = new StringBuilder();
		sb.append("NAME,PHONE_NUMBER,COMPANY,COMPANY_TYPE,AMOUNT,STAGE,REGISTERED,CAN_UNDO,LAST_INVESTMENT\n");
		
		for(User user: Constants.USERMAP.values())
		{
			sb.append(user.getName().replace(",", "") + "," + user.getPhoneNumber() + "," + user.getCompany().replace(",", "") 
			+ "," + user.getCompanyType() + "," + user.getAmount() + "," + user.getStage() 
			+ "," + user.getRegistered() + "," + user.getCanUndo() + "," + user.getLastInvestment() + "\n");
		}
		
		pw.write(sb.toString());
		pw.close();
		pw.flush();
	}

	/*
	 * MODIFIES: file from filename
	 * EFFECTS: Writes companies with assigned numbers to filename
	 */
	public static void writeNumberMap(String fileName, ArrayList<String> info) throws FileNotFoundException
	{
        File file = new File(fileName);
        PrintWriter pw = new PrintWriter(file.getPath());
        StringBuilder sb = new StringBuilder();
        
        sb.append("ID");
        sb.append(',');
        sb.append("COMPANY");
        sb.append('\n');
        
		for(int i = 0; i<info.size(); i++)
		{
			
	        sb.append(new Integer(i+1).toString());
	        sb.append(',');
	        sb.append(info.get(i).replace(",", ""));
	        sb.append('\n');
		}
	       
		pw.write(sb.toString());
	    pw.close();
	    pw.flush();	    
	}
}
