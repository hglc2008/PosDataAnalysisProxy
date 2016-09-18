/**
 * @(#) LoggerUtil.java
 * module  : util
 * version : �汾����ϵͳ�е��ļ��汾
 * date    : 2008-12-4
 * name    : ������
 */
package com.allinpay.util;

import java.util.HashMap;
import java.util.Map;

import sun.reflect.Reflection;

/**
 * ������κζԴ�����޸�,�밴����ĸ�ʽע���޸ĵ�����. ��� ʱ�� ���� �޸����� 1. 2008-12-4 ������ created this
 * class.
 */
public class LoggerUtil {
	/**
	 * ���е���־��¼����.
	 */
	private static Map loggers = new HashMap();

	/**
	 * ȱʡ����־��¼����.
	 */
	private static Logger defaultLogger = Logger.getLogger(LoggerUtil.class);

	/**
	 * �ӻ���loggers�л�ȡָ���������־��¼����.
	 * 
	 * @param caller
	 *            Object ���ü�¼��־�Ķ���.
	 * @return Logger
	 */
	private static Logger getLogger(final Class caller) {
		if (null == caller) {
			return defaultLogger;
		}
		Logger logger = (Logger) loggers.get(caller);
		return logger;
	}

	/**
	 * ����һ��logger���󲢻���.
	 * 
	 * @param caller
	 *            Object
	 */
	private static void createLogger(final Class caller) {
		if (null == caller) {
			return;
		}
		Logger logger = Logger.getLogger(caller);
		loggers.put(caller, logger);
	}

	/**
	 * ���ݵ��ö�����Ҷ�Ӧ����־��¼����,���û���򴴽�һ����־��¼����.
	 * 
	 * @param caller
	 *            Object
	 * @return Logger
	 */
	private static Logger findLogger(final Class caller) {
		Logger logger = getLogger(caller);
		if (null == logger) {
			createLogger(caller);
		} else {
			return logger;
		}
		return getLogger(caller);
	}

	/**
	 * �õ�����LoggerUtil���debug��info��error�����ĵ���������������.
	 * 
	 * @return
	 */
	private static Class getCallerClass() {
		return Reflection.getCallerClass(3);
	}

	/**
	 * ��¼��ʽ��־��Ϣ.
	 * 
	 * @param caller
	 * @param msg
	 */
	public static void debug(final String msg) {
		Logger logger = findLogger(getCallerClass());
		logger.debug(msg);
	}

	/**
	 * ��¼��ʾ��־��Ϣ.
	 * 
	 * @param caller
	 * @param msg
	 */
	public static void info(final String msg) {
		Logger logger = findLogger(getCallerClass());
		logger.info(msg);
	}

	/**
	 * ��¼������־��Ϣ.
	 * 
	 * @param msg
	 * @param e
	 */
	public static void warn(final String msg, final Exception e) {
		Logger logger = findLogger(getCallerClass());
		logger.warn(msg);
		logger.warn(e);
	}

	/**
	 * ��¼������־��Ϣ.
	 * 
	 * @param caller
	 * @param msg
	 */
	public static void warn(final String msg) {
		Logger logger = findLogger(getCallerClass());
		logger.warn(msg);
	}

	/**
	 * ��¼������־��Ϣ.
	 * 
	 * @param caller
	 * @param e
	 */
	public static void warn(final Exception e) {
		Logger logger = findLogger(getCallerClass());
		logger.warn(e);
	}

	/**
	 * ��¼������־��Ϣ.
	 * 
	 * @param caller
	 * @param e
	 */
	public static void warn(final Throwable e) {
		Logger logger = findLogger(getCallerClass());
		logger.warn(e);
	}

	/**
	 * ��¼������־��Ϣ.
	 * 
	 * @param msg
	 * @param e
	 */
	public static void error(final String msg, final Exception e) {
		Logger logger = findLogger(getCallerClass());
		logger.error(msg);
		logger.error(e);
	}

	/**
	 * ��¼������־��Ϣ.
	 * 
	 * @param caller
	 * @param msg
	 */
	public static void error(final String msg) {
		Logger logger = findLogger(getCallerClass());
		logger.error(msg);
	}

	/**
	 * ��¼������־��Ϣ.
	 * 
	 * @param caller
	 * @param e
	 */
	public static void error(final Exception e) {
		Logger logger = findLogger(getCallerClass());
		logger.error(e);
	}

	/**
	 * ��¼������־��Ϣ.
	 * 
	 * @param caller
	 * @param e
	 */
	public static void error(final Throwable e) {
		Logger logger = findLogger(getCallerClass());
		logger.error(e);
	}
}