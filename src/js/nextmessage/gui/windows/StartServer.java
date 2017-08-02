package js.nextmessage.gui.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JLabel;

import js.nextmessage.servlet.Servlet;
import js.nextmessage.util.Print;

/*
 * Description: This class sets up the "StartServer" window
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class StartServer extends Windows
{	
	private TextArea textArea;
	public StartServer(Print print, String phoneNumber)
	{	
		
		textArea = new TextArea();
		textArea.setBounds(250, 75, 700, 350);
		textArea.setFont(new Font("Roboto", Font.PLAIN, 20));
		textArea.setBackground(Color.decode("#FEC92D"));
		textArea.setForeground(Color.decode("#0b2040"));
		panel.add(textArea);
			
		JButton btn1 = new JButton("PRINT LIST OF USERS");
		btn1.setFont(new Font("Roboto Black", Font.PLAIN, 20));
		btn1.setBackground(Color.decode("#5BC4BF"));
		btn1.setForeground(Color.decode("#0b2040"));
		btn1.setFocusPainted(false);
		btn1.setBounds(250, 470, 325, 60);
		panel.add(btn1);
		btn1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					print.printUserList(Servlet.fileDirectory + "user_list.csv");
				} 
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
					System.exit(0);
				}
			}
		});
		
		JButton btn2 = new JButton("PRINT LIST OF INVESTMENTS");
		btn2.setFocusPainted(false);
		btn2.setFont(new Font("Roboto Black", Font.PLAIN, 20));
		btn2.setBackground(Color.decode("#5BC4BF"));
		btn2.setForeground(Color.decode("#0b2040"));
		btn2.setBounds(625, 470, 325, 60);
		btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					print.printInvestmentList(Servlet.fileDirectory + "investment_list.csv");
				} 
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
					System.exit(0);
				}
			}
		});
		panel.add(btn2);
		
		JLabel lbl4 = new JLabel("Phone Number: " + phoneNumber);
		lbl4.setFont(new Font("Roboto Black", Font.PLAIN, 35));
		lbl4.setBounds(350,20,600,40);
		lbl4.setForeground(Color.decode("#5BC4BF"));
		panel.add(lbl4);
	}

	public TextArea getTextArea()
	{
		return textArea;
	}
	
}
