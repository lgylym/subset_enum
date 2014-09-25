#!/bin/bash

#run enum on various configurations
JAVA_HOME='/scratch/yluo/jdk1.7.0_51/'



#test the noBits impact
for ((noBits=10; noBits < 31; noBits=noBits+5))
do
    for method in 'FilterEnum' 'JumpEnum' 'TrieEnum' 'PTrieEnum'
    do
        for ((i=0; i < 10; i++))
        do
            taskset -c 2 $JAVA_HOME/bin/java -Xmx10000m -jar ./subsetEnum.jar -m=$method -n=$noBits -s=18 -r=0
        done
    done
done

#test the sSize impact
for ((sSize=15; sSize < 21; sSize++))
do
    for method in 'FilterEnum' 'JumpEnum' 'TrieEnum' 'PTrieEnum'
    do
        for ((i=0; i < 10; i++))
        do
            taskset -c 2 $JAVA_HOME/bin/java -Xmx10000m -jar ./subsetEnum.jar -m=$method -n=25 -s=$sSize -r=0
        done
    done
done


#test the rSize impact
for ((rSize=14; rSize < 18; rSize++))
do
    for method in 'FilterEnum' 'JumpEnum' 'TrieEnum' 'PTrieEnum'
    do
        for ((i=0; i < 10; i++))
        do
            taskset -c 2 $JAVA_HOME/bin/java -Xmx10000m -jar ./subsetEnum.jar -m=$method -n=25 -s=18 -r=$rSize
        done
    done
done

