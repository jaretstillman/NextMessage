package js.nextmessage.structs;

import js.nextmessage.exceptions.InvalidNameException;

/*
 * Description: Data struct for each user
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class User 
{
	private String first;
	private String last = "";
	private String phoneNumber; 
	private String company;
	private String companyType;
	private double amount;
	private int stage;
	private boolean registered;
	private boolean canUndo;
	private double lastInvestment;
	
	public static final int STAGE_GET_NAME = 0;
	public static final int STAGE_GET_COMPANY = 1;
	public static final int STAGE_GET_COMPANY_TYPE = 2;
	public static final int STAGE_INVEST = 3;
	
	public User()
	{
		first = "Null";
		last = "Null";
		phoneNumber = "Null";
		company = "Null";
		companyType = "Null";
		amount = 0;
		stage = STAGE_GET_NAME;
		lastInvestment = 0;
		canUndo = false;
		registered = false;
	}
	
	public User(String name, String phoneNumber, String company, String companyType, double amount, int stage, boolean registered, boolean canUndo, double lastInvestment) throws InvalidNameException
	{
		this.setName(name);
		this.setPhoneNumber(phoneNumber);
		this.setCompany(company);
		this.setCompanyType(companyType);
		this.setAmount(amount);
		this.stage = stage;
		this.setRegistered(registered);
		this.setCanUndo(canUndo);
		this.setLastInvestment(lastInvestment);
	}
	
	public String getName()
	{
		return first + " " + last;
	}
	
	public void setName(String name) throws InvalidNameException
	{
		String[] firstLast = name.split(" ", 2);
		if(firstLast.length < 2)
		{
			throw new InvalidNameException();
		}
		first = firstLast[0];
		last = firstLast[1];
	}
	
	
	public String getFirst()
	{
		return first;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public String getCompanyType()
	{
		return companyType;
	}

	public void setCompanyType(String companyType)
	{
		this.companyType = companyType;
	}

	public String getCompany()
	{
		return company;
	}


	public void setCompany(String company) 
	{
		this.company = company;
	}


	public String getLast() 
	{
		return last;
	}

	public double getAmount() 
	{
		return amount;
	}


	public void setAmount(double amount) 
	{
		this.amount = amount;
	}


	public int getStage() 
	{
		return stage;
	}


	public void setStage(int stage) 
	{
		this.stage = stage;
	}

	public boolean getRegistered()
	{
		return registered;
	}

	public void setRegistered(boolean registered)
	{
		this.registered = registered;
	}
	
	public double getLastInvestment()
	{
		return lastInvestment;
	}
	
	public void setLastInvestment(double last)
	{
		this.lastInvestment = last;
	}
	
	public boolean getCanUndo()
	{
		return canUndo;
	}
	
	public void setCanUndo(boolean u)
	{
		this.canUndo = u;
	}
}
