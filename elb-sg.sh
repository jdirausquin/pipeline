#!/bin/bash
terraform apply -var env=$Environment -var app=$Application_Name -var port=$Instance_Port -var clustersg=$1 -target aws_elb.elb -target aws_security_group_rule.rule_oncluster