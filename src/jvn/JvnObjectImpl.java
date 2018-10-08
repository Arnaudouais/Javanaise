package jvn;

import java.io.Serializable;

public class JvnObjectImpl implements JvnObject {
	
	int joi;
	Lock lock;
	Serializable o = null;
	JvnLocalServer js = null;
	
	public JvnObjectImpl (int joi, Serializable o, JvnLocalServer js){
		this.joi = joi;
		this.lock = Lock.W;
		this.o = o;
		this.js = js;
	}

	@Override
	public void jvnLockRead() throws JvnException {
		o = js.jvnLockRead(joi);
		lock = Lock.R;

	}

	@Override
	public void jvnLockWrite() throws JvnException {
		o = js.jvnLockWrite(joi);
		lock = Lock.W;
	}

	@Override
	public void jvnUnLock() throws JvnException {
		if (lock.equals(Lock.W)) lock = Lock.WC;
		if (lock.equals(Lock.R)) lock = Lock.RC;
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
	public void jvnInvalidateReader() throws JvnException {
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
	public Serializable jvnInvalidateWriter() throws JvnException {
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
	public Serializable jvnInvalidateWriterForReader() throws JvnException {
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

}
