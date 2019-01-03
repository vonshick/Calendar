#!/bin/bash

gcc -Wall -pthread -o server/server server/server.c
server/server $1 $2
