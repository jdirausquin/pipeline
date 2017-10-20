#!/bin/bash
SG=`aws ecs list-container-instances --cluster $1 | jq .containerInstanceArns[0] | xargs -I {} aws ecs describe-container-instances --cluster $1 --container-instances {} | jq .containerInstances[].ec2InstanceId | xargs -I {} aws ec2 describe-instances --instance-id {} | jq .Reservations[].Instances[].SecurityGroups[].GroupId`
echo $SG 2>&1