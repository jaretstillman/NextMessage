package js.nextmessage.classes;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiMLException;


/*
* NextMessage Servlet Class
* Description: This manages the servlet response and initializes data structures, as well as GUI elements
* External Libraries/Resources used: Twilio/Twiml API, WindowBuilder, Apache Tomcat, Ngrok
* 
* Author: Jaret Stillman (jrsstill@umich.edu)
* Version: 1.0
* Date: 7/27/17
*/

public class Servlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	public static HashMap<String, User> userMap; //Map of phoneNumbers to User structs
	public static NumberMap numberMap; //Map of numbers to companies
	public static HashMap<String, String> letterMap; //Map of letters to company type options
	private JFrame frame;
	
	
	//Initialize data structures, extract ngrok, provide instructions to user, etc.
	public void init()
	{
		PrintStream out;
		try 
		{
			File f = new File("output.txt");
			f.createNewFile(); // if file already exists will do nothing 
			out = new PrintStream(new FileOutputStream(f));
			System.setOut(out);
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			System.exit(0);
		} 

		//String path = extractNgrok();
		frame = new JFrame();
		
	/*	String message = "SET UP STEPS: \n\n1. Navigate to: " + path
				+ "\n2. Click \"ngrok.exe\""
				+ "\n3. Enter \"ngrok http 8080\" in the command window that pops up"
				+ "\n4. Copy or take note of the address in the format \"xxxxx.ngrok.io\" that should show up in the window"
				+ "\n5. Navigate to https://www.twilio.com/console/phone-numbers/"
				+ "\n6. Click on the phone number and scroll down to Messaging"
				+ "\n7. Make sure that \"Configure With\" is set to WebHooks, Twiml Bins or Functions"
				+ "\n8. Make sure that \"A Message Comes In\" is set to Webhook, then copy the ngrok.io address into the text box, and add /NextMessage to the end"
				+ "\n9. Click save at the bottom"
				+ "\n10 The server is now set up"; */
		
	//	String systemType = System.getProperty("os.name");
		
		String message = "SET UP STEPS: \n\n1. Navigate to the first window that popped up, which should say \"Press any key to continue\" at the bottom"
				+ "\n2. Press any key to activate ngrok"
				+ "\n3. Copy or take note of the address in the format \"xxxxx.ngrok.io\" that should show up in the window"
				+ "\n4. Navigate to https://www.twilio.com/console/phone-numbers/"
				+ "\n5. Click on the phone number and scroll down to Messaging"
				+ "\n6. Make sure that \"Configure With\" is set to WebHooks, Twiml Bins or Functions"
				+ "\n7. Make sure that \"A Message Comes In\" is set to Webhook, then copy the ngrok.io address into the text box. Add /NextMessage right after \".io\""
				+ "\n8. Click save at the bottom"
				+ "\n9. Press \"ok\" on this dialogue box to start server";
				
		
		JOptionPane.showMessageDialog(frame, message);
		
		/* FOR LATER
		frame.setTitle("NextMessage v1.0");
		frame.setResizable(false);
		frame.setBackground(Color.WHITE);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(1200,600));
		frame.setMinimumSize(new Dimension(1200,600));
	    frame.setLocation(this.dim.width / 2 - frame.getSize().width / 2, this.dim.height / 2 - frame.getSize().height / 2);
        frame.addWindowListener( new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent we) 
            {
                System.exit(0);
            }
        } );
		frame.pack();
		*/
		
		frame.setVisible(false);
		
		
		userMap = new HashMap<String, User>();

		String fileName = getClass().getClassLoader().getResource("js/nextmessage/resources/number_to_company.csv").getPath();
		
		try
		{
			numberMap = new NumberMap(fileName);
		}
		catch(InvalidFileException e)
		{
			e.printStackStrace();
			System.exit(0);
		}

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
		System.out.println(request.getParameter("From"));
		
		HttpSession session = request.getSession(true);
		String phoneNumber = request.getParameter("From");
		User user = userMap.get(phoneNumber);
		
		if(user == null)
		{
			user = new User();
			userMap.put(phoneNumber,user);
			user.setPhoneNumber(phoneNumber);
		}
		
		Integer counter = (Integer)session.getAttribute("counter");
		
		if(counter == null)
		{
			counter = new Integer(0);
		}
		
		int count = counter.intValue();
		
		String outMsg = " ";
		String inMsg = request.getParameter("Body");
		
		try
		{
			switch (count)
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
					inMsg.replace("$", "");
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
		session.setAttribute("counter", new Integer(count));
		
		Body body = new Body(outMsg);
		Message message = new Message.Builder().body(body).build();
		MessagingResponse mr = new MessagingResponse.Builder().message(message).build();
		
		response.setContentType("text/xml");
		
        try 
        {
            response.getWriter().print(mr.toXml());
        } 
        catch (TwiMLException e) 
        {
            e.printStackTrace();
        }

	}
	
	//Extract Ngrok.exe
/*	private String extractNgrok()
	{
		try
		{
			File f = new File ("./ngrok.exe");
			if (!f.exists()) 
			{
			  try
			  {
				  Thread.sleep(3000); //wait for file to be created
			  }
			  catch(InterruptedException e)
			  {
				  e.printStackTrace();
			  }
			  
			  InputStream in = getClass().getClassLoader().getResourceAsStream("js/nextmessage/resources/ngrok.exe");
			  OutputStream out = new FileOutputStream("./ngrok.exe");
			  IOUtils.copy(in, out);
			}
			
			return f.getAbsolutePath();
		}
		catch(Exception e)
		{
			try
			{
				e.printStackTrace(new PrintStream("output.txt"));
			} 
			catch (FileNotFoundException e1)
			{
				e1.printStackTrace();
			}
			System.out.println("NGROK.EXE NOT FOUND");
			System.exit(0);
		}
		return null;
	} */
}
