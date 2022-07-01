package ch.ipt.kafka.exercise5.aggregate.solution;

import ch.ipt.kafka.techbier.Payment;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class KafkaStreamsAggregateSolution {

    @Value("${source-topic-transactions}")
    private String sourceTopic;

    @Value("${INITIALS}")
    private String initial;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamsAggregateSolution.class);
    private static final Serde<String> STRING_SERDE = Serdes.String();
    private static final Serde<Double> DOUBLE_SERDE = Serdes.Double();
    private static final Serde<Payment> PAYMENT_SERDE = new SpecificAvroSerde<>();

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        String sinkTopic = "total-of-transactions-" + initial;

        //compute the total of all transactions per account (e.g. 1: 1632.45, 2: 256.00, ...)

        KStream<String, Double> groupedStream = streamsBuilder.stream(sourceTopic, Consumed.with(STRING_SERDE, PAYMENT_SERDE))
                .map((key, value) -> new KeyValue<>(
                        value.getCardType().toString(), value
                ))
                .groupByKey()
                .aggregate(
                        () -> 0.0,
                            (key, payment, total) -> total + payment.getAmount(), Materialized.with(Serdes.String(), Serdes.Double())
                )
                .toStream()
                .peek((key, value) -> LOGGER.info("Outgoing record - key " +key +" value " + value));

                groupedStream.to(sinkTopic, Produced.with(STRING_SERDE, DOUBLE_SERDE));

                LOGGER.info(String.valueOf(streamsBuilder.build().describe()));
    }

}