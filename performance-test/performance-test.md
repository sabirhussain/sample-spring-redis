# Redis Standalone Performance Test

## System Configuration

| Property           | Value                                               |
|--------------------|-----------------------------------------------------|
| Processor          | Intel(R) Core(TM) i7-9750H CPU @ 2.60GHz (2.59 GHz) |
| Core Count         | 6                                                   |
| Thread Count       | 12                                                  |
| Thread(s) per core | 2                                                   |
| RAM                | 24 GB (23.8 GB usable)                              |
| Architecture       | x64                                                 |
| Operating System   | Ubuntu 24.04.4 LTS                                  |

## Testing Tool
![Hey](https://github.com/rakyll/hey) is a tiny program that sends some load to a web application. 
It is similar to ApacheBench (ab) but supports HTTP/2 and has more features.

## Valkey Configuration

| Property       | Value                                                  |
|----------------|--------------------------------------------------------|
| ValKey Version | 8.1.6 64 Bit Open Source                               |
| ValKey Client  | lettuce 6.6.0.RELEASE (spring-boot-starter-data-redis) |

### Configuration File

```yaml
port 0
tls-port 6379

tls-cert-file /home/sabir/temp/valkey/valkey-local/tls/server.crt
tls-key-file /home/sabir/temp/valkey/valkey-local/tls/server.key
tls-ca-cert-file /home/sabir/temp/valkey/valkey-local/tls/ca.crt

tls-auth-clients no

########################
 # MULTI THREAD
 ########################

io-threads 4
io-threads-do-reads yes
# events-per-io-thread 0

########################

appendonly yes
dir /home/sabir/temp/valkey/valkey-local/data
bind 0.0.0.0
```

## Application Configuration

```yaml
server:
  tomcat:
    threads:
      max: 500
      min-spare: 50
    accept-count: 200
```

## Performance Test Configuration

### Test Parameters

| Property          | Value                                                                                   |
|-------------------|-----------------------------------------------------------------------------------------|
| Duration of load  | 1 Minute                                                                                |
| Concurrency Level | 1000                                                                                    |
| HTTP Method       | PUT                                                                                     |
| Content-Type      | application/json                                                                        |
| Payload           | {"name":"Load Test","email":"load@email.com","city":"LoadLand","country":"LoadCountry"} |
| Target URL        | http://localhost:8080/users                                                             |

### CLI Command

```bash
hey -z 1m -c 1000 -m PUT -H "Content-Type: application/json" -d '{"name":"Load Test","email":"load@email.com","city":"LoadLand","country":"LoadCountry"}' http://localhost:8080/users
```

## Performance Test Results
**QPS Summary**

| Property           | Value     |
|--------------------|-----------|
| First Cycle QPS    | 2873.9048 |
| Second Cycle QPS   | 2948.0161 |

### First Cycle Results
```bash
Summary:
  Total:        60.3152 secs
  Slowest:      1.6037 secs
  Fastest:      0.1153 secs
  Average:      0.3470 secs
  Requests/sec: 2873.9048


Response time histogram:
  0.115 [1]     |
  0.264 [4944]  |■
  0.413 [149645]        |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.562 [16067] |■■■■
  0.711 [1683]  |
  0.859 [2]     |
  1.008 [87]    |
  1.157 [63]    |
  1.306 [149]   |
  1.455 [346]   |
  1.604 [353]   |


Latency distribution:
  10% in 0.2814 secs
  25% in 0.3042 secs
  50% in 0.3312 secs
  75% in 0.3650 secs
  90% in 0.4159 secs
  95% in 0.4574 secs
  99% in 0.6162 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0017 secs, 0.1153 secs, 1.6037 secs
  DNS-lookup:   0.0001 secs, 0.0000 secs, 0.0471 secs
  req write:    0.0001 secs, 0.0000 secs, 0.0237 secs
  resp wait:    0.3429 secs, 0.0280 secs, 1.3693 secs
  resp read:    0.0002 secs, 0.0000 secs, 0.0283 secs

Status code distribution:
  [200] 173340 responses
```
### Second Cycle Results (After 30 seconds of cooldown)
```bash
Summary:
  Total:        60.2856 secs
  Slowest:      0.8218 secs
  Fastest:      0.0839 secs
  Average:      0.3383 secs
  Requests/sec: 2948.0161


Response time histogram:
  0.084 [1]     |
  0.158 [481]   |
  0.231 [1049]  |
  0.305 [49494] |■■■■■■■■■■■■■■■■■■■■
  0.379 [97727] |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.453 [20698] |■■■■■■■■
  0.527 [5254]  |■■
  0.600 [1881]  |■
  0.674 [483]   |
  0.748 [569]   |
  0.822 [86]    |


Latency distribution:
  10% in 0.2846 secs
  25% in 0.3011 secs
  50% in 0.3268 secs
  75% in 0.3602 secs
  90% in 0.4042 secs
  95% in 0.4495 secs
  99% in 0.5648 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0003 secs, 0.0839 secs, 0.8218 secs
  DNS-lookup:   0.0001 secs, 0.0000 secs, 0.0901 secs
  req write:    0.0000 secs, 0.0000 secs, 0.0394 secs
  resp wait:    0.3378 secs, 0.0138 secs, 0.8216 secs
  resp read:    0.0002 secs, 0.0000 secs, 0.0423 secs

Status code distribution:
  [200] 177723 responses
```
### Thread States

#### Initial Thread States

![Initial Thread States](initial-thread-state.png)

#### During Load Thread States

![During Load Thread States](during-load-thread-state.png)

#### Cooldown Thread States

![Cooldown Thread States](cooldown-thread-state.png)