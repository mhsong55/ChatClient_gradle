#!/bin/bash
HOSTNAME=`hostname -s`
while :
  do
    clear
    echo "$HOSTNAME NETSTAT -naopt | grep 20111"
    timestamp=`date +%Y/%m/%d,%H:%M:%S -d +9hour`
    echo "$timestamp KST"

    netstat -naopt | grep 20111
    sleep 1
  done