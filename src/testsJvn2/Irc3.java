package testsJvn2;

/***
 * Irc class : simple implementation of a chat using JAVANAISE
 * Contact: 
 *
 * Authors: 
 */


import java.awt.*;
import java.awt.event.*;


import jvn.*;


public class Irc3 {
	public TextArea		text;
	public TextField	data;
	Frame 			frame;
	SentenceItf       sentence;


  /**
  * main method
  * create a JVN object nammed IRC for representing the Chat application
  **/
	public static void main(String argv[]) {
		 new Irc3((SentenceItf) JvnProxy.newInstance(new Sentence()));
	   
	}

  /**
   * IRC Constructor
   @param jo the JVN object representing the Chat
   **/
	public Irc3(SentenceItf jo) {
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
		read_button.addActionListener(new readListener3(this));
		frame.add(read_button);
		Button write_button = new Button("write");
		write_button.addActionListener(new writeListener3(this));
		frame.add(write_button);
		frame.setSize(545,201);
		text.setBackground(Color.black); 
		frame.setVisible(true);
	}
}


 /**
  * Internal class to manage user events (read) on the CHAT application
  **/
 class readListener3 implements ActionListener {
	Irc3 irc;
  
	public readListener3 (Irc3 i) {
		irc = i;
	}
   
 /**
  * Management of user events
  **/
	public void actionPerformed (ActionEvent e) {
		// invoke the method
		String s = irc.sentence.read();
		
		
		// display the read value
		irc.data.setText(s);
		irc.text.append(s+"\n");
	}
}

 /**
  * Internal class to manage user events (write) on the CHAT application
  **/
 class writeListener3 implements ActionListener {
	Irc3 irc;
  
	public writeListener3 (Irc3 i) {
        	irc = i;
	}
  
  /**
    * Management of user events
   **/
	public void actionPerformed (ActionEvent e) {
    String s = irc.data.getText();
		
	// invoke the method
	irc.sentence.write(s);
	}
}



