/**
 * Created at 2007-12-10 by pony
 */
package com.allinpay.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * ��¼��־��ʹ��LoggerUtil�ľ�̬����info,error,debug.
 */
public class Logger {
	private org.apache.log4j.Logger logger;

	/**
	 * �������ļ��г�ʼ��logger4j����.ֻ��ʼ��һ��.
	 */
	private static Map<ClassLoader, Boolean> isLogger4jInitMap = new HashMap<ClassLoader, Boolean>();

	/**
	 * ���η���Ϊprotected,�˷���ֻ�ܱ�λ��Loggerͬһ��Ŀ¼�µ�LoggerUtil����.
	 * ������Ҫ��¼��־�Ĵ��붼�����LoggerUtil����ڵ���.
	 * 
	 * @param clazz
	 *            Class
	 * @return
	 */
	protected static Logger getLogger(final Class clazz) {
		return new Logger(clazz.getClassLoader(), clazz.getName());
	}

	private Logger(final ClassLoader cl, final String className) {
		Boolean isLogger4jInit = isLogger4jInitMap.get(cl);
		if (null == isLogger4jInit || !isLogger4jInit) {
			try {
				Properties prop = new Properties();
				prop.load(cl
						.getResourceAsStream("log4j.properties"));
				PropertyConfigurator.configure(prop);
				isLogger4jInitMap.put(cl, Boolean.TRUE);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger = org.apache.log4j.Logger.getLogger(className);
	}

	/**
	 * Debug.
	 * 
	 * @param msg
	 */
	public void debug(final String msg) {
		logger.debug(msg);
	}

	/**
	 * Info.
	 * 
	 * @param msg
	 */
	public void info(final String msg) {
		logger.info(msg);
	}

	/**
	 * warn.
	 * 
	 * @param msg
	 */
	public void warn(final String msg) {
		logger.warn(msg);
	}

	/**
	 * warn.
	 * 
	 * @param e
	 */
	public void warn(final Exception e) {
		logger.warn(e.getMessage(), e);
	}

	/**
	 * warn.
	 * 
	 * @param e
	 */
	public void warn(final Throwable e) {
		logger.warn(e.getMessage(), e);
	}

	/**
	 * Error.
	 * 
	 * @param msg
	 */
	public void error(final String msg) {
		logger.error(msg);
	}

	/**
	 * Error.
	 * 
	 * @param e
	 */
	public void error(final Exception e) {
		logger.error(e.getMessage(), e);
	}

	/**
	 * Error.
	 * 
	 * @param e
	 */
	public void error(final Throwable e) {
		logger.error(e.getMessage(), e);
	}
}
