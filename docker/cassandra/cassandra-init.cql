CREATE KEYSPACE IF NOT EXISTS aggregator
WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};

CREATE TABLE IF NOT EXISTS aggregator.logs_v1 (
    host         text,
    time         timestamp,
    log_level    text,
    message_type text,
    message      text,
    PRIMARY KEY (host, time)
) WITH CLUSTERING ORDER BY ( time DESC );
