package js.nextmessage.gui.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JButton;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import js.nextmessage.constants.Constants;
import js.nextmessage.exceptions.InvalidFundingException;
import js.nextmessage.statistics.Statistics;
import js.nextmessage.structs.InvestorProfile;

/*
 * Description: This class sets up the "Finish" window, which is where the user can send a text report of the companies
 * each user invested in
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class Finish extends Windows
{
	public Finish()
	{
		panel.setBackground(Color.decode("#0b2040"));
		panel.setLayout(null);
		
		JButton btn1 = new JButton("SEND REPORT TEXT");
		btn1.setFont(new Font("Roboto", Font.PLAIN, 50));
		btn1.setBackground(Color.decode("#5BC4BF"));
		btn1.setForeground(Color.decode("#0b2040"));
		btn1.setFocusPainted(false);
		btn1.setBounds(300, 175, 600, 120);
		btn1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				Twilio.init(Constants.ACCOUNT_SID, Constants.AUTH_TOKEN);
				String msg = "Thanks for participating in Talent Tech NEXT! Here is a list of the companies you invested in: ";

				ArrayList<InvestorProfile> investors = null;
				try
				{
					investors = Statistics.fundingByUser(Constants.USERMAP, Constants.INVESTMENTS);
				} 
				catch (InvalidFundingException ex)
				{
					ex.printStackTrace();
					System.exit(0);
				}

				DecimalFormat df = new DecimalFormat("#0.00");
				for (InvestorProfile inv : investors)
				{
					for (String company : inv.getInvestments().keySet())
					{
						msg += "\n    " + company + ": $" + df.format(inv.getInvestments().get(company));
					}

					String phoneNumber = inv.getUser().getPhoneNumber();
					if (phoneNumber.equals("Null"))
					{
						continue;
					}
					@SuppressWarnings("unused")
					Message message = Message
							.creator(new PhoneNumber(phoneNumber), new PhoneNumber(Constants.PHONE_NUMBER), msg).create();
				}
				
				btn1.setEnabled(false); //only send the text once, don't want spam
			}
			
		});
		panel.add(btn1);
		
		JButton btn2 = new JButton("CLOSE SERVER");
		btn2.setFont(new Font("Roboto", Font.PLAIN, 30));
		btn2.setBackground(Color.decode("#FFC720"));
		btn2.setForeground(Color.decode("#0b2040"));
		btn2.setFocusPainted(false);
		btn2.setBounds(450, 400, 300, 70);
		btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				next = "";
			}
		});
		panel.add(btn2);
	}

}
