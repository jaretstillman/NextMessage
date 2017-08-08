package js.nextmessage.exceptions;

/*
 * Description: Exception for trying to access funding data that has not been set yet
 */

public class InvalidFundingRetrievalException extends Exception
{
	private static final long serialVersionUID = 1L;
	private String msg;
	public InvalidFundingRetrievalException()
	{
		msg = "Funding can't be negative!";
	}
	public void printStackTrace()
	{
		System.out.println(msg);
	}
}
