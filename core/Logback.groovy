import groovy.transform.Field
@Grapes([
        @Grab(group = 'ch.qos.logback', module = 'logback-classic', version = '1.2.3')

])



import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.util.StatusPrinter


@Field
String tmpDir = System.getProperty("java.io.tmpdir");

def getLogger(String... args) {

    assert args: "Please input logFile (optional logPath)"

    logFile = args[0]
    logPath = args.size() > 1 ? args[1] : tmpDir



    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    RollingFileAppender rfAppender = new RollingFileAppender();
    rfAppender.setContext(loggerContext);


    TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
    rollingPolicy.setContext(loggerContext);
    rollingPolicy.setParent(rfAppender);

    rollingPolicy.setFileNamePattern("${logPath}/${logFile}.%d{yyyy-MM-dd-HH}.log");

    rollingPolicy.setMaxHistory(6)
    rollingPolicy.start();


    PatternLayoutEncoder encoder = new PatternLayoutEncoder();
    encoder.setContext(loggerContext);
    encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{35} - %msg%n");
    encoder.start();

    rfAppender.setEncoder(encoder);
    rfAppender.setRollingPolicy(rollingPolicy);

    rfAppender.start();

    // attach the rolling file appender to the logger of your choice
    Logger logger = loggerContext.getLogger(logFile);
    logger.addAppender(rfAppender);

    // OPTIONAL: print logback internal status messages
    StatusPrinter.print(loggerContext);


    return logger;
}

def getDataLogger(String... args) {

    assert args: "Please input logFile (optional logPath)"

    logFile = args[0]
    logPath = args.size() > 1 ? args[1] : tmpDir

    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

    RollingFileAppender rfAppender = new RollingFileAppender();
    rfAppender.setContext(loggerContext);


    TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
    rollingPolicy.setContext(loggerContext);
    rollingPolicy.setParent(rfAppender);
    rollingPolicy.setFileNamePattern("${logPath}/${logFile}.%d{yyyy-MM-dd-HH}.log");
    rollingPolicy.start();


    PatternLayoutEncoder encoder = new PatternLayoutEncoder();
    encoder.setContext(loggerContext);

    encoder.setPattern("%msg%n");
    encoder.start();

    rfAppender.setEncoder(encoder);
    rfAppender.setRollingPolicy(rollingPolicy);

    rfAppender.start();

    // attach the rolling file appender to the logger of your choice
    Logger logger = loggerContext.getLogger(logFile);
    logger.addAppender(rfAppender);
    // don't print on the console
    logger.setAdditive(false)

    // OPTIONAL: print logback internal status messages
    StatusPrinter.print(loggerContext);


    return logger;
}

