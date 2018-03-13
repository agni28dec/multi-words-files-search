#!/bin/bash

SERVER_PORT=$1

APPFILE=$2

CONFIG_LOCATION=$3


PERMGEN_PARAM="-XX:PermSize=128m -XX:MaxPermSize=256m"

export hostname 

echo "Script started at : $(date)" >> $CONFIG_LOG

echo "===============================" >> $CONFIG_LOG

echo "PWP_PORT:- $PWP_PORT" >> $CONFIG_LOG

echo "PWP_FD:- $PWP_FD" >> $CONFIG_LOG

echo "SERVER_PORT:- $SERVER_PORT" >> $CONFIG_LOG

echo "JAVA_OPTS_MED:- $JAVA_OPTS_MED" >> $CONFIG_LOG

echo "ENV:- $env" >> $CONFIG_LOG

echo "CONFIG_LOCATION:- $CONFIG_LOCATION" >> $CONFIG_LOG

echo "APPLICATION_FILE:- $APPFILE" >> $CONFIG_LOG

echo "PERMGEN_PARAML- $PERMGEN_PARAM" >> $CONFIG_LOG

echo "HOSTNAME:- $HOSTNAME" >> $CONFIG_LOG

echo "===============================" >> $CONFIG_LOG


java  $JAVA_OPTS_MED $PERMGEN_PARAM -Dspring.profiles.active=$env  
-Dspring.config.location=$CONFIG_LOCATION -Dloader.path=$CONFIG_LOCATION 
-Dserver.port=$SERVER_PORT $APPFILE