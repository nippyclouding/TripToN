package server.TripToN.global.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

public class ClientAbortExceptionLogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        if (event == null || event.getThrowableProxy() == null) {
            return FilterReply.NEUTRAL;
        }

        if (isClientAbort(event.getThrowableProxy())) {
            return FilterReply.DENY;
        }

        return FilterReply.NEUTRAL;
    }

    private boolean isClientAbort(IThrowableProxy throwableProxy) {
        while (throwableProxy != null) {
            if (isClientAbort(throwableProxy.getClassName(), throwableProxy.getMessage())) {
                return true;
            }

            throwableProxy = throwableProxy.getCause();
        }

        return false;
    }

    private boolean isClientAbort(String className, String message) {
        return (className != null && className.contains("ClientAbortException"))
                || isBrokenPipeMessage(message);
    }

    private boolean isBrokenPipeMessage(String message) {
        return message != null && message.contains("Broken pipe");
    }
}
