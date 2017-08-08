package js.nextmessage.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import js.nextmessage.exceptions.InvalidFileException;

/*
 * Description: This is a mapping of number to company. This is done to prevent user spelling errors.
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class NumberMap 
{
	private HashMap<Integer,String> map;
	
	public NumberMap(String csv) throws InvalidFileException
	{
		map = new HashMap<Integer,String>();
		try
		{
			importCSV(csv);
		} 
		catch (Exception e)
		{
			throw new InvalidFileException();
		}
	}
	
	public void importCSV(String fileName) throws IOException, NumberFormatException
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
			Integer n = new Integer(row[0]);
			map.put(n, row[1]);
		}
		
		br.close();
	}
	
	public String get(int number)
	{
		return map.get(number);
	}
	
	public ArrayList<String> getCompanies()
	{
		return new ArrayList<String>(map.values());
	}
	
	public int size()
	{
		return map.size();
	}
	
	public ArrayList<Integer> getKeys()
	{
		return new ArrayList<Integer>(map.keySet());
	}
	
	public ArrayList<String> getValues()
	{
		return new ArrayList<String>(map.values());
	}
}
