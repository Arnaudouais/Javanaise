package testsJvn2;

import jvn.JvnProxy;

public class RudimentalTest {
	
	public static void main(String argv[]) {
		SentenceItf s = (SentenceItf) JvnProxy.newInstance(new Sentence());
		s.write("j'aime les pates");
		System.out.println(s.read());
	}
}
