
import java.net.*; 
import java.util.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


class ClientS extends JFrame {  
	//Campurile clasei ClientS
	private static DataOutputStream ServerOutputStream;
	private static String mesaj;
	private static String chat;
	private static Socket ClientSocket;
	private static DataInputStream ServerInputStream; 
	private static Intro LoginFrame;
    static int Available=0;
    static String Action="";
    static ClientS ClientFrame=new ClientS();
    static String myNick;
    private static int PortClient;
    static String newNick;
    static Boolean connected=false;
    //Campurile interfetei vizuale de ClientS
    private JPanel contentPane;
	private JTextField textField;
    private JTextArea list;
    private JButton btnMsg;
    private JButton btnList;
    private JButton btnNick;
    private JButton btnBcast;
    private JButton btnQuit;
    private JLabel lblActions;
    private JTextArea textArea;
    private JTextField NickField;
    public ClientS() 
    {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 400, 400, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		btnMsg = new JButton("Msg");
		btnMsg.setBounds(150, 214, 63, 23);
		btnMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Msg(arg0);
			}
		});
		contentPane.setLayout(null);
		btnList = new JButton("List");
		btnList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List(arg0);
			}
		});
		btnList.setBounds(8, 214, 63, 23);
		contentPane.add(btnList);
		btnBcast = new JButton("Bcast");
		btnBcast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Bcast(arg0);
			}
		});
		btnBcast.setBounds(223, 214, 74, 23);
		contentPane.add(btnBcast);
		contentPane.add(btnMsg);
		btnNick = new JButton("Nick");
		btnNick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Nick(arg0);
			}
		});
		btnNick.setBounds(81, 214, 59, 23);
		contentPane.add(btnNick);
		btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Quit(arg0);
			}
		});
		btnQuit.setBounds(306, 214, 68, 23);
		contentPane.add(btnQuit);
		
		lblActions= new JLabel("Actions");
		lblActions.setBounds(25, 189, 46, 14);
		contentPane.add(lblActions);
		
		textField = new JTextField();
		textField.setBounds(8, 248, 251, 23);
		contentPane.add(textField);
		textField.setColumns(10);
	
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(8, 12, 236, 177);
		contentPane.add(textArea);
		
		list = new JTextArea();
		list.setEditable(false);
		list.setBounds(249, 11, 125, 177);
		contentPane.add(list);
		
		NickField = new JTextField();
		NickField.setBounds(269, 248, 105, 23);
		contentPane.add(NickField);
		NickField.setColumns(10);
	}
	
	private void List(ActionEvent evt) {
        // TODO add your handling code here:
        Action="List";//Setam actiunea ca list
        ClientFrame.list.setText("");
        try {
            ServerOutputStream.writeUTF("Listeaza"); //transmitem serverului sa ne listeze userii
        } catch (IOException ex) 
        {
        	ClientFrame.textArea.append("Connection failed\n");//daca nu se poate face conexiune afisam Connection failed	
        }
      }
	 private void Bcast(ActionEvent evt) 
	 {
		Action="Bcast";
	    String BcastMsg="";
	    BcastMsg=ClientFrame.textField.getText();//Preluam mesajul de broadcast din textfield
	    if(!BcastMsg.equals("")) //Daca mesajul de broadcast nu e gol
	    {
	      BcastMsg="%"+BcastMsg;//Adaugam prefix mesajului de broadcast pentru a-l recunoaste in server 
	      try
	      {
	        ServerOutputStream.writeUTF(BcastMsg); //Trimitem mesajul serverului
	       }
	       catch(IOException e)
	       {
	    	   ClientFrame.textArea.append("Connection failed\n");//daca nu se poate face conexiune afisam Connection failed	
	        }
	     }
	    }
	 private void Nick(ActionEvent evt)
	 {
	        Action="NickChange";
	        String NickTaken="";
	        NickTaken=textField.getText();//Preluam nickul din textField
	        if(!NickTaken.equals("")) //Daca nick-ul nou nu e vid
	        {
	        try 
	        {
	            ServerOutputStream.writeUTF("#"+NickTaken);   //Adaugam prefixul pentru a-l identifica in server  
	        } 
	        catch (IOException e)
	        {
	        	ClientFrame.textArea.append("Connection failed\n");//daca nu se poate face conexiune afisam Connection failed	
	        }
	  }
	        
	 }
	 private void Msg(ActionEvent evt) 
	 {
	        Action="Msg";
	        String Msg="", Nick="";
	        Msg=ClientFrame.textField.getText(); //Preluam nick-ul si mesajul din textfield-uri
	        Nick=ClientFrame.NickField.getText();
	        if(!Msg.equals("")&& !Nick.equals("")) //Daca nu sunt vide
	        {
	            ClientFrame.textArea.append(myNick+" > "+Nick+" : "+Msg+"\n");//Afisam mesajul transmis in textarea
	            Msg="&"+Nick+"<"+Msg;  //Adaugam prefixul si nick-ul folosind delimitatori
	            try{
	                ServerOutputStream.writeUTF(Msg);// Transmitem serverului string-ul concatenat
	            }
	            catch(IOException e)
	            {
	            	ClientFrame.textArea.append("Connection failed\n");//daca nu se poate face conexiune afisam Connection failed	
	            }
	                                       
	        }
	    }
	 private void Quit(ActionEvent evt) 
	 {
     ClientFrame.dispose();//Inchidem fereastra  
     try 
     {
         ServerOutputStream.writeUTF("Quit");//Transmitem serverului sa se inchida
     }
     catch (IOException e) 
     {
    	 ClientFrame.textArea.append("Connection failed\n"); //daca nu se poate face conexiune afisam Connection failed	
     }
     Action="Quit"; //Setam actiunea ca si Quit
      }
	 public static void ConnectServer() //Realizam conexiunea la server
	 {
		  try {
		 PortClient=Integer.parseInt(LoginFrame.PortIntroField.getText()); 
		 ClientSocket  = new Socket(LoginFrame.ServerIntroField.getText(), PortClient);
		//Deschid un Socket cu adresa si portul introdus
		 ServerOutputStream = new DataOutputStream( ClientSocket.getOutputStream());  //Deschid un DataOutputStream corespunzator Socketului
		 ServerInputStream=new DataInputStream( ClientSocket.getInputStream());
		 connected=true;
		  }
	     catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			LoginFrame.IntroText.append("Connection failed\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LoginFrame.IntroText.append("Connection failed\n");
	 }
}
	 static class Actions implements Runnable{
		    DataInputStream InStream;
		    String Msg;
		    Actions(DataInputStream dis)
		    {
		    	this.InStream=dis;
		    }
		    public void run()
		    {
		    while(true)
		    {
		     try
		      {
		          Msg=this.InStream.readUTF();//Interpretam mesajul de la server                             
		          if(Available==1) 
		          {
		          if(Action.equals("List")) //Daca actiunea este lista afisam utilizatorii
		          {
		          while(!Msg.equals("^")) //Cat timp nu intalnim caracterul ^ afisam useri
		          {
		          ClientFrame.list.append(Msg + "\n");Msg=this.InStream.readUTF();} 
		          Action="";}
		          else if(Action.equals("NickChange"))//Daca actiunea este schimbarea nick-ului
		          {
		           if(Msg.equalsIgnoreCase("Wrong nick")) //verificam daca nick-ul nu exista deja
		           ClientFrame.textArea.append("This nick is already taken!" + "\nPlease enter another one!!\n");
		           if(Msg.equalsIgnoreCase("Nick accepted"))
		           {
		           ClientFrame.textArea.append("Your nick has been successfuly "+ "changed!!\n");}
		           Action="";}
		           else ClientFrame.textArea.append(Msg+"\n"); }
		          }
		        catch(IOException e)
		        {
		       ClientFrame.textArea.append("Connection failed\n"); }//daca nu se poate face conexiunea afisam Connection failed	
		        }
		    }
		}
  public static void main(String[] args) 
  {

	try {
		LoginFrame = new Intro(); 
		LoginFrame.setVisible(true); //Construim si afisam fereastra de login

	} catch (Exception e) {
		LoginFrame.IntroText.append("Loading form failed\n");
	}
	while(true)
	{
		if(connected) //asteptam clientul sa se conecteze
			break;
	}
	Actions Act=new Actions(ServerInputStream);
	Thread Th=new Thread(Act);//Cream threadurile necesare clientului
	while(true)
	{
		if(LoginFrame.IntroAction.equals("Inserted")) //daca clientul a introdus numele
			break;
	}
	String msg;
	try {
	msg = ServerInputStream.readUTF();
	LoginFrame.IntroText.append(msg+"\n");
	myNick=LoginFrame.IntroField.getText();
	ServerOutputStream.writeUTF(myNick);//Verificam nick-ul primit
	msg=ServerInputStream.readUTF();
	LoginFrame.IntroText.append(msg+"\n");
	LoginFrame.IntroAction="";
	LoginFrame.IntroField.setText("");
	while(!msg.equals("Your nick is ok")) //Cat timp nick-ul nu este ok asteptam userul sa puna unul corect
	{
		while(true)
			if(LoginFrame.IntroAction.equals("Inserted"))
				break;
		myNick=LoginFrame.IntroField.getText();
		ServerOutputStream.writeUTF(myNick);
		msg=ServerInputStream.readUTF();
	    LoginFrame.IntroText.append(msg+"\n");
	    LoginFrame.IntroAction="";
	    LoginFrame.IntroField.setText("");
	}
	LoginFrame.IntroAction="Already connected";
	LoginFrame.IntroText.append("Press Login to enter the chat\n");
	while(true)
	{
		if(LoginFrame.IntroAction.equals("Blank"))
			break;
	}
	LoginFrame.dispose();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		LoginFrame.IntroText.append("Connection failed\n");
	}
    Available=1;
    Th.start();//Pornim threadul
    /* Create and display the form */
    EventQueue.invokeLater(new Runnable() 
    {
      public void run() {
      ClientFrame.setVisible(true); //Afisam fereastra clientului
                           }
                       });
    while(true)
    {
    	if(Action.equals("Quit")) //daca actiunea este quit atunci intrerupem bucla infinita
    		break;
    }
    Th.interrupt();//Inchidem threadurile si socketurile
    try {
	ClientSocket.close();
    ServerInputStream.close();
    ServerOutputStream.close();
    } catch (IOException e) {
		// TODO Auto-generated catch block
		ClientFrame.textArea.append("Closing the connection has failed\n");
	}
  }
 
  }

