package js.nextmessage.statistics;

import java.util.ArrayList;
import java.util.HashMap;


import js.nextmessage.exceptions.InvalidFundingException;
import js.nextmessage.structs.CompanyFundingData;
import js.nextmessage.structs.Investment;
import js.nextmessage.structs.InvestorProfile;
import js.nextmessage.structs.User;
import js.nextmessage.util.NumberMap;

/*
 * Description: Goes through Servlet.numberMap and Servlet.investments, aggregates data to prepare for reports/charts
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class Statistics
{
	public static ArrayList<CompanyFundingData> fundingByCompany(NumberMap numberMap, ArrayList<Investment> investments) throws InvalidFundingException
	{
		HashMap<String,CompanyFundingData> ret = new HashMap<String,CompanyFundingData>();

		for(String company : numberMap.getCompanies())
		{
			ret.put(company, new CompanyFundingData(company, 0.0));
		}
		
		for(Investment inv : investments)
		{
			CompanyFundingData cfd = ret.get(inv.getCompany());
			cfd.addFunding(inv.getAmt());
		}
		
		return new ArrayList<CompanyFundingData>(ret.values());
		
	}
	
	public static ArrayList<CompanyFundingData> fundingByCompanyDivided(NumberMap numberMap, ArrayList<Investment> investments) throws InvalidFundingException
	{
		HashMap<String, CompanyFundingData> ret = new HashMap<String,CompanyFundingData>();
		
		for(String company : numberMap.getCompanies())
		{
			ret.put(company, new CompanyFundingData(company, 0.0,0.0,0.0,0.0,0.0));
		}
		
		for(Investment inv : investments)
		{
			String type = inv.getUser().getCompanyType();
			CompanyFundingData cfd = ret.get(inv.getCompany());
			cfd.addFunding(inv.getAmt(), type);
		}

		return new ArrayList<CompanyFundingData>(ret.values());
	}
	
	public static ArrayList<InvestorProfile> fundingByUser(HashMap<String,User> userMap, ArrayList<Investment> investments) throws InvalidFundingException
	{
		//map between phone number and investorprofile
		HashMap<String,InvestorProfile> investors = new HashMap<String, InvestorProfile>();
		for(User user : userMap.values())
		{
			investors.put(user.getPhoneNumber(), new InvestorProfile(user));
		}
		
		for(Investment inv : investments)
		{
			InvestorProfile ip = investors.get(inv.getUser().getPhoneNumber());
			ip.addInvestment(inv.getCompany(), inv.getAmt());
		}
		
		return new ArrayList<InvestorProfile>(investors.values());
	}
}
