/*******************************************************************************
 * Copyright 2018 572682
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package us.dot.its.jpo.geojsonconverter;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.processor.LogAndSkipOnInvalidTimestamp;

import us.dot.its.jpo.ode.util.CommonUtils;

@ConfigurationProperties("geojsonconverter")
@PropertySource("classpath:application.properties")
public class GeoJsonConverterProperties implements EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(GeoJsonConverterProperties.class);

    @Autowired
    private Environment env;

    /*
    * General Properties
    */
    private String kafkaBrokers = null;
    private static final String DEFAULT_KAFKA_PORT = "9092";

    //SPAT
    private String kafkaTopicOdeSpatJson = "topic.OdeSpatJson";
    private String kafkaTopicSpatGeoJson = "topic.SpatGeoJson";

    //MAP
    private String kafkaTopicOdeMapJson = "topic.OdeMapJson";
    private String kafkaTopicMapGeoJson = "topic.MapGeoJson";

    @Autowired
    BuildProperties buildProperties;

    @PostConstruct
    void initialize() {
        logger.info("groupId: {}", buildProperties.getGroup());
        logger.info("artifactId: {}", buildProperties.getArtifact());
        logger.info("version: {}", buildProperties.getVersion());

        if (kafkaBrokers == null) {

            logger.info("geojsonconverter.kafkaBrokers property not defined. Will try DOCKER_HOST_IP => {}", kafkaBrokers);

            String dockerIp = CommonUtils.getEnvironmentVariable("DOCKER_HOST_IP");

            if (dockerIp == null) {
            logger.warn(
                    "Neither geojsonconverter.kafkaBrokers property nor DOCKER_HOST_IP environment variable are defined. Defaulting to localhost.");
            dockerIp = "localhost";
            }
            kafkaBrokers = dockerIp + ":" + DEFAULT_KAFKA_PORT;
        }
    }

    public Properties createStreamProperties(String name) {
        Properties lcsProps = new Properties();
        lcsProps.put(StreamsConfig.APPLICATION_ID_CONFIG, name);

        lcsProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);

        lcsProps.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
            LogAndContinueExceptionHandler.class.getName());
        lcsProps.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
            LogAndSkipOnInvalidTimestamp.class.getName());

        lcsProps.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);

        lcsProps.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG), "all");

        // Reduce cache buffering per topology to 1MB
        lcsProps.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 1 * 1024 * 1024L);

        // Decrease default commit interval. Default for 'at least once' mode of 30000ms
        // is too slow.
        lcsProps.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);

        // All the keys are Strings in this app
        lcsProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // Configure the state store location
        lcsProps.put(StreamsConfig.STATE_DIR_CONFIG, "/var/lib/odd/kafka-streams");

        return lcsProps;
    }

    public String getKafkaBrokers() {
        return kafkaBrokers;
    }

    public void setKafkaBrokers(String kafkaBrokers) {
        this.kafkaBrokers = kafkaBrokers;
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }

	public String getKafkaTopicOdeSpatJson() {
		return kafkaTopicOdeSpatJson;
	}

	public void setKafkaTopicOdeSpatJson(String kafkaTopicOdeSpatJson) {
		this.kafkaTopicOdeSpatJson = kafkaTopicOdeSpatJson;
	}

    public String getKafkaTopicSpatGeoJson() {
		return kafkaTopicSpatGeoJson;
	}

	public void setKafkaTopicSpatGeoJson(String kafkaTopicSpatGeoJson) {
		this.kafkaTopicSpatGeoJson = kafkaTopicSpatGeoJson;
	}

	public String getKafkaTopicOdeMapJson() {
		return kafkaTopicOdeMapJson;
	}

	public void setKafkaTopicOdeMapJson(String kafkaTopicOdeMapJson) {
		this.kafkaTopicOdeMapJson = kafkaTopicOdeMapJson;
	}

    public String getKafkaTopicMapGeoJson() {
		return kafkaTopicMapGeoJson;
	}

	public void setKafkaTopicMapGeoJson(String kafkaTopicMapGeoJson) {
		this.kafkaTopicMapGeoJson = kafkaTopicMapGeoJson;
	}
}