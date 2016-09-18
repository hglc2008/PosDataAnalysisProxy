/**
 * @(#) LoggerUtil.java
 * module  : util
 * version : 版本管理系统中的文件版本
 * date    : 2008-12-4
 * name    : 马仁配
 */
package com.allinpay.util;

import java.util.HashMap;
import java.util.Map;

import sun.reflect.Reflection;

/**
 * 如果有任何对代码的修改,请按下面的格式注明修改的内容. 序号 时间 作者 修改内容 1. 2008-12-4 马仁配 created this
 * class.
 */
public class LoggerUtil {
	/**
	 * 所有的日志记录对象.
	 */
	private static Map loggers = new HashMap();

	/**
	 * 缺省的日志记录对象.
	 */
	private static Logger defaultLogger = Logger.getLogger(LoggerUtil.class);

	/**
	 * 从缓存loggers中获取指定对象的日志记录对象.
	 * 
	 * @param caller
	 *            Object 调用记录日志的对象.
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
	 * 创建一个logger对象并缓存.
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
	 * 根据调用对象查找对应的日志记录对象,如果没有则创建一个日志记录对象.
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
	 * 得到调用LoggerUtil类的debug、info、error方法的调用者所属的类名.
	 * 
	 * @return
	 */
	private static Class getCallerClass() {
		return Reflection.getCallerClass(3);
	}

	/**
	 * 记录调式日志信息.
	 * 
	 * @param caller
	 * @param msg
	 */
	public static void debug(final String msg) {
		Logger logger = findLogger(getCallerClass());
		logger.debug(msg);
	}

	/**
	 * 记录提示日志信息.
	 * 
	 * @param caller
	 * @param msg
	 */
	public static void info(final String msg) {
		Logger logger = findLogger(getCallerClass());
		logger.info(msg);
	}

	/**
	 * 记录警告日志信息.
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
	 * 记录警告日志信息.
	 * 
	 * @param caller
	 * @param msg
	 */
	public static void warn(final String msg) {
		Logger logger = findLogger(getCallerClass());
		logger.warn(msg);
	}

	/**
	 * 记录警告日志信息.
	 * 
	 * @param caller
	 * @param e
	 */
	public static void warn(final Exception e) {
		Logger logger = findLogger(getCallerClass());
		logger.warn(e);
	}

	/**
	 * 记录警告日志信息.
	 * 
	 * @param caller
	 * @param e
	 */
	public static void warn(final Throwable e) {
		Logger logger = findLogger(getCallerClass());
		logger.warn(e);
	}

	/**
	 * 记录错误日志信息.
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
	 * 记录错误日志信息.
	 * 
	 * @param caller
	 * @param msg
	 */
	public static void error(final String msg) {
		Logger logger = findLogger(getCallerClass());
		logger.error(msg);
	}

	/**
	 * 记录错误日志信息.
	 * 
	 * @param caller
	 * @param e
	 */
	public static void error(final Exception e) {
		Logger logger = findLogger(getCallerClass());
		logger.error(e);
	}

	/**
	 * 记录错误日志信息.
	 * 
	 * @param caller
	 * @param e
	 */
	public static void error(final Throwable e) {
		Logger logger = findLogger(getCallerClass());
		logger.error(e);
	}
}