/***
 * Irc class : simple implementation of a chat using JAVANAISE
 * Contact: 
 *
 * Authors: 
 */

package irc;

import java.awt.*;
import java.awt.event.*;


import jvn.*;
import java.io.*;
import java.util.concurrent.TimeUnit;


public class Irc2 {
	public TextArea		text;
	public TextField	data;
	Frame 			frame;
	JvnObject       sentence;


  /**
  * main method
  * create a JVN object nammed IRC for representing the Chat application
  **/
	public static void main(String argv[]) {
	   try {
		//String host = argv[0]; 
		String host = "127.0.0.1";
		// initialize JVN
		JvnServerImpl js = JvnServerImpl.jvnGetServer(host);
		
		// look up the IRC object in the JVN server
		// if not found, create it, and register it in the JVN server
		JvnObject jo = js.jvnLookupObject("IRC");
		//JvnObject jo = null;
		   
		if (jo == null) {
			System.out.println("AAAAAAAAH");
			jo = js.jvnCreateObject((Serializable) new Sentence());
			// after creation, I have a write lock on the object
			jo.jvnUnLock();
			js.jvnRegisterObject("IRC", jo);
		}
		// create the graphical part of the Chat application
		 new Irc2(jo);
	   
	   } catch (Exception e) {
		   System.out.println("IRC problem : " + e.getMessage());
		   e.printStackTrace();
	   }
	}

  /**
   * IRC Constructor
   @param jo the JVN object representing the Chat
   **/
	public Irc2(JvnObject jo) {
		sentence = jo;
		frame=new Frame();
		frame.setLayout(new GridLayout(1,1));
		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		frame.add(text);
		data=new TextField(40);
		frame.add(data);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListener2(this));
		frame.add(read_button);
		Button write_button = new Button("write");
		write_button.addActionListener(new writeListener2(this));
		frame.add(write_button);
		frame.setSize(545,201);
		text.setBackground(Color.black); 
		frame.setVisible(true);
	}
}


 /**
  * Internal class to manage user events (read) on the CHAT application
  **/
 class readListener2 implements ActionListener {
	Irc2 irc;
  
	public readListener2 (Irc2 i) {
		irc = i;
	}
   
 /**
  * Management of user events
  **/
	public void actionPerformed (ActionEvent e) {
	 try {
		// lock the object in read mode
		System.out.println("jvnLockRead : Lock."+irc.sentence.jvnGetObjectLock().toString());
		irc.sentence.jvnLockRead();
		System.out.println("---jvnLockRead : Lock."+irc.sentence.jvnGetObjectLock().toString());
		
		// invoke the method
		String s = ((Sentence)(irc.sentence.jvnGetObjectState())).read();
		
		
		//Sleeping before unlocking
		System.out.println("[READ]Sleeping before unlocking ...");
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Slept!");
		
		
		// unlock the object
		irc.sentence.jvnUnLock();
		System.out.println("---jvnUnlock : Lock."+irc.sentence.jvnGetObjectLock().toString());
		
		// display the read value
		irc.data.setText(s);
		irc.text.append(s+"\n");
	   } catch (JvnException je) {
		   System.out.println("IRC problem : " + je.getMessage());
		   je.printStackTrace();
	   }
	}
}

 /**
  * Internal class to manage user events (write) on the CHAT application
  **/
 class writeListener2 implements ActionListener {
	Irc2 irc;
  
	public writeListener2 (Irc2 i) {
        	irc = i;
	}
  
  /**
    * Management of user events
   **/
	public void actionPerformed (ActionEvent e) {
	   try {	
		// get the value to be written from the buffer
    String s = irc.data.getText();
        	
    // lock the object in write mode
    	System.out.println("jvnLockWrite : Lock."+irc.sentence.jvnGetObjectLock().toString());
		irc.sentence.jvnLockWrite();
		System.out.println("---jvnLockWrite : Lock."+irc.sentence.jvnGetObjectLock().toString());
		
		// invoke the method
		((Sentence)(irc.sentence.jvnGetObjectState())).write(s);
		
		//Sleeping before unlocking
		System.out.println("[WRITE]Sleeping before unlocking ...");
		try {
			TimeUnit.SECONDS.sleep(6);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Slept!");
		
		// unlock the object
		irc.sentence.jvnUnLock();
		System.out.println("---jvnUnlock : Lock."+irc.sentence.jvnGetObjectLock().toString());
	 } catch (JvnException je) {
		   System.out.println("IRC problem  : " + je.getMessage());
	 }
	}
}



