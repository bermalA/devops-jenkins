pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
    }

    stages {
        stage('Checkout Code') {
            steps {
                cleanWs()
                git url: 'https://github.com/bermalA/devops-jenkins.git', branch: 'master'
            }
        }
        stage('Build JAR') {
            steps {
                sh 'pwd && ls -l'
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build('swe304/web-app')
                }
            }
        }
        stage('Push to DockerHub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'DOCKER_HUB_CREDENTIALS') {
                        docker.image('swe304/web-app').push('latest')
                    }
                }
            }
        }
        stage('Deploy to Minikube') {
            steps {
                sh 'minikube kubectl -- set image deployment/webapp webapp=swe304/web-app:latest --record'
            }
        }
    }
}
