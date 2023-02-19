package us.dot.its.jpo.geojsonconverter;

import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Handler for unhandled exceptions thrown from Streams topologies that
 *  logs the exception to a topic, and allows choosing the shutdown behavior.
 * 
 * See {@link https://cwiki.apache.org/confluence/display/KAFKA/KIP-671%3A+Introduce+Kafka+Streams+Specific+Uncaught+Exception+Handler}
 * for a description of the options.
 */
public class StreamsExceptionHandler implements StreamsUncaughtExceptionHandler {

    final static Logger logger = LoggerFactory.getLogger(StreamsExceptionHandler.class);

    final String topology;
    
    public StreamsExceptionHandler(String topology) {
        this.topology = topology;
    }
    
    @Override
    public StreamThreadExceptionResponse handle(Throwable exception) {

        logger.error(String.format("Uncaught exception in stream topology %s", topology), exception);
        // SHUTDOWN_CLIENT option shuts down quickly.
        return StreamThreadExceptionResponse.SHUTDOWN_CLIENT;
        
        // SHUTDOWN_APPLICATION shuts down more slowly, but cleans up more thoroughly
        //return StreamThreadExceptionResponse.SHUTDOWN_APPLICATION;

        // "Replace Thread" mode can be used to keep the streams client alive, 
        // however if the cause of the error was not transient, but due to a code error processing
        // a record, it can result in the record being repeatedly processed throwing the
        // same error
        //
        //return StreamThreadExceptionResponse.REPLACE_THREAD;
    }
    
}
