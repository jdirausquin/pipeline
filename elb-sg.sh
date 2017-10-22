#!/bin/bash
terraform apply -var app=$1 -var env=$2 -var port=$3 -var clustersg=$4 -target aws_elb.elb -target aws_security_group_rule.rule_oncluster