package com.billing.app.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;

public class LogUtility {
	public static void debug(HttpServletRequest req, Logger logger, String msg) {
		if (req.getSession() != null && req.getSession().getId() != null) {
			logger.debug("[" + req.getSession().getId() + "] " + req.getRemoteAddr() + " " + getCallerClassName() + ": " + msg);
		} else {
			logger.debug(req.getRemoteAddr() + " " + getCallerClassName() + ": " + msg);
		}
	}

	public static void info(HttpServletRequest req, Logger logger, String msg) {
		if (req.getSession() != null && req.getSession().getId() != null) {
			logger.info("[" + req.getSession().getId() + "] " + req.getRemoteAddr() + " " + getCallerClassName() + ": " + msg);
		} else {
			logger.info(req.getRemoteAddr() + " " + getCallerClassName() + ": " + msg);
		}
	}

	public static void warn(HttpServletRequest req, Logger logger, String msg) {
		if (req.getSession() != null && req.getSession().getId() != null) {
			logger.warn("[" + req.getSession().getId() + "] " + req.getRemoteAddr() + " " + getCallerClassName() + ": " + msg);
		} else {
			logger.warn(req.getRemoteAddr() + " " + getCallerClassName() + ": " + msg);
		}
	}

	public static void error(HttpServletRequest req, Logger logger, String msg) {
		if (req.getSession() != null && req.getSession().getId() != null) {
			logger.error("[" + req.getSession().getId() + "] " + req.getRemoteAddr() + " " + getCallerClassName() + ": " + msg);
		} else {
			logger.error(req.getRemoteAddr() + " " + getCallerClassName() + ": " + msg);
		}
	}

	public static void error(HttpServletRequest req, Logger logger, String msg, Throwable obj) {
		if (req.getSession() != null && req.getSession().getId() != null) {
			logger.error("[" + req.getSession().getId() + "] " + req.getRemoteAddr() + " " + getCallerClassName() + ": " + msg, obj);
		} else {
			logger.error(req.getRemoteAddr() + " " + getCallerClassName() + ": " + msg, obj);
		}
	}

	public static String getCallerClassName() {
		try {
			StackTraceElement[] stElements = Thread.currentThread().getStackTrace();

			if (stElements.length >= 4) {
				int startAt = 3;
				int maxAttempt = 10;

				int current = startAt;
				String fullMethodClassPath = null;

				while ((fullMethodClassPath == null || fullMethodClassPath.contains("LogUtility")) && (current < maxAttempt)) {
					StackTraceElement callerClass = stElements[current];

					fullMethodClassPath = callerClass.getClassName().replace("com.ewx.ebpp.", "") + "." + callerClass.getMethodName() + ":" + callerClass.getLineNumber() + " ";

					current++;
				}

				return fullMethodClassPath;
			}
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

		return "";
	}
}