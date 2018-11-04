package testsJvn2;

import jvn.JvnProxy;

class ThreadDemo extends Thread {
   private Thread t;
   private SentenceItf s;
   private String threadName;
   
   ThreadDemo( String name) {
      threadName = name;
      System.out.println("Creating " +  threadName );
   }
   
   public void run() {
	  s = (SentenceItf) JvnProxy.newInstance(new Sentence());
      System.out.println("Running " +  threadName );
      for(int i = 1; i < 31; i++) {
	    s.write(((Integer)i).toString());
		System.out.println(s.read());
	 }
      System.out.println("Thread " +  threadName + " exiting.");
   }
   
   public void start () {
      System.out.println("Starting " +  threadName );
      if (t == null) {
         t = new Thread (this, threadName);
         t.start ();
      }
   }
}


public class MultithreadTest {
	
	public static void main(String args[]) {
		Thread t[] = new ThreadDemo[30];
		for(int i=0; i<31;i++){
			t[i] = new ThreadDemo("Thread-"+i);
	     	t[i].start();
		}
	} 

}