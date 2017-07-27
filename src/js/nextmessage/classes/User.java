package js.nextmessage.classes;

/*
 * Description: Data Structure for each user
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class User 
{
	private String first;
	private String last;
	private String phoneNumber; 
	private String company;
	private String companyType;
	private double amount;
	private int numTimesContacted;
	private boolean registered = false;

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
	
	
	public String getFirst() {
		return first;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getCompanyType() {
		return companyType;
	}


	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}


	public String getCompany() {
		return company;
	}


	public void setCompany(String company) {
		this.company = company;
	}


	public String getLast() {
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


	public int getNumTimesContacted() 
	{
		return numTimesContacted;
	}


	public void setNumTimesContacted(int numTimesContacted) 
	{
		this.numTimesContacted = numTimesContacted;
	}

	public boolean getRegistered()
	{
		return registered;
	}

	public void setRegistered(boolean registered)
	{
		this.registered = registered;
	}
}
