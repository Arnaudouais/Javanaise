package testsJvn2;

import java.util.concurrent.TimeUnit;
import jvn.JvnProxy;

public class ClassicTest {

	public static void main(String[] args) {
		SentenceItf s = (SentenceItf) JvnProxy.newInstance(new Sentence());
		//Sleeping
		System.out.println("Sleeping before starting ...");
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Starting!");
		
		for(int i=0; i<20; i++){
			
			s.write(((Integer)i).toString());
			System.out.println(s.read());
		}
		

	}

}
