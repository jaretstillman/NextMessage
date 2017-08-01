package js.nextmessage.gui.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/*
 * Description: This class sets up the "Start" window
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class Start extends Windows
{

	public Start(String version)
	{	
		JLabel lbl1 = new JLabel("NextMessage");
		lbl1.setFont(new Font("Roboto", Font.BOLD, 95));
		lbl1.setForeground(Color.decode("#5BC4BF"));
		lbl1.setBounds(300, 100, 600, 150);
		panel.add(lbl1);
		
		JLabel lbl2 = new JLabel("Developed by Jaret Stillman");
		lbl2.setFont(new Font("Roboto", Font.ITALIC, 40));
		lbl2.setForeground(Color.decode("#5BC4BF"));
		lbl2.setBounds(350, 229, 500, 50);
		panel.add(lbl2);
		
		JLabel lbl3 = new JLabel("Version " + version);
		lbl3.setFont(new Font("Roboto", Font.PLAIN, 21));
		lbl3.setForeground(Color.decode("#5BC4BF"));
		lbl3.setBounds(1070, 535, 106, 26);
		panel.add(lbl3);
		
		JLabel lbl4 = new JLabel("Developed for Talent Tech Next");
		lbl4.setFont(new Font("Roboto", Font.PLAIN, 21));
		lbl4.setForeground(Color.decode("#5BC4BF"));
		lbl4.setBounds(882, 492, 294, 26);
		panel.add(lbl4);
		
		JLabel lbl5 = new JLabel("A Twilio Application");
		lbl5.setFont(new Font("Roboto",Font.PLAIN,21));
		lbl5.setForeground(Color.decode("#5BC4BF"));
		lbl5.setBounds(990,513,186,26);
		panel.add(lbl5);
		
		JButton btn1 = new JButton("PRESS TO START");
		
		btn1.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) {
				next="EnterKeys";
			}
		});
		
		btn1.setFont(new Font("Roboto", Font.BOLD, 30));
		btn1.setBounds(450,350,300,100);
		btn1.setBackground(Color.decode("#5BC4BF"));
		btn1.setFocusPainted(false);
		btn1.setForeground(Color.decode("#0B2040"));
		panel.add(btn1);
		
		ImageIcon icon = createImageIcon("js/nextmessage/resources/TTL_logo.png");
		JLabel logo = new JLabel(icon);
		logo.setBounds(50,450,100,100);
		panel.add(logo);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	private ImageIcon createImageIcon(String path)
	{
		java.net.URL imgURL = getClass().getClassLoader().getResource(path);
		if (imgURL != null)
		{
			return new ImageIcon(imgURL);
		} 
		else
		{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
	
}
