package js.nextmessage.classes;

/*
 * Description: Exception for invalid CSV File format/name
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class InvalidFileException extends Exception
{
	private static final long serialVersionUID = 1L;
	private String msg;
	public InvalidFileException()
	{
		msg = "The file name or format is wrong, please try again";
	}
	
	public void printStackStrace()
	{
		System.out.println(msg);
	}
}
