package js.nextmessage.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import js.nextmessage.exceptions.InvalidFileException;
import js.nextmessage.exceptions.InvalidNameException;
import js.nextmessage.constants.Constants;
import js.nextmessage.structs.Investment;
import js.nextmessage.structs.User;


/*
 * Description: This class reads in autosave reports to restore save data in the event of a crash
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class Recovery
{
	
	/*
	 * MODIFIES: Constants.userMap
	 * EFFECTS: Reads in the autosave_user file and assigns the data to Constants.userMap
	 */
	public static void recoverUserData(String fileName) throws NumberFormatException, InvalidNameException, IOException
	{		
		String line = "";
		String csvSplitBy = ",";
		int i = 0;
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while ((line = br.readLine()) != null)
		{
			// skip the first line
			if (i == 0) 
			{
				++i;
				continue;
			}
			// use comma as separator
			String[] row = line.split(csvSplitBy);
		
			//userMap has been initialized
			User user = new User(row[0],row[1],row[2],row[3],Double.parseDouble(row[4]),Integer.parseInt(row[5]), Boolean.parseBoolean(row[6]),Boolean.parseBoolean(row[7]),Double.parseDouble(row[8]));
			Constants.USERMAP.put(row[1], user);
		}
		
		br.close();
	}
	
	
	/*
	 * REQUIRES: userMap has been recovered already
	 * MODIFIES: Constants.investments
	 * EFFECTS: reads in autosave_investment file and assigns the data to Constants.investments
	 */
	public static void recoverInvestmentData(String fileName) throws NumberFormatException, IOException
	{
		String line = "";
		String csvSplitBy = ",";
		int i = 0;
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while ((line = br.readLine()) != null)
		{
			// skip the first line
			if (i == 0) 
			{
				++i;
				continue;
			}
			// use comma as separator
			String[] row = line.split(csvSplitBy);
		
			//userMap has been initialized
			User u = Constants.USERMAP.get(row[0]);
			Investment inv = new Investment(u, row[2], Double.parseDouble(row[1]));
			Constants.INVESTMENTS.add(inv);
		}
		
		br.close();
	}
	
	public static void recoverNumberMap(String fileName) throws InvalidFileException
	{
		Constants.NUMBERMAP = new NumberMap(fileName);
	}
}
