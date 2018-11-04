/***
 * Sentence class : used for representing the text exchanged between users
 * during a chat application
 * Contact: 
 *
 * Authors: 
 */

package irc;

import jvn.IOAnnotation;
import testsJvn2.SentenceItf;

public class Sentence implements java.io.Serializable, SentenceItf {
	String 		data;
  
	public Sentence() {
		data = new String("");
	}
	
	@IOAnnotation(mode = "write")
	public void write(String text) {
		data = text;
	}
	
	@IOAnnotation(mode = "read")
	public String read() {
		return data;	
	}
	
}