#!bin/bash
ansible-playbook taskdef.yml -e taskdefname=$Task_Definition_Name \
				-e containername=$Container_Name \
				-e imageurl=$Image_URL \
                -e ucpu=$CPU_Units \
                -e umem=$Memory_Units \
                -e cport=$Container_Port \
                -e hport=$Host_Port \
                -e lgroup=$Log_Group \
                -e lstream=$Log_Stream