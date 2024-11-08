package com.learn.finance.utils;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class TestUtils {


    @SneakyThrows
    public static String readMockJson(final String name) {
        String s = new String(Files.readAllBytes(Paths.get(
                TestUtils.class.getResource("/wiremock/__files/" + name).toURI())));
        return s;
    }

    public static <T> T deserialize(final ObjectMapper objectMapper, final String content, final Class<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static <T> T deserialize(final ObjectMapper objectMapper, final String content, final TypeReference<T> valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static List<String> executeCollectingLogs(final Runnable action) {
        final CapturingAppender capturingAppender = new CapturingAppender();
        try {
            action.run();
        } finally {
            capturingAppender.stopCapturing();
        }
        return capturingAppender.getMessages();
    }


    private static class CapturingAppender extends AppenderBase<ILoggingEvent> {
        @Getter
        private final List<String> messages = new ArrayList<>();
        private final Logger logger;

        CapturingAppender() {
            logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            logger.addAppender(this);
        }

        @Override
        public void doAppend(final ILoggingEvent event) {
            messages.add(event.toString());
        }

        @Override
        protected void append(final ILoggingEvent loggingEvent) {
            // This is never called in my impl
        }

        void stopCapturing() {
            logger.detachAppender(this);
        }
    }
}
