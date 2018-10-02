package jvn;

import java.io.Serializable;

public class JvnObjectImpl implements JvnObject {
	
	int joi;
	enum lock {
	    R,W,RC,WC,RWC
	}
	Serializable o = null;
	
	public JvnObjectImpl (int joi, Serializable o){
		this.joi = joi;
		this.o = o;
	}

	@Override
	public void jvnLockRead() throws JvnException {
		// TODO Auto-generated method stub

	}

	@Override
	public void jvnLockWrite() throws JvnException {
		// TODO Auto-generated method stub

	}

	@Override
	public void jvnUnLock() throws JvnException {
		// TODO Auto-generated method stub

	}

	@Override
	public int jvnGetObjectId() throws JvnException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Serializable jvnGetObjectState() throws JvnException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void jvnInvalidateReader() throws JvnException {
		// TODO Auto-generated method stub

	}

	@Override
	public Serializable jvnInvalidateWriter() throws JvnException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable jvnInvalidateWriterForReader() throws JvnException {
		// TODO Auto-generated method stub
		return null;
	}

}
