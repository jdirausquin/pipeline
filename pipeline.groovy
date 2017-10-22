pipeline {
    agent any
    stages {
        stage('Cloning source repo') {
            steps {
                git url: 'https://github.com/jdirausquin/pipeline.git'
            }
        }
        stage('Creating ELB & Asigning rule to Clusters SG') {
                steps {
                    echo 'Getting SG of Cluster'
                    script {
                        sh 'bash get-sg.sh $Cluster_Name > tmpsg'
                        def SG=readFile('tmpsg')
                        echo "Security group of Cluster: ${SG}"
                        echo 'Creating ELB & Asigning rule to Cluster SG'
                        sh "bash elb-sg.sh $Application_Name $Envi_ronment $Instance_Port ${SG}"
                    }
                }
        }
        stage('Creating Task Definition') {
            steps {
                script {
                    def userInput = input( id: 'userInput', message: 'Task Definition Name',
                    parameters: [
                        [$class: 'TextParameterDefinition', description: 'Type Task Definition Name', name: 'TaskDefName'],
                        [$class: 'TextParameterDefinition', description: 'Type Container Name', name: 'ContainerName'],
                        [$class: 'TextParameterDefinition', description: 'Type Image URL', name: 'ImageUrl'],
                        [$class: 'TextParameterDefinition', description: 'Type # of CPU Units', name: 'CpuUnits'],
                        [$class: 'TextParameterDefinition', description: 'Type # of Memory Units', name: 'MemUnits'],
                        [$class: 'TextParameterDefinition', description: 'Type Log Group', name: 'LogGroup'],
                        [$class: 'TextParameterDefinition', description: 'Type Log Stream', name: 'LogStream']
                        ])
                        ansiblePlaybook([
                            colorized: true,
                            playbook: 'taskdef.yml',
                            extras: "-e 'containername=${userInput['ContainerName']} -e taskdefname=${userInput['TaskDefName']} -e imageurl=${userInput['ImageUrl']} -e ucpu=${userInput['CpuUnits']} -e umem=${userInput['MemUnits']} -e cport=$Container_Port -e hport=$Instance_Port -e lgroup=${userInput['LogGroup']} -e lstream=${userInput['LogStream']}'"
                            ])
                        def SG=readFile('tmpsg')
                        sh "bash task-def.sh $Application_Name $Envi_ronment $Instance_Port ${SG}"
                }
            }
        }
    }
}