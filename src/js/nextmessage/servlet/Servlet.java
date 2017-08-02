package js.nextmessage.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiMLException;

import js.nextmessage.exceptions.InvalidFileException;
import js.nextmessage.exceptions.InvalidNameException;
import js.nextmessage.gui.GUI;
import js.nextmessage.structs.Investment;
import js.nextmessage.structs.NumberMap;
import js.nextmessage.structs.User;
import js.nextmessage.util.Print;
import js.nextmessage.util.RunServerSwingWorker;


/*
* NextMessage Servlet Class
* Description: This manages the servlet response and initializes data structures, as well as GUI elements
* External Libraries/Resources used: Twilio/Twiml API, WindowBuilder, Apache Tomcat, Ngrok, JSoup
* 
* Author: Jaret Stillman (jrsstill@umich.edu)
* Version: 2.1
* Date: 8/1/17
*/

public class Servlet extends HttpServlet
{

	public static final String version = "2.1";
	public static final String fileDirectory = "../";
	
	private static final long serialVersionUID = 1L;

	public static HashMap<String, User> userMap; //Map of phoneNumbers to User structs
	public static ArrayList<Investment> investments; //ArrayList of unique investments
	
	private NumberMap numberMap; //Map of numbers to companies
	private HashMap<String, String> letterMap; //Map of letters to company type options
	
	private Thread guiThread;
	private GUI gui;
	private Print print;
	
	//Initialize data structures + GUI
	public void init()
	{	
		//Initialize data lists
		userMap = new HashMap<String, User>();
		investments = new ArrayList<Investment>();
		
		//Bind data to print mechanism
		print = new Print(userMap,investments);
		
		//Set output file
		setOutput(fileDirectory + "output.txt");
		
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
		
		String fileName = fileDirectory + "number_to_company.csv";
		
		//Assign numbers to each company
		try
		{
			numberMap = new NumberMap(fileName);
		}		
		catch(InvalidFileException e)
		{
			e.printStackStrace();
			System.exit(0);
		}

		//Assign letters to each company type
		letterMap = new HashMap<String, String>();
		letterMap.put("a", "Enterprise");
		letterMap.put("b", "Medium-sized business");
		letterMap.put("c", "Small business");
		letterMap.put("d", "Staffing Agency");
		letterMap.put("e", "Recruiting");
	}

	//Manage conversations, conversation state is saved in request/userMap and message is saved in response
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
		
		User user = userMap.get(phoneNumber);
		
		//Create new user if one doesn't exist with that phoneNumber
		if(user == null)
		{
			user = new User();
			userMap.put(phoneNumber,user);
			user.setPhoneNumber(phoneNumber);
		}
		
		int count = user.getNumTimesContacted();
		
		String outMsg = " ";
		String inMsg = request.getParameter("Body");
		
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
								+ ", thanks for attending TTL NEXT. Let's get you set up with your investment funds. First, what company do you work for?";
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
							+ "(a) Enterprise ( Over 1000 employees)\n"
							+ "(b) Medium sized business (100-1000 employees)\n"
							+ "(c) Small Business (0-100 employees)\n" 
							+ "(d) Staffing Agency";
					break;
				case 2:
					inMsg = inMsg.toLowerCase();
					if (inMsg.equals("a") || inMsg.equals("b") || inMsg.equals("c") || inMsg.equals("d"))
					{
						String type = letterMap.get(inMsg);
						user.setCompanyType(type);
						user.setAmount(1000.0);
						outMsg = "Great, thanks for that " + user.getFirst()
								+ "! We've put $1000 TTL Dollars in your account. As a reminder this is play money and you will never be charged for this experience. When you get to demo alley you'll see a sign on each company's table with a company number. Text the investment amount to the company's number.\n Ex. $100 to 1 or $200 to 3.\nYou can distribute your funds however you like.";
						user.setRegistered(true);
					} 
					else
					{
						count--;
						outMsg = "Please text a letter A-D";
					}
					break;
				default:
					inMsg.replace("\\$", "");
					int delimit = inMsg.indexOf(" to ");
					if (delimit == -1)
					{
						outMsg = "Sorry, we couldn't understand that request. Try using the format $amount to company_number i.e. $300 to 5";
					} 
					else
					{
						try
						{
							double amt = Double.parseDouble(inMsg.substring(0, delimit));
							int co = Integer.parseInt(inMsg.substring(delimit + 4, inMsg.length()));
							String company = numberMap.get(co);
							
							if(user.getAmount() == 0)
							{
								outMsg = "You are out of funds.";
							}
							
							else if (company == null)
							{
								outMsg = "We don't recognize that company number. Please try again with a valid number.";
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
									outMsg = "$" + amt + " invested in " + company + ".\nYou have $" + user.getAmount()
										+ " left in your account.";
									if(user.getAmount() == 0)
									{
										outMsg +=  "\nYou are now out of funds. Thanks for investing!";
									}
									
									Investment inv = new Investment(user,company,amt);
									investments.add(inv);
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
	//	@SuppressWarnings("unused")
	private void setOutput(String fileName)
	{
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
				gui = new GUI(print);
				gui.init();
			}
		};
		guiThread.start();
		
		try
		{
			Thread.sleep(5000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
