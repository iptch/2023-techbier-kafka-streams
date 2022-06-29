package ch.ipt.kafka.exercise5;

import ch.ipt.kafka.clients.avro.Payment;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;


//@Component
public class KafkaStreamsAggregate {

    @Value("${source-topic-transactions}")
    private String sourceTopic;
    private String sinkTopic = "average-of-transactions";



    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamsAggregate.class);
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<Payment> PAYMENT_SERDE = new SpecificAvroSerde<>();

    public KafkaStreamsAggregate(@Value("${spring.kafka.properties.schema.registry.url}") String schemaRegistry) {
        Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
        streamsConfiguration.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        final Map<String, String> serdeConfig = Collections.singletonMap(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistry);
        PAYMENT_SERDE.configure(serdeConfig, false); // `false` for record values
    }

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {

        //compute the average of all transactions


        LOGGER.info(String.valueOf(streamsBuilder.build().describe()));
    }

}