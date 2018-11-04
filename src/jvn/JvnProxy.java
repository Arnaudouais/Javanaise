package jvn;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class JvnProxy implements InvocationHandler {
	
	private JvnObject jo; 
	
	private JvnProxy(Serializable obj){
		try {
			//String host = argv[0]; 
			String host = "127.0.0.1";
			// initialize JVN
			JvnServerImpl js = JvnServerImpl.jvnGetServer(host);
			
			// look up the IRC object in the JVN server
			// if not found, create it, and register it in the JVN server
			jo = js.jvnLookupObject("IRC");
			//JvnObject jo = null;
			   
			if (jo == null) {
				System.out.println("AAAAAAAAH");
				jo = js.jvnCreateObject(obj);
				// after creation, I have a write lock on the object
				jo.jvnUnLock();
				js.jvnRegisterObject("IRC", jo);
			}
		   
		   } catch (Exception e) {
			   System.out.println("IRC problem : " + e.getMessage());
			   e.printStackTrace();
		   }
	}
	
	public static Object newInstance(Serializable obj){ 
		return java.lang.reflect.Proxy.newProxyInstance( obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new JvnProxy(obj)); 
	}
	
	public Object invoke(Object proxy, Method m, Object args[]){
		Object result = null;
		try {
			if(!m.isAnnotationPresent(IOAnnotation.class)){
				throw new JvnException("Missing runtime annotation on method. Please add @IOAnnotation(mode = \"read\") or @IOAnnotation(mode = \"write\")");
			}
			else{
				IOAnnotation anno=m.getAnnotation(IOAnnotation.class);
				if(anno.mode().equals("read")){
					// lock the object in read mode
					System.out.println("jvnLockRead : Lock."+jo.jvnGetObjectLock().toString());
					jo.jvnLockRead();
					System.out.println("---jvnLockRead : Lock."+jo.jvnGetObjectLock().toString());
				}
				else if (anno.mode().equals("write")){
					// lock the object in write mode
					System.out.println("jvnLockWrite : Lock."+jo.jvnGetObjectLock().toString());
					jo.jvnLockWrite();
					System.out.println("---jvnLockWrite : Lock."+jo.jvnGetObjectLock().toString());
				}
				else{
					throw new JvnException("Wrong runtime annotation on method. Please add @IOAnnotation(mode = \"read\") or @IOAnnotation(mode = \"write\")");
				}
				
				// invoke the method
				result = m.invoke(jo.jvnGetObjectState(), args);
				
				
				
				
				// unlock the object
				jo.jvnUnLock();
				System.out.println("---jvnUnlock : Lock."+jo.jvnGetObjectLock().toString());
				
			}
		} catch (Exception e){
			System.err.println("Error :" + e) ;
			e.printStackTrace();
		} 
		return result; 
	} 
}