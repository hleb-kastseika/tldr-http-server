# tldr http server

This repository contains Java service that provides tldr-pages as HTTP service.

### Requirements
 - Java 11
 - Maven
 - Docker

### How to build

Build process includes two steps:
 - build java app with Maven
 ```mvn clean package```
 - build Docker image
 ```docker build -t tldr-server .```

I prepared shell script to combine these two points - ```build.sh```.

If you want to run Java app locally without Docker you will need to have installed tldr.
Java service make calls for it. In my docker image I install it using npm.

### How to run and verify

To run the server you just need to start prepared Docker image ```docker run tldr-server```.
There is shell script ```start-server.sh```, but it executes the same command.
Server use port 8080. To find Docker container IP you can use next command:
```
docker inspect --format '{{ .NetworkSettings.IPAddress }}' <<container_id>>
```
After service starts you are able to call API with any HTTP client.
Here are few examples with curl.

Successful call:
```
~$ curl -i 172.17.0.2:8080/top
HTTP/1.1 200 OK
Date: Fri, 08 Apr 2022 19:56:01 GMT
Content-length: 666


  top

  Display dynamic real-time information about running processes.
  More information: https://manned.org/top.

  - Start top:
    top

  - Do not show any idle or zombie processes:
    top -i

  - Show only processes owned by given user:
    top -u username

  - Sort processes by a field:
    top -o field_name

  - Show the individual threads of a given process:
    top -Hp process_id

  - Show only the processes with the given PID(s), passed as a comma-separated list. (Normally you wouldn't know PIDs off hand. This example picks the PIDs from the process name):
    top -p $(pgrep -d ',' process_name)

  - Get help about interactive commands:
    ?
```
Not successful call:
```
~$ curl -i 172.17.0.2:8080/something
HTTP/1.1 404 Not Found
Date: Fri, 08 Apr 2022 19:59:03 GMT
Content-length: 42

tldr has no information about 'something'.
```
I found that when tldr don't find information about requested command in local cache, it tries to update the cache.
Usually it can take some time, so I added 5s timeout to my API to avoid long response time. There is open issue
in tldr Github repository to add ability to turn off cache updating when page is not found:
https://github.com/tldr-pages/tldr-node-client/issues/225.
Also I update tldr local cache in the time of Docker image creation, so you will most likely have
a more or less recent version :)