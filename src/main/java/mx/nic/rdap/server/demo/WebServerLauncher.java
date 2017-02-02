/**
 * 
 */
package mx.nic.rdap.server.demo;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.scan.StandardJarScanner;

public class WebServerLauncher {

	public static void main(String[] args) throws ServletException, LifecycleException {
		String webappDirLocation = "src/main/webapp/";
		Tomcat tomcat = new Tomcat();
		// Set port
		String webPort ="8080";
		if(args.length>0){
			
				try {
					InetAddress.getByName(args[0]);
					tomcat.getConnector().setProperty("address", args[0].trim());
					
				} catch (UnknownHostException e) {
					throw new RuntimeException("Invalid address");
				}
			
			if(args.length>1){
				try{
					Integer.parseInt(args[1]);
					webPort=args[1].trim();
				}catch(NumberFormatException nfe){
					throw new RuntimeException("Invalid port");
				}
			}
		}
		tomcat.getConnector().setProperty("port", webPort);
	
		
		
		
		StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
		((StandardJarScanner) ctx.getJarScanner()).setScanAllDirectories(true);
		// declare an alternate location for your "WEB-INF/classes" dir:
		File classes = new File("target/classes");
		WebResourceRoot resources = new StandardRoot(ctx);
		resources.addPreResources(
				new DirResourceSet(resources, "/WEB-INF/classes", classes.getAbsolutePath(), "/"));
		ctx.setResources(resources);
		ctx.setPath("rdap-server");
		tomcat.start();
		tomcat.getServer().await();
	}

}