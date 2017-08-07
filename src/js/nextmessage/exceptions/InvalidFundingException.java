package js.nextmessage.exceptions;

/*
 * Description: Exception for negative funding
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class InvalidFundingException extends Exception
{
	private static final long serialVersionUID = 1L;
	private String msg;
	public InvalidFundingException()
	{
		msg = "Funding can't be negative!";
	}
	public void printStackTrace()
	{
		System.out.println(msg);
	}
}