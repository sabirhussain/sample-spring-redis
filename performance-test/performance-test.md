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
| Test Tool          | Hey                                                 |

## Redis Configuration

| Property      | Value                    |
|---------------|--------------------------|
| Redis Version | 8.6.1 64 Bit Open Source |
| Redis Client  | lettuce 6.2.0.RELEASE    |

### Configuration File

```yaml
########################
# MULTI THREAD
########################

io-threads 4
io-threads-do-reads yes
#applicable in ValKey- events-per-io-thread 0

########################

appendonly yes
dir /home/sabir/temp/redis-stable/redis-local/data
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
| First Cycle QPS    | 3575.2671 |
| Second Cycle QPS   | 4279.4765 |

### First Cycle Results
```bash
Summary:
  Total:        60.2903 secs
  Slowest:      1.9886 secs
  Fastest:      0.0728 secs
  Average:      0.2790 secs
  Requests/sec: 3575.2671


Response time histogram:
  0.073 [1]     |
  0.264 [127075]        |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.456 [77838] |■■■■■■■■■■■■■■■■■■■■■■■■■
  0.648 [9629]  |■■■
  0.839 [11]    |
  1.031 [0]     |
  1.222 [0]     |
  1.414 [212]   |
  1.605 [105]   |
  1.797 [423]   |
  1.989 [260]   |


Latency distribution:
  10% in 0.1920 secs
  25% in 0.2165 secs
  50% in 0.2476 secs
  75% in 0.3131 secs
  90% in 0.3944 secs
  95% in 0.4548 secs
  99% in 0.5617 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0019 secs, 0.0728 secs, 1.9886 secs
  DNS-lookup:   0.0001 secs, 0.0000 secs, 0.0742 secs
  req write:    0.0001 secs, 0.0000 secs, 0.0553 secs
  resp wait:    0.2762 secs, 0.0716 secs, 1.7732 secs
  resp read:    0.0007 secs, 0.0000 secs, 0.0891 secs

Status code distribution:
  [200] 215554 responses
```
### Second Cycle Results (After 30 seconds of cooldown)
```bash
Summary:
  Total:        60.2127 secs
  Slowest:      0.5252 secs
  Fastest:      0.0465 secs
  Average:      0.2330 secs
  Requests/sec: 4279.4765


Response time histogram:
  0.047 [1]     |
  0.094 [281]   |
  0.142 [2040]  |■
  0.190 [40895] |■■■■■■■■■■■■■■
  0.238 [114281]        |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.286 [65771] |■■■■■■■■■■■■■■■■■■■■■■■
  0.334 [24946] |■■■■■■■■■
  0.382 [6165]  |■■
  0.429 [2626]  |■
  0.477 [629]   |
  0.525 [44]    |


Latency distribution:
  10% in 0.1809 secs
  25% in 0.1998 secs
  50% in 0.2252 secs
  75% in 0.2580 secs
  90% in 0.2970 secs
  95% in 0.3217 secs
  99% in 0.3912 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0001 secs, 0.0465 secs, 0.5252 secs
  DNS-lookup:   0.0001 secs, 0.0000 secs, 0.1502 secs
  req write:    0.0001 secs, 0.0000 secs, 0.1478 secs
  resp wait:    0.2319 secs, 0.0240 secs, 0.5251 secs
  resp read:    0.0007 secs, 0.0000 secs, 0.0967 secs

Status code distribution:
  [200] 257679 responses
```
### Thread States

#### Initial Thread States

![Initial Thread States](initial-thread-state.png)

#### During Load Thread States

![During Load Thread States](during-load-thread-state.png)

#### Cooldown Thread States

![Cooldown Thread States](cooldown-thread-state.png)