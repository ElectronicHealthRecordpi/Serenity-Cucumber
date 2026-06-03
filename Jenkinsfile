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
                    allowMissing         : false,
                    alwaysLinkToLastBuild: true,
                    keepAll              : true,
                    reportDir            : 'target/site/serenity',
                    reportFiles          : 'index.html',
                    reportName           : 'Serenity Report'
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