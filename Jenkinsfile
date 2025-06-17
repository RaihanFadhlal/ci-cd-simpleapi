pipeline {
    agent {
        dockerfile {
            filename 'Dockerfile.agent'
            args '-v /var/run/docker.sock:/var/run/docker.sock -v ${HOME}/.kube:/home/jenkins/.kube'
        }
    }

    environment {
        DOCKERHUB_USERNAME = 'raihanfadhlal'
        DOCKER_IMAGE_NAME = "${DOCKERHUB_USERNAME}/simple-api"
        DOCKER_IMAGE_TAG = "1.${BUILD_NUMBER}"
        DOCKERHUB_CREDENTIALS_ID = 'dockerhub-credentials'
        KUBECONFIG = '/home/jenkins/.kube/config'
    }

    stages {
        stage('Set Execute Permission') {
            steps {
                sh 'chmod +x mvnw'
            }
        }

        stage('Build Spring Boot App') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: DOCKERHUB_CREDENTIALS_ID, usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ."
                    sh "docker tag ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} ${DOCKER_IMAGE_NAME}:latest"
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    sh "docker push ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                    sh "docker push ${DOCKER_IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh "sed -i 's|image: .*|image: ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}|' k8s/deployment.yaml"
                sh 'kubectl apply -f k8s/'
                sh 'kubectl rollout status deployment/simple-api-deployment'
            }
        }
    }

    post {
        always {
            sh 'docker logout'
        }
    }
}