package js.nextmessage.gui.windows;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import js.nextmessage.util.Print;

/*
 * Description: This class sets up the "StartServer" window
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class StartServer extends Windows
{	
	private TextArea textArea;
	public StartServer(Print print)
	{	
		
		textArea = new TextArea();
		textArea.setBounds(250, 50, 700, 350);
		textArea.setFont(new Font("Roboto", Font.PLAIN, 20));
		textArea.setBackground(Color.decode("#FEC92D"));
		textArea.setForeground(Color.decode("#0b2040"));
		panel.add(textArea);
			
		Button btn1 = new Button("PRINT LIST OF USERS");
		btn1.setFont(new Font("Roboto", Font.PLAIN, 20));
		btn1.setBackground(Color.decode("#5BC4BF"));
		btn1.setForeground(Color.decode("#0b2040"));
		btn1.setBounds(250, 465, 300, 60);
		panel.add(btn1);
		btn1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					print.printUserList("../user_list.csv");
				} 
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
					System.exit(0);
				}
			}
		});
		
		Button btn2 = new Button("PRINT LIST OF INVESTMENTS");
		btn2.setFont(new Font("Roboto", Font.PLAIN, 20));
		btn2.setBackground(Color.decode("#5BC4BF"));
		btn2.setForeground(Color.decode("#0b2040"));
		btn2.setBounds(650, 465, 300, 60);
		btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					print.printInvestmentList("../investment_list.csv");
				} 
				catch (FileNotFoundException e)
				{
					e.printStackTrace();
					System.exit(0);
				}
			}
		});
		panel.add(btn2);
	}

	public TextArea getTextArea()
	{
		return textArea;
	}
	
}
