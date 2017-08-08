package js.nextmessage.exceptions;

/*
 * Description: Exception for invalid ngrok configuration (either not open or not working)
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class NgrokNotConfiguredException extends Exception
{
	private static final long serialVersionUID = 1L;
	private String msg;
	public NgrokNotConfiguredException()
	{
		msg = "Ngrok not configured correctly!";
	}
	public void printStackTrace()
	{
		System.out.println(msg);
	}
}