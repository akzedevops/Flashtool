package org.flashtool.system;

import java.net.ProxySelector;

import com.github.markusbernhardt.proxy.ProxySearch;
import com.github.markusbernhardt.proxy.ProxySearch.Strategy;
import com.github.markusbernhardt.proxy.util.PlatformUtil;
import com.github.markusbernhardt.proxy.util.PlatformUtil.Platform;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Proxy {

	private static ProxySelector dps = null;
	
	public static void setProxy() {
		if (dps==null) dps=ProxySelector.getDefault();
		log.info("Searching for a web proxy");
		ProxySearch proxySearch = new ProxySearch();
        
		if (PlatformUtil.getCurrentPlattform() == Platform.WIN) {
		  proxySearch.addStrategy(Strategy.IE);
		  proxySearch.addStrategy(Strategy.FIREFOX);
		  proxySearch.addStrategy(Strategy.JAVA);
		} else 
		if (PlatformUtil.getCurrentPlattform() == Platform.LINUX) {
		  proxySearch.addStrategy(Strategy.GNOME);
		  proxySearch.addStrategy(Strategy.KDE);
		  proxySearch.addStrategy(Strategy.FIREFOX);
		} else {
		  proxySearch.addStrategy(Strategy.OS_DEFAULT);
		}
		ProxySelector ps = proxySearch.getProxySelector();
		if (ps!=null) {
			log.info("A proxy has been found. Using it as default");
			ProxySelector.setDefault(ps);
		}
		else {
			log.info("No proxy found, using direct connection");
		}
	}
	
	public static boolean canResolve(String uri) {
		DNSResolver tr = new DNSResolver("github.com");
		try {
			tr.start();
			tr.join(2000);
			tr.interrupt();
			return tr.get()!=null;
		} catch (InterruptedException e) {
			return tr.get()!=null;
		}
	}
}
