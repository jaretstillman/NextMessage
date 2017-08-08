package js.nextmessage.structs;

import java.util.HashMap;

import js.nextmessage.exceptions.InvalidFundingException;

/*
 * Description: Struct for keeping track of which investments a User has made
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class InvestorProfile
{
	private User user;
	private HashMap<String,Double> investments;
	
	public InvestorProfile(User user)
	{
		this.investments = new HashMap<String,Double>();
		this.user = user;
	}

	public HashMap<String,Double> getInvestments()
	{
		return investments;
	}

	public void addInvestment(String company, double amount) throws InvalidFundingException
	{
		if(amount<0)
		{
			throw new InvalidFundingException();
		}
		if(investments.containsKey(company))
		{
			investments.put(company, amount + investments.get(company));
		}
		else
		{	
			investments.put(company, amount);
		}
	}

	public User getUser()
	{
		return user;
	}
}
