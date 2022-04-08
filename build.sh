#!/bin/bash

mvn package

docker build -t tldr-server .