package js.nextmessage.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import js.nextmessage.gui.windows.EnterKeys;
import js.nextmessage.gui.windows.ImportNumbers;
import js.nextmessage.gui.windows.Start;
import js.nextmessage.gui.windows.StartServer;
import js.nextmessage.gui.windows.Windows;
import js.nextmessage.util.Print;
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
	private Print print;
	private Windows currentWindow;
	private String AS,AT,PS;
	private RunServerSwingWorker<Boolean,String> worker;
	
	//Set up frame settings, initialize vars
	public GUI(String version, Print print)
	{
		this.version = version;
		this.print = print;
		
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
                System.exit(0);
            }
        } );
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
				currentWindow = new ImportNumbers();
				break;
			case "StartServer":
				currentWindow = new StartServer(print);
				break;
			default:
				System.out.println("Not a valid screen");
				System.exit(0);
		}
		
		add(currentWindow.getPanel(),"Center");
		while(currentWindow.getNext()==null)
		{
			if(screen.equals("StartServer")) //Creates the SwingWorker for Servlet.service() to publish to
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
				break;
			case "EnterKeys":
				AS = info.get(0);
				AT = info.get(1);
				PS = info.get(2);
				
				//Connect Twilio to Tomcat Server through Ngrok
				Substitute sub = new Substitute();
				sub.substitute(AS, AT, PS);
				
				break;
			case "ImportNumbers":
				try
				{
					writeCSV(info);
				}
				catch(Exception e)
				{
					e.printStackTrace();
					System.exit(0);
				}
				fileUpdated = true;
				break;
			case "StartServer":
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
	
	/*
	 * REQUIRES: number_to_company.csv exists in resources folder
	 * MODIFIES: number_to_company.csv
	 * EFFECTS: Writes companies to number_to_company.csv
	 */
	private void writeCSV(ArrayList<String> info) throws FileNotFoundException
	{
        File file = new File(getClass().getClassLoader().getResource("js/nextmessage/resources/number_to_company.csv").getPath());
        PrintWriter pw = new PrintWriter(file.getPath());
        StringBuilder sb = new StringBuilder();
        
        sb.append("ID");
        sb.append(',');
        sb.append("COMPANY");
        sb.append('\n');
        
		for(int i = 0; i<info.size(); i++)
		{
			
	        sb.append(new Integer(i+1).toString());
	        sb.append(',');
	        sb.append(info.get(i));
	        sb.append('\n');
		}
	       
		pw.write(sb.toString());
	    pw.close();
	    pw.flush();
	    
	    System.out.println("COMPANIES IMPORTED");
	}
	
	public RunServerSwingWorker<Boolean, String> getSwingWorker()
	{
		return worker;
	}
	
}



