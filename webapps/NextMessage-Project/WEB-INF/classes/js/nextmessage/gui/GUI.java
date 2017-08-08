package js.nextmessage.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.IncomingPhoneNumber;

import js.nextmessage.constants.Constants;
import js.nextmessage.gui.windows.EnterKeys;
import js.nextmessage.gui.windows.Finish;
import js.nextmessage.gui.windows.ImportNumbers;
import js.nextmessage.gui.windows.Start;
import js.nextmessage.gui.windows.StartServer;
import js.nextmessage.gui.windows.Windows;
import js.nextmessage.statistics.Charts;
import js.nextmessage.statistics.Print;
import js.nextmessage.statistics.Reports;
import js.nextmessage.util.RunServerSwingWorker;
import js.nextmessage.util.Substitute;


/*
 * Description: This class manages the GUI
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class GUI extends Frame
{
	private static final long serialVersionUID = 1L;
	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private boolean fileUpdated = false;
	private String version;
	private Windows currentWindow;
	private RunServerSwingWorker<Boolean,String> worker;
	
	//Set up frame settings, initialize vars
	public GUI()
	{
		this.version = Constants.VERSION;
		
		setTitle("NextMessage v" + version);
		setResizable(false);
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(1200,600));
		setMinimumSize(new Dimension(1200,600));
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
	    setLocation(this.dim.width / 2 - getSize().width / 2, this.dim.height / 2 - getSize().height / 2);
        addWindowListener( new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent we) 
            {
            	try
				{
					Print.printAutoSaveUserList(Constants.FILE_DIRECTORY + Constants.AUTOSAVE_USER_FILE);
	            	Print.printAutoSaveInvestmentList(Constants.FILE_DIRECTORY + Constants.AUTOSAVE_INVESTMENT_FILE);
				}
            	catch (Exception e)
				{
					e.printStackTrace();
				}
            	System.exit(0);
            }
        });
        
		pack();
		setVisible(true);

	}
	
	public void init()
	{
		switchScreen("Start");
	}
	
	/*
	 * REQUIRES: Screen is a valid screen name
	 * MODIFIES: currentWindow, worker
	 * EFFECTS: Manages switching between windows, updates TextArea of StartServer window
	 */
	private void switchScreen(String screen)
	{	
		currentWindow = null;
		removeAll();
		
		switch (screen)
		{
			case "Start":
				currentWindow = new Start(version);
				break;	
			case "EnterKeys":
				currentWindow = new EnterKeys();
				break;
			case "ImportNumbers":
				if(fileUpdated)
				{
					//if session has already been recovered, then skip this step
					switchScreen("StartServer");
					return;
				}
				currentWindow = new ImportNumbers();
				break;
			case "StartServer":
				Twilio.init(Constants.ACCOUNT_SID, Constants.AUTH_TOKEN);
				IncomingPhoneNumber phoneNumber = IncomingPhoneNumber.fetcher(Constants.PHONE_SID).fetch();
				String pN = phoneNumber.getFriendlyName();
				Constants.PHONE_NUMBER = phoneNumber.getPhoneNumber().toString();
				currentWindow = new StartServer(pN);
				break;
			case "Finish":
				currentWindow = new Finish();
				break;
			default:
				System.out.println("Not a valid screen");
				System.exit(0);
		}
		
		add(currentWindow.getPanel(),"Center");
		while(currentWindow.getNext()==null)
		{
			if(screen.equals("StartServer")) //Creates the SwingWorker for Constants.service() to publish to
			{	
				worker = new RunServerSwingWorker<Boolean,String>()
				{
					@Override
					protected Boolean doInBackground()
					{
						return true;
					}
					@Override
					protected void process(List<String> stuff)
					{
						StartServer s = (StartServer)currentWindow;
						s.getTextArea().append(stuff.get(stuff.size()-1));
					}
					
					@Override
					protected void done()
					{
					}	
					
				};
				
				worker.execute();
			}
			currentWindow.getPanel().revalidate(); 
			currentWindow.getPanel().repaint();
		}
		extractInfo(screen,currentWindow.getInfo());
		switchScreen(currentWindow.getNext());
	}
	
	/*
	 * REQUIRES: s is a valid window
	 * MODIFIES: site, type, tags
	 * EFFECTS: sets global vars based on returns from window programs finishing
	 */
	private void extractInfo(String s, ArrayList<String> info)
	{
		switch (s)
		{
			case "Start":
				if(!info.isEmpty())
				{
					fileUpdated = true;
				}
				break;
			case "EnterKeys":
				Constants.ACCOUNT_SID = info.get(0);
				Constants.AUTH_TOKEN = info.get(1);
				Constants.PHONE_SID = info.get(2);
				
				break;
			case "ImportNumbers":
				try
				{
					Print.writeNumberMap(Constants.FILE_DIRECTORY + Constants.NUMBERMAP_FILE, info);
				    System.out.println("COMPANIES IMPORTED");
				}
				catch(Exception e)
				{
					e.printStackTrace();
					System.exit(0);
				}
				fileUpdated = true;
				break;
			case "StartServer":
				try
				{
					Print.printAutoSaveUserList(Constants.AUTOSAVE_USER_FILE);
					Print.printAutoSaveInvestmentList(Constants.AUTOSAVE_INVESTMENT_FILE);
					Print.printUserList(Constants.USER_LIST_CSV);
					Print.printInvestmentList(Constants.INVESTMENT_LIST_CSV);
					Reports.generateInvestorReport(Constants.USER_REPORT_FILE);
					Reports.generateFundingByCompanyReport(Constants.FUNDING_REPORT_FILE);
					Reports.generateFundingByCompanyDividedReport(Constants.FUNDING_REPORT_DIV_FILE);
					Charts.generateFundingByCompanyChart(Constants.FUNDING_CHART_FILE, false);
					Charts.generateFundingByCompanyDividedChart(Constants.FUNDING_CHART_DIV_FILE, false);
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
				break;
			case "Finish":
				System.exit(0);
				break;
			default:
				System.out.println("Not a valid screen");
				System.exit(0);
		}
	}
	
	public boolean getFileUpdated()
	{
		return fileUpdated;
	}
	
	public RunServerSwingWorker<Boolean, String> getSwingWorker()
	{
		return worker;
	}
	
}



