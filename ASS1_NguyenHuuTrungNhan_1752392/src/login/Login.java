package login;

import java.awt.EventQueue;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import client.MainGui;
import tags.Encode;
import tags.Tags;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.awt.Font;
import javax.swing.UIManager;

public class Login {
 private static String NAME_FAILED = "INVALID CHARACTER";
 private static String NAME_EXSIST = "USER NAME EXISTED";
 private static String SERVER_NOT_START = "TURN ON SERVER FIRST";

 private Pattern checkName = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");

 private JFrame frameLoginForm;
 private JTextField txtPort;
 private JLabel lblError;
 private String name = "", IP = "";
 private JTextField txtIP;	
 private JTextField txtUsername;
 private JButton btnLogin;

 public static void main(String[] args) {
  EventQueue.invokeLater(new Runnable() {
   public void run() {
    try {
     Login window = new Login();
     window.frameLoginForm.setVisible(true);
    } catch (Exception e) {
     e.printStackTrace();
    }
   }
  });
 }

 public Login() {
  initialize();
 }

 private void initialize() {
  frameLoginForm = new JFrame();

  frameLoginForm.setTitle("Login");
  frameLoginForm.setResizable(false);

  frameLoginForm.setBounds(100, 100, 531, 243);
  frameLoginForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frameLoginForm.getContentPane().setLayout(null);



  JLabel lblHostServer = new JLabel("IP Server");
  lblHostServer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
  
  lblHostServer.setBounds(18, 45, 86, 20);
  frameLoginForm.getContentPane().add(lblHostServer);

  JLabel lblPortServer = new JLabel("Port Server");
  lblPortServer.setFont(new Font("Segoe UI", Font.PLAIN, 13));
  
  lblPortServer.setBounds(270, 45, 86, 20);
  frameLoginForm.getContentPane().add(lblPortServer);

  txtPort = new JTextField();
  txtPort.setFont(new Font("Segoe UI", Font.PLAIN, 13));
  
  txtPort.setText("9999");
  txtPort.setEditable(false);
  txtPort.setColumns(10);
  
  txtPort.setBounds(351, 42, 36, 28);
  frameLoginForm.getContentPane().add(txtPort);

  JLabel lblUserName = new JLabel("User Name");
  lblUserName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
  
  lblUserName.setBounds(18, 90, 106, 38);
  frameLoginForm.getContentPane().add(lblUserName);
  

  lblError = new JLabel("");
  
  lblError.setBounds(18, 180, 399, 20);
  frameLoginForm.getContentPane().add(lblError);

  txtIP = new JTextField();                                 //txtIP start

  txtIP.setFont(new Font("Segoe UI", Font.PLAIN, 13));      // Change starts
  txtIP.setText("192.168.56.1");        
  txtIP.setEditable(false);                               //Change ends


  txtIP.setBounds(117, 42, 99, 28);
  frameLoginForm.getContentPane().add(txtIP);
  txtIP.setColumns(10);                                     
  
                                                            //txtIP ends

  txtUsername = new JTextField();
  txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 13));
  txtUsername.setColumns(10);
  
  txtUsername.setBounds(117, 91, 366, 30);
  frameLoginForm.getContentPane().add(txtUsername);

  btnLogin = new JButton("Login");
  btnLogin.setFont(new Font("Segoe UI", Font.PLAIN, 13));

  btnLogin.addActionListener(new ActionListener() {

   public void actionPerformed(ActionEvent arg0) {
    name = txtUsername.getText();
    lblError.setVisible(false);
    IP = txtIP.getText();


    
    if (checkName.matcher(name).matches() && !IP.equals("")) {
     try {
      Random rd = new Random();
      int portPeer = 10000 + rd.nextInt() % 1000;
      InetAddress ipServer = InetAddress.getByName(IP);
      
      int portServer = Integer.parseInt("9999");
      Socket socketClient = new Socket(ipServer, portServer);

      String msg = Encode.getCreateAccount(name, Integer.toString(portPeer));
      ObjectOutputStream serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
      serverOutputStream.writeObject(msg);
      serverOutputStream.flush();
      ObjectInputStream serverInputStream = new ObjectInputStream(socketClient.getInputStream());
      msg = (String) serverInputStream.readObject();

      socketClient.close();
      if (msg.equals(Tags.SESSION_DENY_TAG)) {
       lblError.setText(NAME_EXSIST);
       lblError.setVisible(true);
       return;
      }
      new MainGui(IP, portPeer, name, msg);
      
      frameLoginForm.dispose();
     } catch (Exception e) {
      lblError.setText(SERVER_NOT_START);
      lblError.setVisible(true);
      e.printStackTrace();
     }
    }
    else {
     lblError.setText(NAME_FAILED);
     lblError.setVisible(true);
     lblError.setText(NAME_FAILED);
    }
   }
  });
  
  btnLogin.setBounds(225, 135, 81, 36);            
  frameLoginForm.getContentPane().add(btnLogin);
  lblError.setVisible(false);


 }
}