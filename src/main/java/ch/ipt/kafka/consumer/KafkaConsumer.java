package ch.ipt.kafka.consumer;

import ch.ipt.kafka.clients.avro.Account;
import ch.ipt.kafka.clients.avro.Payment;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


//@Component
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(id = "transactions-consumer",
            topics = "transactions")
    public void receiveTransactionMessage(ConsumerRecord<String, Payment> consumerRecord) {
        String key = consumerRecord.key();
        Payment value = consumerRecord.value();
        LOGGER.info("received credit message: key={}, value={}", key, value);
    }

    @KafkaListener(id = "accounts-consumer",
            topics = "accounts")
    public void receiveAccountMessage(ConsumerRecord<String, Account> consumerRecord) {
        String key = consumerRecord.key();
        Account value = consumerRecord.value();
        LOGGER.info("received credit message: key={}, value={}", key, value);
    }
}