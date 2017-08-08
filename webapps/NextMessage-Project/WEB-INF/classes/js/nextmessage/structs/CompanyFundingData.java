package js.nextmessage.structs;

import js.nextmessage.constants.Constants;
import js.nextmessage.exceptions.InvalidFundingException;
import js.nextmessage.exceptions.InvalidFundingRetrievalException;

/*
 * Description: Struct for keeping track of the different types and amount of funding a company has received
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class CompanyFundingData
{
	private double totalFunding = 0;
	private double eFunding;
	private double mbFunding;
	private double sbFunding;
	private double staffFunding;
	private double invFunding;
	private String name;
	
	public CompanyFundingData(String name)
	{
		this.name = name;
		eFunding = -0.01;
		mbFunding = -0.01;
		sbFunding = -0.01;
		staffFunding = -0.01;
		invFunding = -0.01;
	}
	
	public CompanyFundingData(String name, double funding) throws InvalidFundingException
	{	
		this.name = name;
		addFunding(funding);
		eFunding = -0.01;
		mbFunding = -0.01;
		sbFunding = -0.01;
		staffFunding = -0.01;
		invFunding = -0.01;
	}
	
	public CompanyFundingData(String name, double enterpriseFunding, double mediumSizedBusinessFunding, double smallBusinessFunding, double staffingAgencyFunding, double investorFunding) throws InvalidFundingException
	{
		this.name = name;
		eFunding = enterpriseFunding;
		mbFunding = mediumSizedBusinessFunding;
		sbFunding = smallBusinessFunding;
		staffFunding = staffingAgencyFunding;
		invFunding = investorFunding;
		check(eFunding);
		check(mbFunding);
		check(sbFunding);
		check(staffFunding);
		check(invFunding);
		totalFunding = enterpriseFunding + mediumSizedBusinessFunding + smallBusinessFunding + staffingAgencyFunding + investorFunding;
	}
	
	private void check(double funding) throws InvalidFundingException
	{
		if(funding < 0)
		{
			throw new InvalidFundingException();
		}
	}

	public double getTotalFunding()
	{
		return totalFunding;
	}
	
	public void addFunding(double funding) throws InvalidFundingException
	{
		check(funding);
		this.totalFunding += funding;
	}
	
	public void addFunding(double funding, String type) throws InvalidFundingException
	{
		check(funding);
		
		switch(type)
		{
			case "Enterprise":
				eFunding += funding;
				break;
			case "Medium-Sized Business":
				mbFunding += funding;
				break;
			case "Small Business":
				sbFunding += funding;
				break;
			case "Staffing Agency":
				staffFunding += funding;
				break;
			case "Investor":
				invFunding += funding;
				break;
			default:
				System.out.println(type + " IS NOT A COMPANY TYPE");
				System.exit(0);
		}
		
		addFunding(funding);
	}
	
	public double getEnterpriseFunding() throws InvalidFundingRetrievalException
	{
		if(eFunding < 0)
		{
			throw new InvalidFundingRetrievalException();
		}
		return eFunding;
	}
	
	public double getMediumSizedBusinessFunding() throws InvalidFundingRetrievalException
	{
		if(mbFunding < 0)
		{
			throw new InvalidFundingRetrievalException();
		}
		return mbFunding;
	}
	
	public double getSmallBusinessFunding() throws InvalidFundingRetrievalException
	{
		if(sbFunding < 0)
		{
			throw new InvalidFundingRetrievalException();
		}
		return sbFunding;
	}
	
	public double getStaffingAgencyFunding() throws InvalidFundingRetrievalException
	{
		if(staffFunding < 0)
		{
			throw new InvalidFundingRetrievalException();
		}
		return staffFunding;
	}
	
	public double getInvestorFunding() throws InvalidFundingRetrievalException
	{
		if(invFunding < 0)
		{
			throw new InvalidFundingRetrievalException();
		}
		return invFunding;
	}
	
	public String getName()
	{
		return name;
	}

	public String getEnterpriseFundingString()
	{
		if(eFunding < 0)
		{
			return "No information available";
		}
		else
		{
			return Constants.df.format(eFunding);
		}
	}

	public String getMediumSizedBusinessFundingString()
	{
		if(mbFunding < 0)
		{
			return "No information available";
		}
		else
		{
			return Constants.df.format(mbFunding);
		}
	}

	public String getSmallBusinessFundingString()
	{
		if(sbFunding < 0)
		{
			return "No information available";
		}
		else
		{
			return Constants.df.format(sbFunding);
		}
	}

	public String getStaffingAgencyFundingString()
	{
		if(staffFunding < 0)
		{
			return "No information available";
		}
		else
		{
			return Constants.df.format(staffFunding);
		}
	}
	
	public String getInvestorFundingString()
	{
		if(invFunding < 0)
		{
			return "No information available";
		}
		else
		{
			return Constants.df.format(invFunding);
		}
	}

	public String getTotalFundingString()
	{
		if(totalFunding < 0)
		{
			return "No information available";
		}
		else
		{
			return Constants.df.format(totalFunding);
		}
	}
}
