pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Tests Login') {
            steps {
                sh 'mvn clean verify -Dmaven.test.failure.ignore=true -Dserenity.take.screenshots=AFTER_EACH_STEP'
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