# Valkey Standalone Performance Test

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
| ValKey Client  | lettuce 6.2.0.RELEASE (spring-boot-starter-data-redis) |

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

## Application Thread Configuration

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
| First Cycle QPS    | 2736.6461 |
| Second Cycle QPS   | 2875.3695 |

### First Cycle Results
```bash
Summary:
  Total:        60.3447 secs
  Slowest:      1.8804 secs
  Fastest:      0.1357 secs
  Average:      0.3642 secs
  Requests/sec: 2736.6461


Response time histogram:
  0.136 [1]     |
  0.310 [11045] |■■■
  0.485 [145877]        |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.659 [5665]  |■■
  0.834 [1554]  |
  1.008 [2]     |
  1.183 [48]    |
  1.357 [167]   |
  1.531 [160]   |
  1.706 [228]   |
  1.880 [395]   |


Latency distribution:
  10% in 0.3141 secs
  25% in 0.3252 secs
  50% in 0.3438 secs
  75% in 0.3692 secs
  90% in 0.4059 secs
  95% in 0.4837 secs
  99% in 0.7254 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0018 secs, 0.1357 secs, 1.8804 secs
  DNS-lookup:   0.0001 secs, 0.0000 secs, 0.0824 secs
  req write:    0.0000 secs, 0.0000 secs, 0.0210 secs
  resp wait:    0.3613 secs, 0.1357 secs, 1.8029 secs
  resp read:    0.0002 secs, 0.0000 secs, 0.0539 secs

Status code distribution:
  [200] 165142 responses
```
### Second Cycle Results (After 30 seconds of cooldown)
```bash
Summary:
  Total:        60.3056 secs
  Slowest:      0.6658 secs
  Fastest:      0.0232 secs
  Average:      0.3469 secs
  Requests/sec: 2875.3695


Response time histogram:
  0.023 [1]     |
  0.087 [29]    |
  0.152 [124]   |
  0.216 [592]   |
  0.280 [199]   |
  0.344 [93432] |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.409 [71979] |■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
  0.473 [4747]  |■■
  0.537 [1523]  |■
  0.602 [764]   |
  0.666 [11]    |


Latency distribution:
  10% in 0.3152 secs
  25% in 0.3261 secs
  50% in 0.3410 secs
  75% in 0.3611 secs
  90% in 0.3818 secs
  95% in 0.4016 secs
  99% in 0.4898 secs

Details (average, fastest, slowest):
  DNS+dialup:   0.0002 secs, 0.0232 secs, 0.6658 secs
  DNS-lookup:   0.0002 secs, 0.0000 secs, 0.1185 secs
  req write:    0.0000 secs, 0.0000 secs, 0.0374 secs
  resp wait:    0.3463 secs, 0.0071 secs, 0.6259 secs
  resp read:    0.0002 secs, 0.0000 secs, 0.0256 secs

Status code distribution:
  [200] 173401 responses
```
### Thread States

#### Initial Thread States

![Initial Thread States](initial-thread-state.png)

#### During Load Thread States

![During Load Thread States](during-load-thread-state.png)

#### Cooldown Thread States

![Cooldown Thread States](cooldown-thread-state.png)