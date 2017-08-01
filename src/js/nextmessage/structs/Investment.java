package js.nextmessage.structs;

/*
 * Description: Struct for keeping track of individual investments
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class Investment
{
	private User user;
	private String company;
	private double amt;
	public Investment(User u, String c, double a)
	{
		setUser(u);
		setCompany(c);
		setAmt(a);
	}
	
	public User getUser()
	{
		return user;
	}
	public void setUser(User user)
	{
		this.user = user;
	}
	public String getCompany()
	{
		return company;
	}
	public void setCompany(String company)
	{
		this.company = company;
	}
	public double getAmt()
	{
		return amt;
	}
	public void setAmt(double amt)
	{
		this.amt = amt;
	}
}
