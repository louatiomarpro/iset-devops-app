pipeline {
    agent any

    environment {
        DOCKER_HUB_USER = 'jmayeul'
        BACK_IMAGE       = 'jmayeul/iset-devops-backend'
        FRONT_IMAGE      = 'jmayeul/iset-devops-frontend'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
                echo "Code récupéré — build #${env.BUILD_NUMBER}"
            }
        }

        stage('Build Backend') {
    		steps {
        		dir('backend') {
            			withMaven(maven: 'maven3') {
                			sh 'mvn clean package -DskipTests'
            			}
        		}
    		}
	}

        stage('Build Images Docker') {
            steps {
                sh "docker build -t ${BACK_IMAGE}:${BUILD_NUMBER}  ./backend"
                sh "docker build -t ${FRONT_IMAGE}:${BUILD_NUMBER} ./frontend"
                sh "docker tag ${BACK_IMAGE}:${BUILD_NUMBER}  ${BACK_IMAGE}:latest"
                sh "docker tag ${FRONT_IMAGE}:${BUILD_NUMBER} ${FRONT_IMAGE}:latest"
            }
        }

        stage('Push sur Docker Hub') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                        docker push ${BACK_IMAGE}:${BUILD_NUMBER}
                        docker push ${BACK_IMAGE}:latest
                        docker push ${FRONT_IMAGE}:${BUILD_NUMBER}
                        docker push ${FRONT_IMAGE}:latest
                    '''
                }
            }
        }

        stage('Deploy en Production') {
            steps {
                sh '''
                    docker stop iset-backend  || true
                    docker stop iset-frontend || true
                    docker rm   iset-backend  || true
                    docker rm   iset-frontend || true
                    docker run -d --name iset-backend  -p 8080:8080 ${BACK_IMAGE}:latest
                    docker run -d --name iset-frontend -p 80:80     ${FRONT_IMAGE}:latest
                '''
            }
        }
    }

    post {
        success {
            echo "✅ Pipeline réussi — build #${env.BUILD_NUMBER} déployé !"
        }
        failure {
            echo "❌ Pipeline échoué — voir la console Jenkins"
        }
        always {
            cleanWs()
        }
    }
}
