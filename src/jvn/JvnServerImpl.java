/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;

import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;



public class JvnServerImpl 	
              extends UnicastRemoteObject 
							implements JvnLocalServer, JvnRemoteServer{
	
  // A JVN server is managed as a singleton 
	private static JvnServerImpl js = null;
	Registry registry = null;
	JvnRemoteCoord coord = null;
	HashMap<Integer,JvnObject> objects;
  /**
  * Default constructor
  * @throws JvnException
  **/
	private JvnServerImpl(String host) throws Exception {
		super();
		// to be completed
		registry = LocateRegistry.getRegistry(host);
		coord = (JvnRemoteCoord) registry.lookup("CoordinatorService");
		objects = new HashMap<Integer, JvnObject>();
	}
	
  /**
    * Static method allowing an application to get a reference to 
    * a JVN server instance
    * @throws JvnException
    **/
	public static JvnServerImpl jvnGetServer(String host) {
		if (js == null){
			try {
				js = new JvnServerImpl(host);
			} catch (Exception e) {
				return null;
			}
		}
		return js;
	}
	
	/**
	* The JVN service is not used anymore
	* @throws JvnException
	**/
	public  void jvnTerminate()
	throws jvn.JvnException {
		try{
			coord.jvnTerminate(js);
		} catch (RemoteException e){
			System.err.println("Error :" + e) ;
			e.printStackTrace();
		}
		js = null;
	} 
	
	/**
	* creation of a JVN object
	* @param o : the JVN object state
	* @throws JvnException
	**/
	public  JvnObject jvnCreateObject(Serializable o)
	throws jvn.JvnException {
		int joi;
		try{
		joi = coord.jvnGetObjectId();
		JvnObject obj = new JvnObjectImpl(joi,o,js);
		objects.put(joi, obj);
		return obj;
		} catch (RemoteException e){
			System.err.println("jvnCreateObject - Error :" + e) ;
			e.printStackTrace();
		}
		return null; 
	}
	
	/**
	*  Associate a symbolic name with a JVN object
	* @param jon : the JVN object name
	* @param jo : the JVN object 
	* @throws JvnException
	**/
	public  void jvnRegisterObject(String jon, JvnObject jo)
	throws jvn.JvnException {
		try{
			coord.jvnRegisterObject(jon, jo, (JvnRemoteServer)js);
		} catch (RemoteException e){
			System.err.println("jvnRegisterObject - Error :" + e) ;
			e.printStackTrace();
		}
	}
	
	/**
	* Provide the reference of a JVN object beeing given its symbolic name
	* @param jon : the JVN object name
	* @return the JVN object 
	* @throws JvnException
	**/
	public  JvnObject jvnLookupObject(String jon)
	throws jvn.JvnException {
		try{
			JvnObject tmp = coord.jvnLookupObject(jon, js);
			if(tmp != null) {
				tmp.jvnSetObjectServer(js);
				objects.put(tmp.jvnGetObjectId(), tmp);
			}
			return tmp;
		} catch (RemoteException e){
			System.err.println("jvnLookupObject - Error :" + e) ;
			e.printStackTrace();
		}
		return null;
	}	
	
	/**
	* Get a Read lock on a JVN object 
	* @param joi : the JVN object identification
	* @return the current JVN object state
	* @throws  JvnException
	**/
   public Serializable jvnLockRead(int joi)
	 throws JvnException {
	   	try{
	   		return coord.jvnLockRead(joi, js);
		} catch (RemoteException e){
			System.err.println("jvnLockRead - Error :" + e) ;
			e.printStackTrace();
		}
		return null;

	}	
	/**
	* Get a Write lock on a JVN object 
	* @param joi : the JVN object identification
	* @return the current JVN object state
	* @throws  JvnException
	**/
   public Serializable jvnLockWrite(int joi)
	 throws JvnException {
	   try{
	   		return coord.jvnLockWrite(joi, js);
		} catch (RemoteException e){
			System.err.println("jvnLockWrite - Error :" + e) ;
			e.printStackTrace();
		}
		return null;
	}	

	
  /**
	* Invalidate the Read lock of the JVN object identified by id 
	* called by the JvnCoord
	* @param joi : the JVN object id
	* @return void
	* @throws java.rmi.RemoteException,JvnException
	**/
  public void jvnInvalidateReader(int joi)
	throws java.rmi.RemoteException,jvn.JvnException {
		objects.get(joi).jvnInvalidateReader(); 
	};
	    
	/**
	* Invalidate the Write lock of the JVN object identified by id 
	* @param joi : the JVN object id
	* @return the current JVN object state
	* @throws java.rmi.RemoteException,JvnException
	**/
  public Serializable jvnInvalidateWriter(int joi)
	throws java.rmi.RemoteException,jvn.JvnException { 
	  return objects.get(joi).jvnInvalidateWriter(); 
	};
	
	/**
	* Reduce the Write lock of the JVN object identified by id 
	* @param joi : the JVN object id
	* @return the current JVN object state
	* @throws java.rmi.RemoteException,JvnException
	**/
   public Serializable jvnInvalidateWriterForReader(int joi)
	 throws java.rmi.RemoteException,jvn.JvnException { 
		return objects.get(joi).jvnInvalidateWriterForReader(); 
	 };

}

 
