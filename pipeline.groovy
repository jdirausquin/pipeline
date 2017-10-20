pipeline {
    agent any

    stages {
        stage('Create ELB') {
            steps {
                echo 'Getting SG of Cluster'
                sh 'get-sg.sh $ClusterName > SG'
                echo 'Creating ELB & Asigning rule to Cluster SG'
                sh 'elb-sg.sh'
            }
        }
    }
}