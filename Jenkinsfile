pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
    }

    stages {
        stage('Checkout Code') {
            steps {
                cleanWs()  // Clean workspace before checkout
                git url: 'https://github.com/bermalA/devops-jenkins.git', branch: 'master'  // Checkout the repository using the git plugin
            }
        }
        stage('Build JAR') {
            steps {
                sh 'pwd && ls -l'  // Print the current working directory and list files
                sh 'chmod +x ./gradlew'  // Ensure gradlew has execute permissions
                sh './gradlew clean build'  // Run the gradle build script
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    docker.build('swe304/web-app')
                }
            }
        }
        stage('Debug Credentials') {
            steps {
                sh 'printenv'  // Print environment variables to check if credentials are loaded
            }
        }
        stage('Push to DockerHub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {
                        docker.image('swe304/web-app').push('latest')
                    }
                }
            }
        }
        stage('Start Minikube') {
            steps {
                sh 'minikube delete || true'  // Delete any existing Minikube cluster
                sh 'minikube start --driver=docker --cpus=4 --memory=8192'  // Start Minikube with more resources
            }
        }
        stage('Create Deployment') {
            steps {
                sh 'minikube kubectl -- apply -f webapp-deploy.yaml'  // Create the deployment
            }
        }
        stage('Set Kubernetes Context') {
            steps {
                sh 'minikube kubectl -- config use-context minikube'  // Ensure Minikube context is set
            }
        }
        stage('Deploy to Minikube') {
            steps {
                sh 'minikube kubectl -- set image deployment/web-app-deploy web-app=swe304/web-app:latest'  // Set the image for the deployment
            }
        }
    }
}
