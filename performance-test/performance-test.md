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
| First Cycle QPS    | 3915.2487 |
| Second Cycle QPS   | 4254.9090 |

### First Cycle Results
```bash
Summary:
  Total:        60.1607 secs
  Slowest:      2.6875 secs
  Fastest:      0.0708 secs
  Average:      0.2549 secs
  Requests/sec: 3915.2487


Response time histogram:
  0.071 [1]     |
  0.332 [213535]        |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.594 [20764] |■■■■
  0.856 [244]   |
  1.117 [0]     |
  1.379 [0]     |
  1.641 [0]     |
  1.902 [1]     |
  2.164 [165]   |
  2.426 [241]   |
  2.687 [593]   |


Latency distribution:
  10% in 0.1845 secs
  25% in 0.2046 secs
  50% in 0.2323 secs
  75% in 0.2662 secs
  90% in 0.3260 secs
  95% in 0.3892 secs
  99% in 0.5324 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0020 secs, 0.0708 secs, 2.6875 secs
  DNS-lookup:   0.0001 secs, 0.0000 secs, 0.0937 secs
  req write:    0.0001 secs, 0.0000 secs, 0.0590 secs
  resp wait:    0.2515 secs, 0.0708 secs, 2.6233 secs
  resp read:    0.0006 secs, 0.0000 secs, 0.0849 secs

Status code distribution:
  [200] 235544 responses
```
### Second Cycle Results (After 30 seconds of cooldown)
```bash
Summary:
  Total:        60.1862 secs
  Slowest:      0.5569 secs
  Fastest:      0.0672 secs
  Average:      0.2345 secs
  Requests/sec: 4254.9090


Response time histogram:
  0.067 [1]     |
  0.116 [1246]  |■
  0.165 [6171]  |■■■
  0.214 [98456] |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.263 [98659] |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.312 [25502] |■■■■■■■■■■
  0.361 [13740] |■■■■■■
  0.410 [8008]  |■■■
  0.459 [3685]  |■
  0.508 [580]   |
  0.557 [39]    |


Latency distribution:
  10% in 0.1809 secs
  25% in 0.1972 secs
  50% in 0.2228 secs
  75% in 0.2524 secs
  90% in 0.3130 secs
  95% in 0.3588 secs
  99% in 0.4280 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0003 secs, 0.0672 secs, 0.5569 secs
  DNS-lookup:   0.0001 secs, 0.0000 secs, 0.0875 secs
  req write:    0.0001 secs, 0.0000 secs, 0.0447 secs
  resp wait:    0.2336 secs, 0.0299 secs, 0.5206 secs
  resp read:    0.0005 secs, 0.0000 secs, 0.0915 secs

Status code distribution:
  [200] 256087 responses
```
### Thread States

#### Initial Thread States

![Initial Thread States](initial-thread-state.png)

#### During Load Thread States

![During Load Thread States](during-load-thread-state.png)

#### Cooldown Thread States

![Cooldown Thread States](cooldown-thread-state.png)