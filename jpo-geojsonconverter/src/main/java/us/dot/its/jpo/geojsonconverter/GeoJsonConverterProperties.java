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

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.processor.LogAndSkipOnInvalidTimestamp;

import us.dot.its.jpo.geojsonconverter.pojos.GeometryOutputMode;
import us.dot.its.jpo.ode.util.CommonUtils;

@ConfigurationProperties("geojsonconverter")
public class GeoJsonConverterProperties implements EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(GeoJsonConverterProperties.class);

    @Autowired
    private Environment env;

    // General Properties
    private String kafkaBrokers = null;
    private static final String DEFAULT_KAFKA_PORT = "9092";

    // Conluent Properties
    private boolean confluentCloudEnabled = false;
    private String confluentKey = null;
    private String confluentSecret = null;

    //SPAT
    private String kafkaTopicOdeSpatJson = "topic.OdeSpatJson";
    private String kafkaTopicProcessedSpat = "topic.ProcessedSpat";

    //MAP
    private String kafkaTopicOdeMapJson = "topic.OdeMapJson";
    private String kafkaTopicProcessedMap = "topic.ProcessedMap";
    private String kafkaTopicProcessedMapWKT = "topic.ProcessedMapWKT";

    //BSM
    private String kafkaTopicOdeBsmJson = "topic.OdeBsmJson";
    private String kafkaTopicProcessedBsm = "topic.ProcessedBsm";
    
    private GeometryOutputMode geometryOutputMode = GeometryOutputMode.GEOJSON_ONLY;

    private boolean jacksonAllowUnknownProperties = false;

    @PostConstruct
    public void initialize() {
        if (kafkaBrokers == null) {
            String dockerIp = CommonUtils.getEnvironmentVariable("DOCKER_HOST_IP");

            if (dockerIp == null) {
            logger.warn(
                    "Neither spring.kafka.bootstrap-servers property nor DOCKER_HOST_IP environment variable are defined. Defaulting to localhost.");
            dockerIp = "localhost";
            }
            kafkaBrokers = dockerIp + ":" + DEFAULT_KAFKA_PORT;
            logger.warn("spring.kafka.bootstrap-servers property not defined. Will try DOCKER_HOST_IP => {}", kafkaBrokers);
        }

        String kafkaType = CommonUtils.getEnvironmentVariable("KAFKA_TYPE");
        if (kafkaType != null) {
            confluentCloudEnabled = kafkaType.equals("CONFLUENT");
            if (confluentCloudEnabled) {
                confluentKey = CommonUtils.getEnvironmentVariable("CONFLUENT_KEY");
                confluentSecret = CommonUtils.getEnvironmentVariable("CONFLUENT_SECRET");
            }
        }

        // Adding configuration option to allow unknown properties when deserializing in Kafka streams
        // This is useful for when schema updates are made in the ODE
        // Boolean jacksonAllowUnknownPropertiesBool = Boolean.parseBoolean(CommonUtils.getEnvironmentVariable("JACKSON_ALLOW_UNKNOWN_PROPERTIES"));
        // logger.info(String.format("JACKSON_ALLOW_UNKNOWN_PROPERTIES is set to %s", jacksonAllowUnknownPropertiesBool));
        // if (jacksonAllowUnknownPropertiesBool) {
        //     logger.warn(String.format("JACKSON_ALLOW_UNKNOWN_PROPERTIES is set to %s", jacksonAllowUnknownPropertiesBool));
        //     this.jacksonAllowUnknownProperties = jacksonAllowUnknownPropertiesBool;
        // }
    }

    public Properties createStreamProperties(String name) {
        Properties streamProps = new Properties();
        streamProps.put(StreamsConfig.APPLICATION_ID_CONFIG, name);

        streamProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);

        streamProps.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG,
            LogAndContinueExceptionHandler.class.getName());
        streamProps.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
            LogAndSkipOnInvalidTimestamp.class.getName());

        streamProps.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 2);

        streamProps.put(StreamsConfig.producerPrefix(ProducerConfig.ACKS_CONFIG), "all");

        // Reduce cache buffering per topology to 1MB
        streamProps.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 1 * 1024 * 1024L);

        // Decrease default commit interval. Default for 'at least once' mode of 30000ms
        // is too slow.
        streamProps.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 100);

        // All the keys are Strings in this app
        streamProps.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        // Configure the state store location
        streamProps.put(StreamsConfig.STATE_DIR_CONFIG, "/var/lib/ode/kafka-streams");

        if (confluentCloudEnabled) {
            streamProps.put("ssl.endpoint.identification.algorithm", "https");
            streamProps.put("security.protocol", "SASL_SSL");
            streamProps.put("sasl.mechanism", "PLAIN");

            if (confluentKey != null && confluentSecret != null) {
                String auth = "org.apache.kafka.common.security.plain.PlainLoginModule required " +
                    "username=\"" + confluentKey + "\" " +
                    "password=\"" + confluentSecret + "\";";
                    streamProps.put("sasl.jaas.config", auth);
            }
            else {
                logger.error("Environment variables CONFLUENT_KEY and CONFLUENT_SECRET are not set. Set these in the .env file to use Confluent Cloud");
            }
        }

        return streamProps;
    }

    public String getKafkaBrokers() {
        return kafkaBrokers;
    }

    @Value("${spring.kafka.bootstrap-servers}")
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
		return kafkaTopicProcessedSpat;
	}

	public void setKafkaTopicSpatGeoJson(String kafkaTopicSpatGeoJson) {
		this.kafkaTopicProcessedSpat = kafkaTopicSpatGeoJson;
	}

	public String getKafkaTopicOdeMapJson() {
		return kafkaTopicOdeMapJson;
	}

	public void setKafkaTopicOdeMapJson(String kafkaTopicOdeMapJson) {
		this.kafkaTopicOdeMapJson = kafkaTopicOdeMapJson;
	}

    public String getKafkaTopicProcessedMap() {
		return kafkaTopicProcessedMap;
	}

	public void setKafkaTopicProcessedMap(String kafkaTopicMapGeoJson) {
		this.kafkaTopicProcessedMap = kafkaTopicMapGeoJson;
	}

    public String getKafkaTopicProcessedMapWKT() {
		return kafkaTopicProcessedMapWKT;
	}

	public void setKafkaTopicProcessedMapWKT(String kafkaTopicProcessedMapWKT) {
		this.kafkaTopicProcessedMapWKT = kafkaTopicProcessedMapWKT;
	}

    public String getKafkaTopicOdeBsmJson() {
		return kafkaTopicOdeBsmJson;
	}

	public void setKafkaTopicOdeBsmJson(String kafkaTopicOdeBsmJson) {
		this.kafkaTopicOdeBsmJson = kafkaTopicOdeBsmJson;
	}

    public String getKafkaTopicProcessedBsm() {
		return kafkaTopicProcessedBsm;
	}

	public void setKafkaTopicProcessedBsm(String kafkaTopicProcessedBsm) {
		this.kafkaTopicProcessedBsm = kafkaTopicProcessedBsm;
	}

    public Boolean getConfluentCloudStatus() {
		return confluentCloudEnabled;
	}

    @Value("${geometry.output.mode}")
    public void setGeometryOutputMode(String gomString) {
        if (GeometryOutputMode.findByName(gomString) != null)
            this.geometryOutputMode = GeometryOutputMode.findByName(gomString);
        else
            this.geometryOutputMode = GeometryOutputMode.GEOJSON_ONLY;
    }

    public GeometryOutputMode getGeometryOutputMode() {
        return geometryOutputMode;
    }
    
    @Value("${jackson.mapper.allow.unknown.properties}")
    public void setJacksonAllowUnknownProperties(String jacksonAllowUnknownProperties) {
        if (Boolean.parseBoolean(jacksonAllowUnknownProperties))
            this.jacksonAllowUnknownProperties = Boolean.parseBoolean(jacksonAllowUnknownProperties);
        else
            this.jacksonAllowUnknownProperties = false;
    }

    public boolean getJacksonAllowUnknownProperties() {
        return jacksonAllowUnknownProperties;
    }
}