# Umgebung konfigurieren

Diese App produziert, konsumiert und verarbeitet Kafka Events. 

Es gibt 2 Umgebuingen welche konfigurierte werden können:

* Confluent Cloud
* Lokaler Redpanda Cluster

## Confluent Cloud

1. Aktives Spring Boot Profile auf `confluent` setzen: [Anleitung IntelliJ](https://www.jetbrains.com/help/idea/run-debug-configuration-spring-boot.html#modify-options)

2. In `src/main/resources/application-confluent.yml` folgende Werte befüllen.
   * INITIALS (z.B. `PGR`)
   * **Kafka Config:**
     * KAFKA_KEY
     * KAFKA_SECRET
   
   * **Schema Registry Config:**
     * SR_API_KEY
     * SR_API_SECRET

   (Kafka & Schema Registry Config werden dir von CSU und DKN zur Verfügung gestellt.)

3. Falls persönlicher Kafka Cluster verwendet wird:

   Kafka Producer aktivieren
   
   In Klasse `ch.ipt.kafka.producer.PaymentProducer` & `ch.ipt.kafka.producer.AccountProducer` die `@Configuration` Annotation aktivieren.

4. Maven `compile` ausführen um die Klassen aus den avro Schemas zu generieren.
5. In IntelliJ den Ordner mit den generierten Klassen als `Generated Sources Root` markieren


## Lokaler Redpanda Cluster

1. Redpanda starten (siehe unten)
2. Redpanda Profil konfigurieren

   In `src/main/resources/application-redpanda.yml` folgende Werte befüllen.
   * INITIALS (z.B. `PGR-redpanda`)  

3. Redpanda Profil aktivieren

   Aktives Spring Boot Profile auf `redpanda` setzen: [Anleitung IntelliJ](https://www.jetbrains.com/help/idea/run-debug-configuration-spring-boot.html#modify-options)

4. Kafka Producer aktivieren

   In Klasse `ch.ipt.kafka.producer.PaymentProducer` & `ch.ipt.kafka.producer.AccountProducer` die `@Configuration` Annotation aktivieren.


### Start Redpanda
```
cd src/main/resources/redpanda
docker-compose up -d
docker-compose logs -f
```

### Redpanda UI
Auf http://localhost:8080 findest du alle Informationen zu deiner Redpanda Instanz. 

### Redpanda zurücksetzen
```
cd src/main/resources/redpanda
docker-compose kill
docker-compose rm
```

## App starten

Topics `accounts`& `transactions` erstellen (2 partitions, delete after 1w). Dies ist nur einmal nötig und wird von den Coaches gemacht.

Die Klasse `ch.ipt.kafka.TechBierKafkaStreamsApplication` starten (Vorher muss korrektes Intellij Profil gesetzt werden)

Du solltest jetzt in den Topics `accounts`& `transactions` messages sehen. Du kannst jetzt auf diese reagieren.

Auf dem M1 MacBook hatte ich einen LinkerError ab Aufgabe 4 wo die State Stores ins Spiel kommen.
Grund war, dass ich auf JDK17 eingestellt hatte. Mit OpenJDK11 ging es dann.

## App erweitern

Aktivere die jeweiligen Klassen in `exerciseX.X` dort kannst du einzelne Streaming Applikationen erstellen.

Die wichtigste Dokumentationsseite ist die DSL Doku von Confluent: https://docs.confluent.io/platform/current/streams/developer-guide/dsl-api.html
Für mehr Details ist auch die Java API manchmal nützlich: https://docs.confluent.io/platform/current/streams/javadocs/javadoc/org/apache/kafka/streams/kstream/KStream.html

## Streams Topologie visualisieren

Die Streams DSL generiert und loggt zur Laufzeit eine Topologie von Processor Nodes.
Diese kann mit folgendem Tool leicht visualisiert werden: https://zz85.github.io/kafka-streams-viz/
