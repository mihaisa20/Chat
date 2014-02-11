import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.UnknownHostException;


public class Intro extends JFrame {

	//Campurile clasei Intro
	static JPanel IntroPanel;
	static JTextField IntroField;
	static JTextArea IntroText;
    static JButton Login;
    static JLabel IntroLabel;
    private JLabel lblServerAddress;
    static JTextField ServerIntroField;
    private JLabel lblPort;
	private JButton Connect;
    static JTextField PortIntroField;
    static String IntroAction="";
    static Boolean connected=false;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
    //Constructor
	public Intro() 
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 256);
		IntroPanel = new JPanel();
		IntroPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(IntroPanel);
		IntroPanel.setLayout(null);
		Login = new JButton("Login");
		Login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
						Login(arg0);
				}
		});
		Login.setBounds(191, 183, 67, 23);
		IntroPanel.add(Login);
		Connect=new JButton("Connect");
		Connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					try {
						Connect(arg0);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						IntroText.append("Connection failed\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						IntroText.append("Connection failed\n");
					}
				}

		
		});
		Connect.setBounds(5,145,89,23);
		IntroPanel.add(Connect);
		
		IntroField = new JTextField();
		IntroField.setBounds(5, 183, 176, 23);
	    IntroPanel.add(IntroField);
		IntroField.setColumns(10);
		
	    IntroLabel= new JLabel("Enter your nick");
		IntroLabel.setBounds(5, 168, 90, 14);
		IntroPanel.add(IntroLabel);
		
	    IntroText = new JTextArea();
	    IntroText.setEditable(false);
		IntroText.setBounds(10, 11, 187, 84);
	    IntroPanel.add(IntroText);
	    
	    lblServerAddress = new JLabel("Server Address");
	    lblServerAddress.setBounds(5, 100, 90, 14);
	    IntroPanel.add(lblServerAddress);
	    
	    ServerIntroField = new JTextField();
	    ServerIntroField.setBounds(5, 118, 176, 24);
	    IntroPanel.add(ServerIntroField);
	    ServerIntroField.setColumns(10);
	    
	    lblPort = new JLabel("Port");
	    lblPort.setBounds(191, 100, 46, 14);
	    IntroPanel.add(lblPort);
	    
	    PortIntroField = new JTextField();
	    PortIntroField.setBounds(191, 118, 67, 24);
	    IntroPanel.add(PortIntroField);
	    PortIntroField.setColumns(10);
		IntroText.append("Insert the adress and the port\n");
	}
	public static void Login(ActionEvent evt) 
	{
		
      if(!IntroAction.equals("Already connected"))
      { if(!IntroField.getText().equals(""))
    	  IntroAction="Inserted";
      }
      else 
    	  IntroAction="Blank";
	}
	public static void Connect(ActionEvent arg0) throws UnknownHostException, IOException {
		ClientS.ConnectServer();
		if(ClientS.connected==true)
		{IntroText.append("Connected\n");
		IntroText.append("Insert your nickname\n");}
	}
	}
