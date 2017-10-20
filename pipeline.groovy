pipeline {
    agent any
    stages {
        stage('Clone source repo') {
            steps {
                git url: 'https://github.com/jdirausquin/pipeline.git'
            }
        }

        stage('Create ELB') {
                steps {
                    echo 'Getting SG of Cluster'
                    script {
                        sh 'bash get-sg.sh $Cluster_Name > tmpsg'
                        def SG=readFile('tmpsg')
                        echo "Security group of Cluster: ${SG}"
                        echo 'Creating ELB & Asigning rule to Cluster SG'
                        sh "bash elb-sg.sh $Environment $Application_Name $Instance_Port ${SG}"
                    }
                }
        }
    }
}