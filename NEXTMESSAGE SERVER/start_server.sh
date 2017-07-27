#!/bin/bash

cd bin
./catalina.sh run

echo press q to close server
while :
do
    read -n1 key

    if [[ $key = q ]]
    then
        break
    fi
done



