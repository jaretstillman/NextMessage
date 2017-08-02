package js.nextmessage.gui.windows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/*
 * Description: This class sets up the "ImportNumbers" window
 * 
 * Author: Jaret Stillman (jrsstill@umich.edu)
 */

public class ImportNumbers extends Windows
{

	private int numItems = 0;
	private ArrayList<JTextField> textFields;
	
	public ImportNumbers()
	{		
		textFields = new ArrayList<JTextField>();
		
		JPanel p = new JPanel();
		p.setPreferredSize(new Dimension(600,1500));
		p.setMinimumSize(new Dimension(600,1500));
		p.setMaximumSize(new Dimension(600,1500));
		p.setBackground(Color.decode("#74ADAE"));

		JScrollPane sp = new JScrollPane(p);
		p.setLayout(null);
		
		JLabel lbl1 = new JLabel("NUMBER");
		lbl1.setFont(new Font("Roboto", Font.BOLD, 30));
		lbl1.setForeground(Color.decode("#0b2040"));
		lbl1.setHorizontalTextPosition(SwingConstants.LEFT);
		lbl1.setHorizontalAlignment(SwingConstants.LEFT);
		lbl1.setBounds(67, 13, 139, 27);
		p.add(lbl1);
		
		JLabel lbl2 = new JLabel("COMPANY NAME");
		lbl2.setHorizontalTextPosition(SwingConstants.LEFT);
		lbl2.setHorizontalAlignment(SwingConstants.LEFT);
		lbl2.setFont(new Font("Roboto", Font.BOLD, 30));
		lbl2.setForeground(Color.decode("#0b2040"));
		lbl2.setBounds(280, 13, 235, 27);
		p.add(lbl2);
		
		int labelX = 102;
		int inputX = 280;
		int buttonX = 67;
		int y = 70;
		
		
		JButton plus = new JButton("ADD ANOTHER COMPANY");
		plus.setFont(new Font("Roboto", Font.BOLD, 20));
		plus.setBackground(Color.decode("#0B2040"));
		plus.setForeground(Color.decode("#FFC720"));
		plus.setFocusPainted(false);
		p.add(plus);
		plus.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				createLabelPrompt(labelX,inputX,buttonX,y, p, plus);
			}
			
		});
		createLabelPrompt(labelX,inputX,buttonX,y,p,plus);
		
		JButton btn2 = new JButton("IMPORT");
		btn2.setBounds(471, 450, 205, 45);
		btn2.setFocusPainted(false);
		btn2.setForeground(Color.decode("#0B2040"));
		btn2.setBackground(Color.decode("#5BC4BF"));
		btn2.setFont(new Font("Roboto", Font.BOLD, 20));
		panel.add(btn2);
		
		textFields.get(0).getDocument().addDocumentListener(new DocumentListener() //check first field
		{
			@Override
			public void changedUpdate(DocumentEvent arg0)
			{}

			@Override
			public void insertUpdate(DocumentEvent arg0)
			{
				btn2.setBackground(Color.decode("#FFC720"));
			}

			@Override
			public void removeUpdate(DocumentEvent arg0)
			{
				if(ImportNumbers.this.textFields.get(0).getText().equals(""))
				{
					btn2.setBackground(Color.decode("#5BC4BF"));
				}
			}
		});
		
		
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setBounds(300,50,600,350);
		panel.add(sp);
		
		btn2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(!textFields.get(0).getText().equals(""))
				{
					for(JTextField tf : textFields)
					{
						info.add(tf.getText());
					}
					next = "StartServer";
				}
				else
				{
					JOptionPane.showMessageDialog(panel, "Please enter at least 1 company");
				}
			}
			
		});
	}
	
	private void createLabelPrompt(int labelX, int inputX, int buttonX, int y, JPanel p, JButton plus)
	{	
		numItems++;
		
		JTextField textField = new JTextField();
		textField.setBounds(inputX, y + (numItems-1) * 47 , 245, 42);
		textField.setFont(new Font("Roboto",Font.PLAIN,25));
		p.add(textField);
		textField.setColumns(10);
		textFields.add(textField);
		
		JLabel label = new JLabel(new Integer(numItems).toString());
		label.setFont(new Font("Roboto Black", Font.PLAIN, 25));
		label.setForeground(Color.decode("#0B2040"));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setHorizontalTextPosition(SwingConstants.CENTER);
		label.setBounds(labelX, y + (numItems-1) * 47, 56, 42);
		p.add(label);
		
		plus.setBounds(buttonX, y + 47 * numItems, 458, 35);

		textField.setFocusTraversalKeysEnabled(false);
		textField.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e)
			{
				if(e.getKeyCode()==KeyEvent.VK_TAB)
				{
					if(textFields.indexOf(textField)!=textFields.size()-1)
					{
						textFields.get(textFields.indexOf(textField)+1).grabFocus();
					}
				}
			}
		});
		
	}
	

}
