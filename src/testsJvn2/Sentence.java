/***
 * Sentence class : used for representing the text exchanged between users
 * during a chat application
 * Contact: 
 *
 * Authors: 
 */

package testsJvn2;

import java.util.concurrent.TimeUnit;

import jvn.IOAnnotation;

public class Sentence implements java.io.Serializable, SentenceItf {
	String 		data;
  
	public Sentence() {
		data = new String("");
	}
	
	public void write(String text) {
		System.out.println("[WRITE]Sleeping before unlocking ...");
		try {
			TimeUnit.MILLISECONDS.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Slept!");
		
		data = text;
	}
	
	public String read() {
		//For test purposes : Sleeping before unlocking
		System.out.println("[READ]Sleeping before unlocking ...");
		try {
			TimeUnit.MILLISECONDS.sleep(250);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Slept!");
		
		return data;	
	}
	
}