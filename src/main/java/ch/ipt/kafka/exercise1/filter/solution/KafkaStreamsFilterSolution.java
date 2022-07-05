package ch.ipt.kafka.exercise1.filter.solution;

import ch.ipt.kafka.techbier.Payment;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


//@Component
public class KafkaStreamsFilterSolution {

    @Value("${source-topic-transactions}")
    private String sourceTopic;

    @Value("${INITIALS}")
    private String initial;

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamsFilterSolution.class);
    private static final double LIMIT = 500.00;

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        String sinkTopic = "filtered-transactions-" + initial;

        //implement a filter which only sends payments over 500.- to a sink topic

        KStream<String, Payment> stream = streamsBuilder.stream(sourceTopic);

        stream
                .filter((key, payment) -> payment.getAmount() > LIMIT)
                .peek((key, payment) -> LOGGER.info("Message: key={}, value={}", key, payment))
                .to(sinkTopic);

        LOGGER.info(String.valueOf(streamsBuilder.build().describe()));
    }

}