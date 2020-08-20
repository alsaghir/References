# Apache Kafka

## Rules of thumbs and best practices about topics splitting for pub/sub pattern

- 10 partitions per topic, and 10,000 partitions per Kafka cluster
- No. of Partitions = Desired Throughput / Partition Speed
- Replication Factor refer to backups. 3 is the default.
- Any events that need to stay in a fixed order must go in the same topic (and they must also use the same partitioning key). Most commonly, the order of events matters if they are about the same entity.
- About different entities, if event related to all of them then combine all details in single atomic message and publish it in single topic.
- If several consumers all read a particular group of topics, this suggests that maybe those topics should be combined
- As a rule of thumb, if you care about latency, it’s probably a good idea to limit the number of partitions per broker to 100 x b x r, where b is the number of brokers in a Kafka cluster and r is the replication factor.
- Consumer subscribing to sent messages mechanism and acts based on it using spring-kafka for example.
- Use identifier as partition key. For example user id for User entity.
- Ion format
  - message converter
  - Serializer / Deserializer
  - Configure producer & kafka template
  - Consumer & Listener

## Settings recommendation

### ZooKeeper

`4lw.commands.whitelist=*`

### Broker

`allow.auto.create.topics=false` - Allows automatic topic creation on the broker when subscribing to or assigning a topic.
`log.retention.bytes=1000000000` - The maximum size of the log before deleting it
`log.retention.hours=168` - The number of hours to keep a log file before deleting it (in hours), tertiary to log.retention.ms property

### Consumer

`socket.receive.buffer.bytes = 1024000`

### Producer

`retries` - guards against situations where the broker leading the partition isn’t able to respond to a produce request right away
`buffer.memory` - high-throughput producers, tune buffer sizes
`batch.size` - high-throughput producers, tune buffer sizes

## Kafka Architectureee difenetion

**Message**: A record or unit of data within Kafka. Each message has a key and a value, and optionally headers.

**Producer**: Producers publish messages to Kafka topics. Producers decide which topic partition to publish to either randomly (round-robin) or using a partitioning algorithm based on a message’s key.

**Broker**: Kafka runs in a distributed system or cluster. Each node in the cluster is called a broker.

**Topic**: A topic is a category to which data records—or messages—are published. Consumers subscribe to topics in order to read the data written to them.

**Topic partition**: Topics are divided into partitions, and each message is given an offset. Each partition is typically replicated at least once or twice. Each partition has a leader and one or more replicas (copies of the data) that exist on followers, providing protection against a broker failure. All brokers in the cluster are both leaders and followers, but a broker has at most one replica of a topic partition. The leader is used for all reads and writes.

**Offset**: Each message within a partition is assigned an offset, a monotonically increasing integer that serves as a unique identifier for the message within the partition.

**Consumer**: Consumers read messages from Kafka topics by subscribing to topic partitions. The consuming application then processes the message to accomplish whatever work is desired.

**Consumer group**: Consumers can be organized into logic consumer groups. Topic partitions are assigned to balance the assignments among all consumers in the group. Within a consumer group, all consumers work in a load-balanced mode; in other words, each message will be seen by one consumer in the group. If a consumer goes away, the partition is assigned to another consumer in the group. This is referred to as a rebalance. If there are more consumers in a group than partitions, some consumers will be idle. If there are fewer consumers in a group than partitions, some consumers will consume messages from more than one partition.

**Lag**: A consumer is lagging when it’s unable to read from a partition as fast as messages are produced to it. Lag is expressed as the number of offsets that are behind the head of the partition. The time required to recover from lag (to “catch up”) depends on how quickly the consumer is able to consume messages per second:

`time = messages / (consume rate per second - produce rate per second)`

## Commands

- Servers

Start kafka zookeeper and broker each with specified config file

`bin/zookeeper-server-start.sh config/zookeeper.properties`

`bin/kafka-server-start.sh config/server.properties`

- Create Topic

`bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 3 --partitions 3 --topic test`

- List Topics

`bin/kafka-topics.sh --bootstrap-server localhost:9092 --list`

- Topic Details

`bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe --topic test`

- Start console producer and consumer

`bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test`
`bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test`

For `consumer` you can add

`--from-beginning` to read messages from beginning
`--group test` to specify group
`--partition 6` to read messages from specific partition
`--offset 2`to read messages from specific offset

- List consumer groups and consumer group details

`bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list`
`bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group test --describe`

- Get information from zookeepr about active broker ids or about broker by its id

`bin/zookeeper-shell.sh localhost:2181 ls /brokers/ids` - List Ids
`bin/zookeeper-shell.sh localhost:2181 get /brokers/ids/0` - Get info by id
