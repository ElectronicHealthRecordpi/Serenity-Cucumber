pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Tests Login y Productos') {
            steps {
                sh 'mvn clean verify -Dmaven.test.failure.ignore=true'
            }
        }

        stage('Reporte HTML') {
            steps {
                publishHTML(target: [
                    reportDir            : 'target/site/serenity',
                    reportFiles          : 'index.html',
                    reportName           : 'Serenity Report',
                    keepAll              : true,
                    alwaysLinkToLastBuild: true
                ])
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/site/serenity/**',
                             fingerprint: true
        }
    }
}