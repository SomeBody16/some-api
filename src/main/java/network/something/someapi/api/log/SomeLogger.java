package network.something.someapi.api.log;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class SomeLogger {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final String modId;

    public SomeLogger(String modId) {
        this.modId = modId;
    }

    private String prepareMessage(String message, String level) {
        return "[%s] [%s] %s".formatted(modId, level, message);
    }

    public void info(String format, Object... arguments) {
        format = prepareMessage(format, "INFO");
        LOGGER.info(format, arguments);
    }

    public void warn(String format, Object... arguments) {
        format = prepareMessage(format, "WARN");
        LOGGER.warn(format, arguments);
    }

    public void error(String format, Object... arguments) {
        format = prepareMessage(format, "ERROR");
        LOGGER.error(format, arguments);
    }

    public void error(String format, Throwable e) {
        format = prepareMessage(format, "ERROR");
        LOGGER.error(format, e);
    }
}
