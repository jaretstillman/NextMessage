package js.nextmessage.gui.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import js.nextmessage.exceptions.NgrokNotConfiguredException;
import js.nextmessage.util.Substitute;

/*
 * Description: This class sets up the "EnterKeys" window
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class EnterKeys extends Windows
{

	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	public EnterKeys()
	{
		textField = new JTextField();
		textField.grabFocus();
		textField.setFont(new Font("Roboto", Font.PLAIN, 25));
		textField.setBounds(350, 120, 500, 50);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Roboto", Font.PLAIN, 25));
		textField_1.setColumns(10);
		textField_1.setBounds(350, 220, 500, 50);
		panel.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Roboto", Font.PLAIN, 25));
		textField_2.setColumns(10);
		textField_2.setBounds(350, 320, 500, 50);
		panel.add(textField_2);
		
		JButton btn1 = new JButton("Continue");
		btn1.setFont(new Font("Roboto", Font.PLAIN, 30));
		btn1.setBounds(500, 434, 200, 58);
		btn1.setFocusPainted(false);
		btn1.setBackground(Color.decode("#5BC4BF"));
		btn1.setForeground(Color.decode("#0b2040"));
		panel.add(btn1);
		
		JLabel lbl1 = new JLabel("ACCOUNT_SID: ");
		lbl1.setHorizontalAlignment(SwingConstants.CENTER);
		lbl1.setForeground(Color.decode("#FFC720"));
		lbl1.setFont(new Font("Roboto", Font.BOLD, 20));
		lbl1.setBounds(181, 135, 157, 16);
		panel.add(lbl1);
		
		JLabel lbl2 = new JLabel("AUTH TOKEN: ");
		lbl2.setHorizontalAlignment(SwingConstants.CENTER);
		lbl2.setForeground(Color.decode("#FFC720"));
		lbl2.setFont(new Font("Roboto", Font.BOLD, 20));
		lbl2.setBounds(190, 235, 148, 16);
		panel.add(lbl2);
		
		JLabel lbl3 = new JLabel("PHONE_SID: ");
		lbl3.setHorizontalAlignment(SwingConstants.CENTER);
		lbl3.setForeground(Color.decode("#FFC720"));
		lbl3.setFont(new Font("Roboto", Font.BOLD, 20));
		lbl3.setBounds(208, 335, 130, 16);
		panel.add(lbl3);
		
		JButton btn2 = new JButton("<HTML><U>HELP</U></HTML>");
		btn2.setFont(new Font("Roboto", Font.ITALIC, 30));
		btn2.setBackground(Color.decode("#0b2040"));
		btn2.setBorderPainted(false);
		btn2.setFocusPainted(false);
		btn2.setOpaque(false);
		btn2.setForeground(Color.decode("#5BC4BF"));
		btn2.setBounds(75, 450, 97, 30);
		btn2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(panel, "To get ACCOUNT SID and AUTH TOKEN, go to https://www.twilio.com/console\n To get PHONE SID, go to https://www.twilio.com/console/phone-numbers/ and click on your phone number");
			}	
		});
		btn2.addMouseListener(new MouseListener(){
			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				btn2.setFont(new Font("Roboto Black", Font.ITALIC, 30));
			}

			@Override
			public void mouseExited(MouseEvent arg0)
			{
				btn2.setFont(new Font("Roboto", Font.ITALIC, 30));				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0){}
		});
		panel.add(btn2);
		
		btn1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(textField.getText().equals("") || textField_1.getText().equals("") || textField_2.getText().equals(""))
				{
					JOptionPane.showMessageDialog(panel, "Please enter all keys");
				}
				else
				{
					String as = textField.getText();
					String at = textField_1.getText();
					String ps = textField_2.getText();
					
					try
					{
						//Connect Twilio to Tomcat Server through Ngrok
						Substitute sub = new Substitute();
						sub.substitute(as, at, ps);

						info.add(as);
						info.add(at);
						info.add(ps);
						
						next = "ImportNumbers";
					}
					catch(NgrokNotConfiguredException ex)
					{
						JOptionPane.showMessageDialog(panel, "Those keys are correct, but Ngrok is not configured correctly.\nCheck your internet connection, or try restarting the app.");
					}
					catch(Exception ex)
					{
						JOptionPane.showMessageDialog(panel, "Those keys are invalid, please try again with valid keys");
					}
				}
			}
			
		});
		
		JLabel lbl4 = new JLabel("Please Enter Account Keys");
		lbl4.setForeground(Color.decode("#FFC720"));
		lbl4.setFont(new Font("Roboto Black", Font.PLAIN, 25));
		lbl4.setBounds(445, 45, 310, 42);
		panel.add(lbl4);
		
		textField.setFocusTraversalKeysEnabled(false);
		textField.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode()==KeyEvent.VK_TAB)
				{
					textField_1.grabFocus();
				}
			}
		});
		
		textField_1.setFocusTraversalKeysEnabled(false);
		textField_1.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode()==KeyEvent.VK_TAB)
				{
					textField_2.grabFocus();
				}
			}
		});
	}


}
