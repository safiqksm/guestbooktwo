package be.crydust.org.eclipse.persistence.logging;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;

//import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.sessions.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EclipseLink Logger bridge over SLF4J.
 *
 * @author Jaro Kuruc
 * @see https://bugs.eclipse.org/bugs/show_bug.cgi?id=296391
 */
public class SLF4JLogger extends AbstractSessionLog {

    /**
     * Stores the default session name in case there is the session name is
     * missing.
     */
    public static final String ECLIPSELINK_NAMESPACE = "org.eclipse.persistence";
    public static final String DEFAULT_ECLIPSELINK_NAMESPACE = ECLIPSELINK_NAMESPACE + ".default";
    public static final String SESSION_ECLIPSELINK_NAMESPACE = ECLIPSELINK_NAMESPACE + ".session";

    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(DEFAULT_ECLIPSELINK_NAMESPACE);

    /**
     * Stores the namespace for session, i.e.
     * "org.eclipse.persistence.session.<sessionname>".
     */
    private String sessionNameSpace;

    /**
     * Stores eagerly initialised loggers.
     */
    private final Map<String, Logger> categoryLoggers = new HashMap<>();

    /**
     * INTERNAL: Add logger to the catagoryLoggers.
     */
    protected void addLogger(String loggerCategory, String loggerNameSpace) {
        categoryLoggers.put(loggerCategory, LoggerFactory.getLogger(loggerNameSpace));
    }

    /**
     * INTERNAL: Returns the Logger for the given category
     */
    protected Logger getLogger(String category) {
        Logger logger = categoryLoggers.get(category);
        if (logger != null) {
            return logger;
        }
        logger = categoryLoggers.get(sessionNameSpace);
        if (logger != null) {
            return logger;
        }
        return DEFAULT_LOGGER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSession(Session session) {
        super.setSession(session);
        if (session != null) {
            String sessionName = session.getName();
//            if (StringUtils.isNotBlank(sessionName)) {
            if (sessionName != null
                    && sessionName.length() != 0
                    && sessionName.trim().length() != 0) {
                sessionNameSpace = SESSION_ECLIPSELINK_NAMESPACE + "." + sessionName;

                // Initialise loggers eagerly
                addLogger(sessionNameSpace, sessionNameSpace);
                for (int i = 0; i < loggerCatagories.length; i++) {
                    String loggerCategory = loggerCatagories[i];
                    String loggerNameSpace = sessionNameSpace + "." + loggerCategory;
                    addLogger(loggerCategory, loggerNameSpace);
                }
            }
        }
    }

    /**
     * INTERNAL: Prints SessionLogEntry to a logger using given callback.
     */
    protected void logEntry(SessionLogEntry logEntry, LoggerCallback callback) {
        if (logEntry.hasException()) {
            if (shouldLogExceptionStackTrace()) {
                callback.log(null, logEntry.getException());
            } else {
                callback.log("{}", logEntry.getException().toString());
            }
        } else {
            callback.log("{}", formatMessage(logEntry));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(final SessionLogEntry logEntry) {
        if (logEntry != null) {
            final Logger logger = getLogger(logEntry.getNameSpace());

            switch (logEntry.getLevel()) {
                case ALL:
                case FINEST:
                    if (logger.isTraceEnabled()) {
                        logEntry(logEntry, new LoggerCallback() {
                            @Override
                            public void log(String msg, Throwable t) {
                                logger.trace(msg, t);
                            }

                            @Override
                            public void log(String format, String arg) {
                                logger.trace(format, arg);
                            }
                        });
                    }
                    break;
                case FINER:
                case FINE:
                    if (logger.isDebugEnabled()) {
                        logEntry(logEntry, new LoggerCallback() {
                            @Override
                            public void log(String msg, Throwable t) {
                                logger.debug(msg, t);
                            }

                            @Override
                            public void log(String format, String arg) {
                                logger.debug(format, arg);
                            }
                        });
                    }
                    break;
                case CONFIG:
                case INFO:
                    if (logger.isInfoEnabled()) {
                        logEntry(logEntry, new LoggerCallback() {
                            @Override
                            public void log(String msg, Throwable t) {
                                logger.info(msg, t);
                            }

                            @Override
                            public void log(String format, String arg) {
                                logger.info(format, arg);
                            }
                        });
                    }
                    break;
                case WARNING:
                    if (logger.isWarnEnabled()) {
                        logEntry(logEntry, new LoggerCallback() {
                            @Override
                            public void log(String msg, Throwable t) {
                                logger.warn(msg, t);
                            }

                            @Override
                            public void log(String format, String arg) {
                                logger.warn(format, arg);
                            }
                        });
                    }
                    break;
                case SEVERE:
                    if (logger.isErrorEnabled()) {
                        logEntry(logEntry, new LoggerCallback() {
                            @Override
                            public void log(String msg, Throwable t) {
                                logger.error(msg, t);
                            }

                            @Override
                            public void log(String format, String arg) {
                                logger.error(format, arg);
                            }
                        });
                    }
                    break;
                case OFF:
                default:
                    break;
            }
        }
    }

    protected interface LoggerCallback {

        void log(String msg, Throwable t);

        void log(String format, String arg);
    }

}
