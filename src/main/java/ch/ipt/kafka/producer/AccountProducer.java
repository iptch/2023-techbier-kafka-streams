package ch.ipt.kafka.producer;

import ch.ipt.kafka.techbier.Account;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Arrays;

//This class is only needed to produce data. probably someone else is already producing data
//@Configuration
public class AccountProducer {

    @Value("${source-topic-accounts}")
    private String sourceTopic;

    @Autowired
    KafkaProducer kafkaProducer;

    @Autowired
    private NewTopic createAccountTopic;

    public AccountProducer(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostConstruct
    public void sendAccounts() {
        Arrays.asList(AccountDataEnum.values())
                .forEach(
                        accountEnum -> {
                            Account account = AccountDataEnum.getAccount(accountEnum);
                            kafkaProducer.sendAccount(account, sourceTopic);
                        });
    }

}
