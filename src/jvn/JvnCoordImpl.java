/***
 * JAVANAISE Implementation
 * JvnServerImpl class
 * Contact: 
 *
 * Authors: 
 */

package jvn;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.List;
import java.io.Serializable;


public class JvnCoordImpl 	
              extends UnicastRemoteObject 
							implements JvnRemoteCoord{
	

	private static JvnCoordImpl jvnCoord= null;
	int idToGive = 0;
	HashMap<String, JvnInfos> mapCoord;
	ArrayList<String> names;
  /**
  * Default constructor
  * @throws JvnException
  **/
	private JvnCoordImpl() throws Exception {
		// to be completed
		mapCoord = new HashMap<String, JvnInfos>();
		names = new ArrayList<String>();
	}
	
	public static void jvnLaunchCoord() {
		if (jvnCoord == null){
			try {
				jvnCoord = new JvnCoordImpl();
				JvnRemoteCoord coord_stub = (JvnRemoteCoord) UnicastRemoteObject.exportObject(jvnCoord, 0);


			    // Register the remote object in RMI registry with a given identifier
			    Registry registry= LocateRegistry.getRegistry();
			    registry.bind("CoordinatorService", jvnCoord);
				
			} catch (Exception e) {
				System.err.println("Error :" + e) ;
				e.printStackTrace();
			}
		}
	}

  /**
  *  Allocate a NEW JVN object id (usually allocated to a 
  *  newly created JVN object)
  * @throws java.rmi.RemoteException,JvnException
  **/
  public int jvnGetObjectId()
  throws java.rmi.RemoteException,jvn.JvnException {
    // to be completed 
	idToGive++;
    return idToGive;
  }
  
  /**
  * Associate a symbolic name with a JVN object
  * @param jon : the JVN object name
  * @param jo  : the JVN object 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the JVNServer
  * @throws java.rmi.RemoteException,JvnException
  **/
  public void jvnRegisterObject(String jon, JvnObject jo, JvnRemoteServer js)
  throws java.rmi.RemoteException,jvn.JvnException{
	  if (!mapCoord.containsKey(jon)){
		  mapCoord.put(jon, new JvnInfos(jo.jvnGetObjectId(),jo.jvnGetObjectState(),jo.jvnGetObjectLock(),js));
		  names.add(jo.jvnGetObjectId(), jon);
	  }
	  
  }
  
  /**
  * Get the reference of a JVN object managed by a given JVN server 
  * @param jon : the JVN object name
  * @param js : the remote reference of the JVNServer
  * @throws java.rmi.RemoteException,JvnException
  **/
  public JvnObject jvnLookupObject(String jon, JvnRemoteServer js)
  throws java.rmi.RemoteException,jvn.JvnException{
    // to be completed 
    return null;
  }
  
  /**
  * Get a Read lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
   public Serializable jvnLockRead(int joi, JvnRemoteServer js)
   throws java.rmi.RemoteException, JvnException{
    JvnInfos obj = mapCoord.get(names.get(joi));
    Lock l = obj.jvnGetLock();
    if(l.equals(Lock.R)){
    	obj.jvnAddServer(js);
    }
    else if (l.equals(Lock.W)){
    	if(obj.jvnGetClients().size()>1){
    		throw new JvnException("Too many writers");
    	}
    	for(JvnRemoteServer client : obj.jvnGetClients()){
    		obj.jvnSetState(client.jvnInvalidateWriterForReader(joi));
    	}
    	obj.jvnSetLock(Lock.R);
    }
    else if(l.equals(Lock.NL)){
    	obj.jvnSetLock(Lock.R);
    	obj.jvnAddServer(js);
    	
    }
    else{
		throw new JvnException("Weird lock value");
	}
    return obj.jvnGetState();
   }

  /**
  * Get a Write lock on a JVN object managed by a given JVN server 
  * @param joi : the JVN object identification
  * @param js  : the remote reference of the server
  * @return the current JVN object state
  * @throws java.rmi.RemoteException, JvnException
  **/
   public Serializable jvnLockWrite(int joi, JvnRemoteServer js)
   throws java.rmi.RemoteException, JvnException{
	   JvnInfos obj = mapCoord.get(names.get(joi));
	    Lock l = obj.jvnGetLock();
	    if(l.equals(Lock.R)){
	    	for(JvnRemoteServer client : obj.jvnGetClients()){
	    		client.jvnInvalidateReader(joi);
	    		obj.jvnGetClients().remove(client);
	    	}
	    }
	    else if (l.equals(Lock.W)){
	    	if(obj.jvnGetClients().size()>1){
	    		throw new JvnException("Too many writers");
	    	}
	    	for(JvnRemoteServer client : obj.jvnGetClients()){
	    		obj.jvnSetState(client.jvnInvalidateWriter(joi));
	    		obj.jvnGetClients().remove(client);
	    	}
	    }
	    else if(l.equals(Lock.NL)){
	    	
	    	obj.jvnAddServer(js);
	    	
	    }
	    else{
			throw new JvnException("Weird lock value");
		}
	    
	    obj.jvnSetLock(Lock.W);
	    return obj.jvnGetState();
   }

	/**
	* A JVN server terminates
	* @param js  : the remote reference of the server
	* @throws java.rmi.RemoteException, JvnException
	**/
    public void jvnTerminate(JvnRemoteServer js)
	 throws java.rmi.RemoteException, JvnException {
	 // to be completed
    }
}

 
