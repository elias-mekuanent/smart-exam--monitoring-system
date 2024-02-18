#!/bin/bash

# Set the directory where Kafka is installed
KAFKA_HOME=/home/yaphet/IdeaProjects/Tools/kafka_2.13-3.2.1

# Start ZooKeeper
$KAFKA_HOME/bin/zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties &

# Wait for ZooKeeper to start
sleep 5s

# Start Kafka
$KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties &
