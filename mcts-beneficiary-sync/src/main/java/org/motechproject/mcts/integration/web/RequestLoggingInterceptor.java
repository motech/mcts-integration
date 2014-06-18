package org.motechproject.mcts.integration.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class RequestLoggingInterceptor extends HandlerInterceptorAdapter {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(RequestLoggingInterceptor.class);

	private ClassLoader classLoader = null;
	private boolean bundleClassLoaderSet = false;
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		LOGGER.info("preHandler Called");
//		if (classLoader == null){
		classLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
		bundleClassLoaderSet = true;
//		}
		return true;
	}
	
	 @Override
	    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		 LOGGER.info("afterCompletion Called");
		 if (bundleClassLoaderSet){
			 Thread.currentThread().setContextClassLoader(classLoader);
		 }
	    }
}
