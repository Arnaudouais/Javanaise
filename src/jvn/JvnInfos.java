package jvn;

import java.io.Serializable;
import java.util.ArrayList;

public class JvnInfos {
	int joi;
	Serializable o;
	Lock l;
	ArrayList<JvnRemoteServer> clients;
	public JvnInfos(int joi, Serializable o, Lock l, JvnRemoteServer client) {
		this.joi = joi;
		this.o = o;
		this.l = l;
		this.clients = new ArrayList<JvnRemoteServer>();
		this.clients.add(client);
		
	}
	
	public void jvnAddServer(JvnRemoteServer client){
		this.clients.add(client);
	}
	
	public void jvnRemoveServer(JvnRemoteServer client){
		this.clients.remove(client);
	}
	
	public int jvnGetIdentifier (){
		return joi;
	}
	
	public Serializable jvnGetState (){
		return o;
	}
	
	public Lock jvnGetLock (){
		return l;
	}
	
	public ArrayList<JvnRemoteServer> jvnGetClients (){
		return clients;
	}
	
	public void jvnSetIdentifier(int joi){
		this.joi = joi;
	}
	
	public void jvnSetState(Serializable o){
		this.o = o;
	}
	
	public void jvnSetLock(Lock l){
		this.l = l;
	}

}
