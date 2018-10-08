package jvn;

import java.io.Serializable;

public class JvnObjectImpl implements JvnObject {
	
	int joi;
	Lock lock;
	Serializable o = null;
	transient JvnLocalServer js = null;
	
	public JvnObjectImpl (int joi, Serializable o, JvnLocalServer js){
		this.joi = joi;
		this.lock = Lock.W;
		this.o = o;
		this.js = js;
	}
	
	public JvnObjectImpl (int joi, Serializable o){
		this.joi = joi;
		this.lock = Lock.W;
		this.o = o;
		this.js = null;
	}

	@Override
	public void jvnLockRead() throws JvnException {
		//switch 
		 //R, W, RC, WC, RWC, NL
		synchronized(this){
			System.out.println("jvnLockRead : Lock."+lock.toString());
			switch(lock){
	    	case R :
	    		break;
	    	case RC :
	    		lock = Lock.R;
	    		break;
	    	case RWC :
	    		break;
	    	case WC:
	    		lock = Lock.RWC;
	    		break;
	    	case W :
	    		do{
	    			try{
						wait();
					} catch (Exception e){
						System.err.println("Error :" + e) ;
						e.printStackTrace();
					}
	    		} while(lock.equals(Lock.W));
	    		o = js.jvnLockRead(joi);
	    		lock = Lock.RWC;
	    		break;
	    	case NL :
	    		o = js.jvnLockRead(joi);
	    		lock = Lock.R;
	    		break;
			}
		}
		
		

	}

	@Override
	public void jvnLockWrite() throws JvnException {
		synchronized(this){
			System.out.println("jvnLockWrite : Lock."+lock.toString());
			switch(lock){
	    	case R :
	    		do{
	    			try{
						wait();
					} catch (Exception e){
						System.err.println("Error :" + e) ;
						e.printStackTrace();
					}
	    		} while(lock.equals(Lock.R));
	    		o = js.jvnLockWrite(joi);
	    		lock = Lock.W;
	    		break;
	    	case RC :
	    		o = js.jvnLockWrite(joi);
	    		lock = Lock.W;
	    		break;
	    	case RWC :
	    		do{
	    			try{
						wait();
					} catch (Exception e){
						System.err.println("Error :" + e) ;
						e.printStackTrace();
					}
	    		} while(lock.equals(Lock.RWC));
	    		break;
	    	case WC:
	    		lock = Lock.W;
	    		break;
	    	case W :
	    		break;
	    	case NL :
	    		o = js.jvnLockWrite(joi);
	    		lock = Lock.W;
	    		break;
			}
		}
	}

	@Override
	public synchronized void jvnUnLock() throws JvnException {
		if (lock.equals(Lock.W)) lock = Lock.WC;
		if (lock.equals(Lock.R)) lock = Lock.RC;
		if (lock.equals(Lock.RWC)) lock = Lock.WC;
		notifyAll();
	}

	@Override
	public int jvnGetObjectId() throws JvnException {
		return this.joi;
	}

	@Override
	public Serializable jvnGetObjectState() throws JvnException {
		return this.o;
	}

	@Override
	public synchronized void jvnInvalidateReader() throws JvnException {
		if(lock.equals(Lock.RC)){
			lock = Lock.NL;
		}
		else if(lock.equals(Lock.RWC) || lock.equals(Lock.R)){
			do{
				try{
					wait();
				} catch (Exception e){
					System.err.println("Error :" + e) ;
					e.printStackTrace();
				}
			} while(lock.equals(Lock.RWC) || lock.equals(Lock.R));
			lock = Lock.NL;
		}
		else{
			throw new JvnException("Weird lock value");
		}

	}

	@Override
	public synchronized Serializable jvnInvalidateWriter() throws JvnException {
		if(lock.equals(Lock.WC) || lock.equals(Lock.RWC)){
			lock = Lock.NL;
		}
		else if(lock.equals(Lock.W)){
			do{
				try{
					wait();
				} catch (Exception e){
					System.err.println("Error :" + e) ;
					e.printStackTrace();
				}
			} while(lock.equals(Lock.W));
		}
		else{
			throw new JvnException("Weird lock value");
		}
		return o;
	}

	@Override
	public synchronized Serializable jvnInvalidateWriterForReader() throws JvnException {
		if(lock.equals(Lock.RWC)){
			lock = Lock.R;
		}
		else if (lock.equals(Lock.WC)){
			lock = Lock.RC;
		}
		else if (lock.equals(Lock.W)){
			do{
				try{
					wait();
				} catch (Exception e){
					System.err.println("Error :" + e) ;
					e.printStackTrace();
				}
			} while(lock.equals(Lock.W));
			lock = Lock.RC;
		}
		else{
			throw new JvnException("Weird lock value");
		}
		return o; 
	}

	@Override
	public Lock jvnGetObjectLock() throws JvnException {
		return this.lock;
	}

	@Override
	public void jvnSetObjectLock(Lock l) throws JvnException {
		this.lock = l;	
	}

	@Override
	public void jvnSetObjectServer(JvnLocalServer js) throws JvnException {
		this.js = js;	
	}

}
