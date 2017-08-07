package js.nextmessage.statistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import js.nextmessage.constants.Constants;
import js.nextmessage.exceptions.InvalidFundingException;
import js.nextmessage.exceptions.InvalidFundingRetrievalException;
import js.nextmessage.structs.CompanyFundingData;
import js.nextmessage.structs.InvestorProfile;

/*
 * Description: Generates different reports from aggregated Statistics class data
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class Reports
{		
	public static void generateFundingByCompanyReport(String fileName) throws InvalidFundingException, FileNotFoundException
	{
		File file = new File(fileName);
		PrintWriter pw = new PrintWriter(file.getPath());
		StringBuilder sb = new StringBuilder();
		ArrayList<CompanyFundingData> fundingData = Statistics.fundingByCompany(Constants.NUMBERMAP, Constants.INVESTMENTS);
		double sum = 0.0;
		sb.append("TOTAL MONEY INVESTED BY COMPANY: \r\n\r\n");
		for(CompanyFundingData cfd : fundingData)
		{
			sb.append(cfd.getName() + ": " + "$" + (cfd.getTotalFundingString()) + "\r\n");
			sum +=  cfd.getTotalFunding();
		}
		sb.append("\r\nTOTAL INVESTED: $" + Constants.df.format(sum));
		
		pw.write(sb.toString());
		pw.close();
		pw.flush();		
	}
	
	public static void generateFundingByCompanyDividedReport(String fileName) throws FileNotFoundException, InvalidFundingException
	{
		File file = new File(fileName);
		PrintWriter pw = new PrintWriter(file.getPath());
		StringBuilder sb = new StringBuilder();
		
		ArrayList<CompanyFundingData> fundingData = Statistics.fundingByCompanyDivided(Constants.NUMBERMAP, Constants.INVESTMENTS);
		double tSum = 0.0;
		double eSum = 0.0;
		double mSum = 0.0;
		double sSum = 0.0;
		double iSum = 0.0;
		double stSum = 0.0;
		sb.append("FUNDING BY COMPANY: \r\n\r\n");
		for(CompanyFundingData cfd : fundingData)
		{

			sb.append(cfd.getName() + ":\r\n    Enterprise: " + "$" + cfd.getEnterpriseFundingString() + "\r\n");
			sb.append("    Medium-Sized Businesses: " + "$" + cfd.getMediumSizedBusinessFundingString() + "\r\n");
			sb.append("    Small Businesses: " + "$" + cfd.getSmallBusinessFundingString() + "\r\n");
			sb.append("    Staffing Agencies: " + "$" + cfd.getStaffingAgencyFundingString() + "\r\n");
			sb.append("    Investors: " + "$" + cfd.getInvestorFundingString() + "\r\n");
			sb.append("    Total: " + "$" + cfd.getTotalFundingString() + "\r\n");
			
			try
			{
				eSum += cfd.getEnterpriseFunding();
				mSum += cfd.getMediumSizedBusinessFunding();
				sSum += cfd.getSmallBusinessFunding();
				iSum += cfd.getInvestorFunding();
				stSum += cfd.getStaffingAgencyFunding();
			} 
			catch (InvalidFundingRetrievalException ex)
			{
				ex.printStackTrace();
				// Get out of normal control flow (no info available for subfunding sections
			}
			
			tSum += cfd.getTotalFunding();
		}
		
		sb.append("\r\nENTERPRISE TOTAL INVESTED: $" + Constants.df.format(eSum));
		sb.append("\r\nMEDIUM-SIZED BUSINESSES TOTAL INVESTED: $" + Constants.df.format(mSum));
		sb.append("\r\nSMALL BUSINESSES INVESTED: $" + Constants.df.format(sSum));
		sb.append("\r\nSTAFFING AGENCIES TOTAL INVESTED: $" + Constants.df.format(stSum));
		sb.append("\r\nINVESTORS TOTAL INVESTED: $" + Constants.df.format(iSum));
		sb.append("\r\nTOTAL INVESTED: $" + Constants.df.format(tSum));
		
		pw.write(sb.toString());
		pw.close();
		pw.flush();	
	}
	
	public static void generateInvestorReport(String fileName) throws FileNotFoundException, InvalidFundingException
	{
		File file = new File(fileName);
		PrintWriter pw = new PrintWriter(file.getPath());
		StringBuilder sb = new StringBuilder();
		
		ArrayList<InvestorProfile> investorData = Statistics.fundingByUser(Constants.USERMAP, Constants.INVESTMENTS);
		sb.append("USER INVESTMENT DATA: \r\n\r\n");
		
		double sum = 0.0;
		
		for(InvestorProfile ip : investorData)
		{
			double it = 0.0;
			
			sb.append(ip.getUser().getName() + ":\r\n  Company: " + ip.getUser().getCompany() + "\r\n  Company Type: " + ip.getUser().getCompanyType() + "\r\n");
			sb.append("  Investments:\r\n");
			for(String co : ip.getInvestments().keySet())
			{
				sb.append("    " + co + ": $" + Constants.df.format(ip.getInvestments().get(co)) + "\r\n");
				it += ip.getInvestments().get(co);
			}
			sb.append("    TOTAL: $" + Constants.df.format(it) + "\r\n");
			sum += it;
		}
		sb.append("\r\nTOTAL INVESTED: $" + Constants.df.format(sum));
		
		pw.write(sb.toString());
		pw.close();
		pw.flush();
	}
}
