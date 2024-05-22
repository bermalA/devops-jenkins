pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
    }

    stages {
        stage('Build JAR') {
            steps {
                // Ensure gradlew has execute permissions
                sh 'chmod +x ./gradlew'
                // Run the gradle build script
                sh './gradlew clean build'
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build('<your-dockerhub-username>/new-web-app')
                }
            }
        }
        stage('Push to DockerHub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'DOCKER_HUB_CREDENTIALS') {
                        docker.image('<your-dockerhub-username>/new-web-app').push('latest')
                    }
                }
            }
        }
        stage('Deploy to Minikube') {
            steps {
                sh 'minikube kubectl -- set image deployment/webapp webapp=<your-dockerhub-username>/new-web-app:latest --record'
            }
        }
    }
}
