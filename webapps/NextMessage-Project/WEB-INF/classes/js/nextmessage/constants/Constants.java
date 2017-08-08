package js.nextmessage.constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import js.nextmessage.structs.Investment;
import js.nextmessage.structs.User;
import js.nextmessage.util.NumberMap;

/*
 * Description: Constants to be shared across the entire program
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class Constants
{
	public static HashMap<String, User> USERMAP; //Map of phoneNumbers to User structs
	public static ArrayList<Investment> INVESTMENTS; //ArrayList of unique investments
	public static NumberMap NUMBERMAP; //Map of numbers to companies
	
	public static final String VERSION = "2.3";
	public static final String FILE_DIRECTORY = "../REPORTS/";
	public static final double PRINCIPLE_AMOUNT = 1000.0;
	public static final DecimalFormat df = new DecimalFormat("#0.00");
	public static String ACCOUNT_SID;
	public static String AUTH_TOKEN;
	public static String PHONE_SID;
	public static String PHONE_NUMBER;
	
	public static final String AUTOSAVE_USER_FILE = FILE_DIRECTORY + "autosave_users.csv";
	public static final String AUTOSAVE_INVESTMENT_FILE =  FILE_DIRECTORY + "autosave_investments.csv";
	public static final String NUMBERMAP_FILE =  FILE_DIRECTORY + "number_to_company.csv";
	public static final String USER_LIST_CSV =  FILE_DIRECTORY + "user_info.csv";
	public static final String INVESTMENT_LIST_CSV =  FILE_DIRECTORY + "investment_info.csv";
	public static final String USER_REPORT_FILE =  FILE_DIRECTORY + "investor_info_report.txt";
	public static final String FUNDING_REPORT_FILE =  FILE_DIRECTORY + "funding_by_company_report.txt";
	public static final String FUNDING_REPORT_DIV_FILE =  FILE_DIRECTORY + "funding_by_company_divided_report.txt";
	public static final String FUNDING_CHART_FILE =  FILE_DIRECTORY + "funding_by_company_chart.png";
	public static final String FUNDING_CHART_DIV_FILE =  FILE_DIRECTORY + "funding_by_company_divided_chart.png";
	public static final String LOG_FILE = FILE_DIRECTORY + "log.txt";

}
