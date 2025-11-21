package simu.framework;

import java.util.logging.Logger;

/**
 * Lightweight tracing utility used across the simulation framework.
 * Replaces ad-hoc System.out/System.err usage with a single place to control output.
 *
 * @author (your name)
 */
public class Trace {
	/**
	 * Logging levels for trace output.
	 */
	public enum Level { INFO, WAR, ERR }

	/** The current trace level. Only messages at or above this level are output. */
	private static Level traceLevel = Level.INFO; // default
	/** The logger instance used for output. */
	private static final Logger logger = Logger.getLogger("simu.framework.Trace");

	/**
	 * Sets the current trace level. Only messages at or above this level will be output.
	 * @param lvl the trace level to set
	 */
	public static void setTraceLevel(Level lvl){
		traceLevel = lvl;
	}

	/**
	 * Outputs a message at the given trace level if it meets the current threshold.
	 * Uses java.util.logging for output.
	 * @param lvl the level of the message
	 * @param txt the message text
	 */
	public static void out(Level lvl, String txt){
		if (traceLevel != null && lvl.ordinal() >= traceLevel.ordinal()){
			// Use java.util.logging so output can be redirected/configured by consumers
			switch (lvl) {
				case INFO:
					logger.info(txt);
					break;
				case WAR:
					logger.warning(txt);
					break;
				case ERR:
					logger.severe(txt);
					break;
				default:
					logger.info(txt);
			}
		}
	}
}