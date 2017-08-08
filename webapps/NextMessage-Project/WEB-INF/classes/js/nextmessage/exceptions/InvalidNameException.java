package js.nextmessage.exceptions;

/*
 * Description: Exception for invalid name format
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class InvalidNameException extends Exception
{
	private static final long serialVersionUID = 1L;
	private String msg;
	public InvalidNameException()
	{
		msg = "Invalid Name!";
	}
	public void printStackTrace()
	{
		System.out.println(msg);
	}
}
