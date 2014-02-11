import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;



import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class ServerS extends JFrame {

	//Campurile interfetei de Server
	private JPanel contentPane;
	private JTextField PortField;
    private JButton btnStartServer;
    private JLabel lblPort;
    static JTextArea ServertextArea;
    //Campurile serverului
    static int nrConexiuni=0;   
    static ArrayList<Conexiune> Conexiuni;//lista in care tinem conexiunile
    private static ServerSocket ServS;
    private static Socket Sc;
    private static Conexiune ConX;
    int Port;
    //Constructor
	public ServerS()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 256, 259);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		PortField = new JTextField();
		PortField.setBounds(10, 191, 75, 20);
		contentPane.add(PortField);
		PortField.setColumns(10);
		
		btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StartServer(arg0);
			}
		});
		btnStartServer.setBounds(118, 190, 113, 23);
		contentPane.add(btnStartServer);
		
		lblPort = new JLabel("Port");
		lblPort.setBounds(10, 178, 46, 14);
		contentPane.add(lblPort);
		
		ServertextArea = new JTextArea();
		ServertextArea.setEditable(false);
		ServertextArea.setBounds(10, 11, 219, 156);
		contentPane.add(ServertextArea);
	}
   private void StartServer(ActionEvent evt) 
   {
	    Conexiuni=new ArrayList<Conexiune>();
	    ServS = null; 
	    Sc = null;
	    Port=Integer.parseInt(PortField.getText());
	    try {
			ServS = new ServerSocket(Port);
		} catch (IOException e) {
			ServertextArea.append("Initializing the connection failed\n");
	} 
	    
	    ServertextArea.append("Server Started\n");
	    notOpened=true;
   }
   private static Boolean notOpened=false;
  public static void main(String[] arg) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerS frame = new ServerS();//Initializam si afisam interfata de Server
					frame.setVisible(true);
					frame.ServertextArea.append("Insert the connection port\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		int  nr=0;
		while(true)
		{
		if(notOpened==true) //asteptam sa se deschida conexiunea
			break;
		}
	    while (true) { //Realizam conexiunea si threadurile
	            Sc = ServS.accept();
	            ConX = new Conexiune(Sc);
	            Conexiuni.add(ConX);
	            Thread fir = new Thread(ConX);
	            fir.start();
	            nrConexiuni++;
                ServertextArea.append("New Client Connected\n");
	            
	        }
    
  }
  
static class Conexiune implements Runnable 
{ 
  Socket Sc = null; 
  DataInputStream InputStream ;
  DataOutputStream OutputStream ;
  String mesaj;
  String Nick;
  
  public Conexiune(Socket client)
  { // constructorul clasei in care primim parametrii
	Sc = client; 
    try {
		InputStream = new DataInputStream(Sc.getInputStream());
		OutputStream = new DataOutputStream(Sc.getOutputStream());
		OutputStream.writeUTF("Initializing");
        String Name = null;
		Name = InputStream.readUTF();

    Boolean l=true;
    while(l)//Verificam ca nick-ul primit sa fie corect
    {
        for(Conexiune con: Conexiuni)
            if(con.Nick.equalsIgnoreCase(Name))
            {
                OutputStream.writeUTF("The  nick is not available \n" + "Choose another one:");l=false;
                break;
            }
        if(l==false)
        {
			Name=InputStream.readUTF();l=true;}
        else break;
    }
         this.Nick=Name;  
		OutputStream.writeUTF("Your nick is ok");
    }
		catch (IOException e) {
		ServertextArea.append("Connection failed \n");
	}
}
  public void Msg(String Msg) //Transmitem mesajul catre destinatar
  {
      String mesaj=" ";
      String dest="";
      int i;
      mesaj = Msg.substring(1);
      for(i=0;i<mesaj.length();i++)
        if( mesaj.charAt(i)=='<')
         {
        	dest=mesaj.substring(0,i);
            mesaj=mesaj.substring(i+1);
            break;
          }
         Boolean l=false;
         for(Conexiune con : Conexiuni)
            if(con.Nick.equalsIgnoreCase(dest))
              {
                 try {
					con.OutputStream.writeUTF(this.Nick + " : "+mesaj);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					ServertextArea.append("Connection failed \n");
				}
                 l=true;
                 break;
              }
          if(l==false)
			try {
				this.OutputStream.writeUTF("User unavailable");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ServertextArea.append("Connection failed \n");
			}
      }
  public void Broadcast(String Msg) //Realizam broadcastul 
  {
	      String msg=" ";
	      int i;
          msg=Msg.substring(1);
	      for(Conexiune con : ServerS.Conexiuni)
				try {
					con.OutputStream.writeUTF(this.Nick + " : "+msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					ServertextArea.append("Connection failed \n");
				}
   }
  public void List() {
      for(Conexiune con:ServerS.Conexiuni)
			try {
				this.OutputStream.writeUTF(con.Nick);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ServertextArea.append("Connection failed \n");
			}
      try {
		this.OutputStream.writeUTF("^");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		ServertextArea.append("Connection failed \n");
	}
   }

   public void Nick(String Nickname) {//schimbam nick-ul userului
       Nickname=Nickname.substring(1);
       int k=0;
       for( Conexiune con: Conexiuni )
           if(!this.Nick.equals(con.Nick))
               if(Nickname.equals(con.Nick))
               {
                   try {
					this.OutputStream.writeUTF("Wrong nick");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					ServertextArea.append("Connection failed \n");
				}
                   k=1;
                   break;
               }
       if(k==0)
       {
           this.Nick=Nickname;
           try {
			this.OutputStream.writeUTF("Nick accepted");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ServertextArea.append("Connection failed \n");
		}
       }
   }
   public void Quit()
   {
	   ServerS.nrConexiuni--;
       for(Conexiune con : ServerS.Conexiuni)
           if(this.OutputStream!=con.OutputStream ){
              try {
				con.OutputStream.writeUTF(this.Nick + " has left! ");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				ServertextArea.append("Connection failed\n");
			}
           }
       ServerS.ServertextArea.append("Client Disconnected!\n");
       try {
		InputStream.close();
		OutputStream.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		ServertextArea.append("Connection failed\n");
	}
       
       Conexiuni.remove(this);
   }
   public void run() {//Preluam mesajele si le interpretam
       try{
       String msg=" ";
           while (true) {
               msg = this.InputStream.readUTF();
               if (msg.charAt(0)=='%')Broadcast(msg);
               if (msg.charAt(0)=='&')Msg(msg); 
               if (msg.charAt(0)=='#') Nick(msg);
               if (msg.equals("Listeaza"))List();
               if (msg.equals("Quit")) break;
           }
          Quit();
   }
   catch(IOException e)
    {
	   ServertextArea.append("Connection failed \n");
       Quit();
	 }
   }

}
}
