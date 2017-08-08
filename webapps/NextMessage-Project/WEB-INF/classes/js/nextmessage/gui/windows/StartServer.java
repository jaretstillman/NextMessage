package js.nextmessage.gui.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import js.nextmessage.constants.Constants;
import js.nextmessage.statistics.Charts;
import js.nextmessage.statistics.Print;
import js.nextmessage.statistics.Reports;

/*
 * Description: This class sets up the "StartServer" window
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class StartServer extends Windows
{	
	private TextArea textArea;
	private Thread autoSave;
	public StartServer(String phoneNumber)
	{	
		ArrayList<JCheckBox> checkBoxes  = new ArrayList<JCheckBox>();
				
		textArea = new TextArea("",1,1000,TextArea.SCROLLBARS_VERTICAL_ONLY);
		textArea.setBounds(150, 75, 900, 300);
		textArea.setFont(new Font("Roboto", Font.PLAIN, 20));
		textArea.setBackground(Color.decode("#FEC92D"));
		textArea.setForeground(Color.decode("#0b2040"));
		panel.add(textArea);
			
		JCheckBox check1 = new JCheckBox("Spreadsheet of User Info");
		check1.setBounds(200, 380, 250, 25);
		panel.add(check1);
		checkBoxes.add(check1);
		
		JCheckBox check2 = new JCheckBox("User Individual Investment Report");
		check2.setBounds(450, 410, 300, 25);
		panel.add(check2);
		checkBoxes.add(check2);
		
		JCheckBox check3 = new JCheckBox("Spreadsheet of Investments");
		check3.setBounds(200, 410, 250, 25);
		panel.add(check3);
		checkBoxes.add(check3);
		
		JCheckBox check4 = new JCheckBox("Investment by Company Report");
		check4.setBounds(450, 380, 250, 25);
		panel.add(check4);
		checkBoxes.add(check4);

		JCheckBox check5 = new JCheckBox("Investment by Company Chart");
		check5.setBounds(750, 380, 250, 25);
		panel.add(check5);
		checkBoxes.add(check5);

		JCheckBox check6 = new JCheckBox("Investment by Company Report (divided by Investor Type)");
		check6.setBounds(450, 440, 450, 25);
		panel.add(check6);
		checkBoxes.add(check6);

		JCheckBox check7 = new JCheckBox("Investment by Company Chart (divided by Investor Type)");
		check7.setBounds(750, 410, 450, 25);
		panel.add(check7);
		checkBoxes.add(check7);
		
		for(JCheckBox check : checkBoxes)
		{
			check.setFont(new Font("Roboto",Font.PLAIN,15));
			check.setForeground(Color.decode("#5BC4BF"));
			check.setOpaque(false);
		}

		JButton btn1 = new JButton("GENERATE REPORTS");
		btn1.setFocusPainted(false);
		btn1.setBackground(Color.decode("#5BC4BF"));
		btn1.setForeground(Color.decode("#0B2040"));
		btn1.setFont(new Font("Roboto", Font.PLAIN, 30));
		btn1.setBounds(425, 490, 350, 57);
		panel.add(btn1);
		
		btn1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				for(int i = 0; i < checkBoxes.size(); i++)
				{
					if (checkBoxes.get(i).isSelected())
					{
						try
						{
							switch (i)
							{
								case 0:
									Print.printUserList(Constants.USER_LIST_CSV);
									break;
								case 1:
									Reports.generateInvestorReport(Constants.USER_REPORT_FILE);
									break;
								case 2:
									Print.printInvestmentList(Constants.INVESTMENT_LIST_CSV);
									break;
								case 3:
									Reports.generateFundingByCompanyReport(Constants.FUNDING_REPORT_FILE);
									break;
								case 4:
									Charts.generateFundingByCompanyChart(Constants.FUNDING_CHART_FILE, true);
									break;
								case 5:
									Reports.generateFundingByCompanyDividedReport(Constants.FUNDING_REPORT_DIV_FILE);
									break;
								case 6:
									Charts.generateFundingByCompanyDividedChart(Constants.FUNDING_CHART_DIV_FILE, true);
									break;
							}

						} 
						catch (Exception e)
						{
							e.printStackTrace();
							System.exit(0);
						}
					}
				}
			}
			
		});
		
		JLabel lbl4 = new JLabel("Phone Number: " + phoneNumber);
		lbl4.setFont(new Font("Roboto Black", Font.PLAIN, 35));
		lbl4.setBounds(350,20,600,40);
		lbl4.setForeground(Color.decode("#5BC4BF"));
		panel.add(lbl4);

		JButton btn2 = new JButton("<HTML><U>SEND A MESSAGE TO ALL USERS</U></HTML>");
		btn2.setFont(new Font("Roboto", Font.ITALIC, 20));
		btn2.setForeground(Color.decode("#FFC720"));
		btn2.setBorderPainted(false);
		btn2.setFocusPainted(false);
		btn2.setOpaque(false);
		btn2.setBackground(Color.decode("#0b2040"));
		btn2.setHorizontalTextPosition(SwingConstants.CENTER);
		btn2.setBounds(75, 490, 200, 50);
		btn2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String msg = JOptionPane.showInputDialog(panel, "Write your message here: ");
				if(msg != null)
				{
					Twilio.init(Constants.ACCOUNT_SID, Constants.AUTH_TOKEN);
					for(String phoneNumber : Constants.USERMAP.keySet())
					{					    
					    if(phoneNumber == null)
					    {
					    	continue;
					    }
					    
					    @SuppressWarnings("unused")
						Message message = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(Constants.PHONE_NUMBER), msg).create();
					}
				}
			}	
		});
		btn2.addMouseListener(new MouseListener(){
			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				btn2.setFont(new Font("Roboto Black", Font.ITALIC, 20));
			}

			@Override
			public void mouseExited(MouseEvent arg0)
			{
				btn2.setFont(new Font("Roboto", Font.ITALIC, 20));				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0){}
		});
		panel.add(btn2);
		
		startAutoSave(2);
		
		JButton btn3 = new JButton("FINISH");
		btn3.setFont(new Font("Roboto", Font.PLAIN, 25));
		btn3.setBounds(965, 490, 180, 45);
		btn3.setFocusPainted(false);
		btn3.setBackground(Color.decode("#FFC720"));
		btn3.setForeground(Color.decode("#0B2040"));
		btn3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				next = "Finish";
			}

		});
		panel.add(btn3);
		
	}

	public TextArea getTextArea()
	{
		return textArea;
	}
	
	/*
	 * MODIFIES: autosave files
	 * EFFECTS: Starts the autosave thread, which autosaves the user and investment data every n minutes
	 */
	private void startAutoSave(int n)
	{
		autoSave = new Thread()
		{
			@Override
			public void run()
			{
				// Send all user data to a file
				while (true)
				{
					try
					{
						System.out.println("AUTOSAVING...");
						Print.printAutoSaveUserList(Constants.AUTOSAVE_USER_FILE);
						Print.printAutoSaveInvestmentList(Constants.AUTOSAVE_INVESTMENT_FILE);
					} 
					catch (FileNotFoundException ex)
					{
						ex.printStackTrace();
						System.exit(0);
					}
					
					try
					{
						Thread.sleep(n * 60 * 1000); // wait n minutes
					} 
					catch (InterruptedException e)
					{
						e.printStackTrace();
						System.exit(0);
					}
				}
			}
		};
		autoSave.start();
	}
	
}
