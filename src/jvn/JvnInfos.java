package jvn;

import java.util.ArrayList;

public class JvnInfos {
	JvnObject o;
	Lock l;
	ArrayList<JvnRemoteServer> clients;
	public JvnInfos(JvnObject o, Lock l, JvnRemoteServer client) {
		this.o = o;
		this.l = l;
		this.clients = new ArrayList<JvnRemoteServer>();
		this.clients.add(client);
	}

}
