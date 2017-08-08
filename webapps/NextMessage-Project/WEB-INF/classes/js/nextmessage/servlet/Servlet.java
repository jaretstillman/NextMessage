package js.nextmessage.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiMLException;

import js.nextmessage.constants.Constants;
import js.nextmessage.exceptions.InvalidFileException;
import js.nextmessage.exceptions.InvalidNameException;
import js.nextmessage.gui.GUI;
import js.nextmessage.structs.Investment;
import js.nextmessage.structs.User;
import js.nextmessage.util.NumberMap;
import js.nextmessage.util.RunServerSwingWorker;


/*
* NextMessage Servlet Class
* Description: This manages the servlet response and initializes data structures, as well as the GUI thread
* External Libraries/Resources used: Twilio/Twiml API, WindowBuilder, Apache Tomcat, Ngrok, JSoup, JFreeChart
* 
* Author: Jaret Stillman (jrsstill@umich.edu)
* Version: 2.2.1
* Date: 8/8/17
*/

public class Servlet extends HttpServlet
{	
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, String> letterMap; //Map of letters to company type options
	
	private Thread guiThread;
	private GUI gui;
	
	//Initialize data structures + GUI
	public void init()
	{	
		//Initialize data lists
		Constants.USERMAP = new HashMap<String, User>();
		Constants.INVESTMENTS = new ArrayList<Investment>();
		
		//Set output file
		setOutput(Constants.LOG_FILE);
		
		//Initialize GUI
		initGUI();		
			
		//wait until user has updated number_to_csv file
		while(gui.getFileUpdated() == false) 
		{
			try
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		String fileName = Constants.FILE_DIRECTORY + Constants.NUMBERMAP_FILE;
		
		//Assign numbers to each company
		try
		{
			Constants.NUMBERMAP = new NumberMap(fileName);
		}		
		catch(InvalidFileException e)
		{
			e.printStackStrace();
			System.exit(0);
		}

		//Assign letters to each company type
		letterMap = new HashMap<String, String>();
		letterMap.put("a", "Enterprise");
		letterMap.put("b", "Medium-Sized Business");
		letterMap.put("c", "Small Business");
		letterMap.put("d", "Staffing Agency");
		letterMap.put("e", "Investor");
	}

	//Manage conversations, conversation state is saved in request/Constants.USERMAP and message is saved in response
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException
	{	
		RunServerSwingWorker<Boolean,String> worker = gui.getSwingWorker();
		
		//Publish to TextArea
		worker.pub("Message received from: " + request.getParameter("From") + "\n");
		try
		{
			Thread.sleep(1000);
		} 
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		
		String phoneNumber = request.getParameter("From");
		
		User user = Constants.USERMAP.get(phoneNumber);
		
		//Create new user if one doesn't exist with that phoneNumber
		if(user == null)
		{
			user = new User();
			Constants.USERMAP.put(phoneNumber,user);
			user.setPhoneNumber(phoneNumber);
		}
		
		int count = user.getNumTimesContacted();
		
		String outMsg = " ";
		String inMsg = request.getParameter("Body");
		inMsg = removeTrailingSpaces(inMsg);
		
		//Switch the response based on inMsg and numTimesContacted
		try
		{
			switch (user.getNumTimesContacted())
			{
				case 0:
					try
					{
						user.setName(inMsg);
						outMsg = "Hi " + user.getFirst()
								+ ", thanks for attending Talent Tech NEXT. Let's get you set up with your investment funds. First, what's the name of your company?";
					} 
					catch (InvalidNameException e)
					{
						count--;
						outMsg = "Woops, we couldn't recognize that name. Try entering your name as FIRST LAST i.e. John Smith";
					}
					break;
				case 1:
					user.setCompany(inMsg);
					outMsg = "Thanks " + user.getFirst() + "! Which of the following categories does "
							+ user.getCompany() + " fit into (reply back with letter)?\n\n"
							+ "(a) Enterprise (Over 1000 employees)\n"
							+ "(b) Medium-Sized Business (100-1000 employees)\n"
							+ "(c) Small Business (0-100 employees)\n" 
							+ "(d) Staffing Agency\n"
							+ "(e) Investor";
					break;
				case 2:
					inMsg = inMsg.toLowerCase();
					if (inMsg.equals("a") || inMsg.equals("b") || inMsg.equals("c") || inMsg.equals("d") || inMsg.equals("e"))
					{
						String type = letterMap.get(inMsg);
						user.setCompanyType(type);
						user.setAmount(Constants.PRINCIPLE_AMOUNT);
						outMsg = "Great, thanks for that " + user.getFirst()
								+ "! We've put $" + Constants.df.format(round(Constants.PRINCIPLE_AMOUNT,0)) + " TTL Dollars in your account. As a reminder this is play money and you will never be charged for this experience. When you get to demo alley you'll see a sign on each company's table with a company number. Text the investment amount to the company's number.\n Ex. $100 to 1 or $200 to 3.\nYou can distribute your funds however you like." +
								"\n\n\nType \"Companies\" to see the full list of companies with their assigned numbers";
						user.setRegistered(true);
					} 
					else
					{
						count--;
						outMsg = "Please text a letter A-E";
					}
					break;
				default:
					inMsg = inMsg.replace("$", "");
					int delimit = inMsg.indexOf(" to ");
					if(inMsg.toLowerCase().equals("companies"))
					{
						for(int i : Constants.NUMBERMAP.getKeys())
						{
							outMsg += i + ": " + Constants.NUMBERMAP.get(i) + "\n";
						}
					} 
					else if (inMsg.toLowerCase().equals("undo"))
					{
						if (!user.getCanUndo())
						{
							outMsg += "Nothing to undo!";
						} 
						else
						{
							user.setCanUndo(false);
							int i = 0;
							for (i = Constants.INVESTMENTS.size() - 1; i >= 0; i--)
							{
								if (Constants.INVESTMENTS.get(i).getUser() == user)
								{
									Constants.INVESTMENTS.remove(i);
									break;
								}
							}
							if (i < 0)
							{
								outMsg += "Nothing to undo!";
							} 
							else
							{
								double last = user.getLastInvestment();
								user.setAmount(user.getAmount() + last);
								outMsg += "Last investment undone. Your account has been reset to $" + Constants.df.format(user.getAmount());
							}
						}
					}
					else if (delimit == -1)
					{
						outMsg = "Sorry, we couldn't understand that request. Try using the format $amount to company_number i.e. $300 to 5";
					} 
					else
					{
						try
						{
							double amt = Double.parseDouble(inMsg.substring(0, delimit));
							amt = round(amt,2);
							
							int co = Integer.parseInt(inMsg.substring(delimit + 4, inMsg.length()));
							String company = Constants.NUMBERMAP.get(co);
							
							if(user.getAmount() == 0)
							{
								outMsg = "You are out of funds.";
							}
							
							else if (company == null)
							{
								outMsg = "We don't recognize that company number. Please try again with a valid number.";
							}
							
							else if (amt <= 0)
							{
								outMsg = "Please enter a positive amount";
							}
							
							else
							{
								double newAmt = user.getAmount() - amt;
								if (newAmt < 0)
								{
									outMsg = "Insufficient funds for that investment, please try again";
								}
								else
								{
									user.setAmount(newAmt);
									outMsg = "$" + Constants.df.format(amt) + " invested in " + company + ".\nYou have $" + Constants.df.format(user.getAmount())
										+ " left in your account.";
									if(user.getAmount() == 0)
									{
										outMsg +=  "\nYou are now out of funds. Thanks for investing!";
									}
									
									outMsg +=  "\nType \"Undo\" to undo this investment.";
									
									Investment inv = new Investment(user,company,amt);
									Constants.INVESTMENTS.add(inv);
									
									user.setLastInvestment(amt);
									user.setCanUndo(true);
								}
							}
						} 
						catch (NumberFormatException e)
						{
							outMsg = "Sorry, we couldn't understand that request. Try using the format $AMOUNT to COMPANY_NUMBER i.e. $300 to 5";
						}
					}
			}
		} 
		catch (Exception e) //NullPointerException most likely
		{
			count--;
			outMsg = "Error Detected, Please Try Again";
		}
		
		count++;
		user.setNumTimesContacted(count); //Update numTimesContacted
		
		Body body = new Body(outMsg);
		Message message = new Message.Builder().body(body).build();
		MessagingResponse mr = new MessagingResponse.Builder().message(message).build();
		
		response.setContentType("text/xml");
		
		//Send response
        try 
        {
            response.getWriter().print(mr.toXml());
        } 
        catch (TwiMLException e) 
        {
            e.printStackTrace();
        }

	}
	
	//Sends std out and std err to fileName
	private void setOutput(String fileName)
	{
		File file = new File(Constants.FILE_DIRECTORY);
		try
		{
			if(!file.exists())
			{
				file.mkdir();
			}
		} 
		catch (SecurityException ex)
		{
			ex.printStackTrace();
		}
		
		PrintStream out;
		try 
		{
			File f = new File(fileName);
			f.createNewFile(); // if file already exists will do nothing 
			out = new PrintStream(new FileOutputStream(f));
			System.setOut(out);
			System.setErr(out);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(0);
		} 
	}
	
	//Initialize GUI and guiThread
	private void initGUI()
	{
		guiThread = new Thread()
		{
			public void run()
			{
				System.out.println("STARTING GUI");
				gui = new GUI();
				gui.init();
			}
		};
		guiThread.start();
		
		try
		{
			Thread.sleep(5000);
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	//Round value to certain number of places
	private double round(double value, int places) 
	{
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	private String removeTrailingSpaces(String s)
	{
		if(s == null)
		{
			return null;
		}
		for(int i = s.length()-1; i>=0; i--)
		{
			if(!Character.isWhitespace(s.charAt(i)))
			{
				break;
			}
			s = s.substring(0,i);
		}
		return s;
	}
}
