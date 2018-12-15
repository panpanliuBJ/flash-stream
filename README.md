# flash-stream

- aggregate user share by time window


# Quickstart
```shell
mvn clean package
java -jar flash-stream.jar
```

# streaming data
## setup [json-data-generator](https://github.com/acesinc/json-data-generator)
## generator mock data
```
cd streaming-workflows/json-data-generator-1.3.1-SNAPSHOT
./startup.sh
```
##  run stream processor
##  viewing the results

```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic btc-user-share-min5
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic btc-user-share-min15
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092  --topic btc-user-share-hour

```

## stream topology describe

```
2018-12-15 10:16:55.941|INFO |main|c.f.f.s.k.t.StreamsStarter|37|topology=Topologies:
   Sub-topology: 0
    Source: KSTREAM-SOURCE-0000000000 (topics: [btc-share])
      --> KSTREAM-FILTER-0000000001
    Processor: KSTREAM-FILTER-0000000001 (stores: [])
      --> KSTREAM-FLATMAPVALUES-0000000002
      <-- KSTREAM-SOURCE-0000000000
    Processor: KSTREAM-FLATMAPVALUES-0000000002 (stores: [])
      --> KSTREAM-FILTER-0000000003
      <-- KSTREAM-FILTER-0000000001
    Processor: KSTREAM-FILTER-0000000003 (stores: [])
      --> KSTREAM-SINK-0000000004
      <-- KSTREAM-FLATMAPVALUES-0000000002
    Sink: KSTREAM-SINK-0000000004 (topic: btc-flatten-share)
      <-- KSTREAM-FILTER-0000000003

  Sub-topology: 1
    Source: KSTREAM-SOURCE-0000000005 (topics: [btc-flatten-share])
      --> KSTREAM-FILTER-0000000006, KSTREAM-FILTER-0000000016, KSTREAM-FILTER-0000000026
    Processor: KSTREAM-FILTER-0000000006 (stores: [])
      --> KSTREAM-MAP-0000000007
      <-- KSTREAM-SOURCE-0000000005
    Processor: KSTREAM-FILTER-0000000016 (stores: [])
      --> KSTREAM-MAP-0000000017
      <-- KSTREAM-SOURCE-0000000005
    Processor: KSTREAM-FILTER-0000000026 (stores: [])
      --> KSTREAM-MAP-0000000027
      <-- KSTREAM-SOURCE-0000000005
    Processor: KSTREAM-MAP-0000000007 (stores: [])
      --> KSTREAM-KEY-SELECT-0000000008
      <-- KSTREAM-FILTER-0000000006
    Processor: KSTREAM-MAP-0000000017 (stores: [])
      --> KSTREAM-KEY-SELECT-0000000018
      <-- KSTREAM-FILTER-0000000016
    Processor: KSTREAM-MAP-0000000027 (stores: [])
      --> KSTREAM-KEY-SELECT-0000000028
      <-- KSTREAM-FILTER-0000000026
    Processor: KSTREAM-KEY-SELECT-0000000008 (stores: [])
      --> KSTREAM-FILTER-0000000011
      <-- KSTREAM-MAP-0000000007
    Processor: KSTREAM-KEY-SELECT-0000000018 (stores: [])
      --> KSTREAM-FILTER-0000000021
      <-- KSTREAM-MAP-0000000017
    Processor: KSTREAM-KEY-SELECT-0000000028 (stores: [])
      --> KSTREAM-FILTER-0000000031
      <-- KSTREAM-MAP-0000000027
    Processor: KSTREAM-FILTER-0000000011 (stores: [])
      --> KSTREAM-SINK-0000000010
      <-- KSTREAM-KEY-SELECT-0000000008
    Processor: KSTREAM-FILTER-0000000021 (stores: [])
      --> KSTREAM-SINK-0000000020
      <-- KSTREAM-KEY-SELECT-0000000018
    Processor: KSTREAM-FILTER-0000000031 (stores: [])
      --> KSTREAM-SINK-0000000030
      <-- KSTREAM-KEY-SELECT-0000000028
    Sink: KSTREAM-SINK-0000000010 (topic: user-share-store-min5-repartition)
      <-- KSTREAM-FILTER-0000000011
    Sink: KSTREAM-SINK-0000000020 (topic: user-share-store-min15-repartition)
      <-- KSTREAM-FILTER-0000000021
    Sink: KSTREAM-SINK-0000000030 (topic: user-share-store-hour-repartition)
      <-- KSTREAM-FILTER-0000000031

  Sub-topology: 2
    Source: KSTREAM-SOURCE-0000000012 (topics: [user-share-store-min5-repartition])
      --> KSTREAM-AGGREGATE-0000000009
    Processor: KSTREAM-AGGREGATE-0000000009 (stores: [user-share-store-min5])
      --> KTABLE-TOSTREAM-0000000013
      <-- KSTREAM-SOURCE-0000000012
    Processor: KTABLE-TOSTREAM-0000000013 (stores: [])
      --> KSTREAM-FOREACH-0000000015, KSTREAM-SINK-0000000014
      <-- KSTREAM-AGGREGATE-0000000009
    Processor: KSTREAM-FOREACH-0000000015 (stores: [])
      --> none
      <-- KTABLE-TOSTREAM-0000000013
    Sink: KSTREAM-SINK-0000000014 (topic: btc-user-share-min5)
      <-- KTABLE-TOSTREAM-0000000013

  Sub-topology: 3
    Source: KSTREAM-SOURCE-0000000022 (topics: [user-share-store-min15-repartition])
      --> KSTREAM-AGGREGATE-0000000019
    Processor: KSTREAM-AGGREGATE-0000000019 (stores: [user-share-store-min15])
      --> KTABLE-TOSTREAM-0000000023
      <-- KSTREAM-SOURCE-0000000022
    Processor: KTABLE-TOSTREAM-0000000023 (stores: [])
      --> KSTREAM-FOREACH-0000000025, KSTREAM-SINK-0000000024
      <-- KSTREAM-AGGREGATE-0000000019
    Processor: KSTREAM-FOREACH-0000000025 (stores: [])
      --> none
      <-- KTABLE-TOSTREAM-0000000023
    Sink: KSTREAM-SINK-0000000024 (topic: btc-user-share-min15)
      <-- KTABLE-TOSTREAM-0000000023

  Sub-topology: 4
    Source: KSTREAM-SOURCE-0000000032 (topics: [user-share-store-hour-repartition])
      --> KSTREAM-AGGREGATE-0000000029
    Processor: KSTREAM-AGGREGATE-0000000029 (stores: [user-share-store-hour])
      --> KTABLE-TOSTREAM-0000000033
      <-- KSTREAM-SOURCE-0000000032
    Processor: KTABLE-TOSTREAM-0000000033 (stores: [])
      --> KSTREAM-FOREACH-0000000035, KSTREAM-SINK-0000000034
      <-- KSTREAM-AGGREGATE-0000000029
    Processor: KSTREAM-FOREACH-0000000035 (stores: [])
      --> none
      <-- KTABLE-TOSTREAM-0000000033
    Sink: KSTREAM-SINK-0000000034 (topic: btc-user-share-hour)
      <-- KTABLE-TOSTREAM-0000000033

```

# contact

Email: finleyliupan@gmail.com
