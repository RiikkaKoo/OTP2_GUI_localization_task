pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
    }

    environment {
          PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
          DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
          DOCKERHUB_REPO = 'riikkakoo/otp2-gui-localization'
          DOCKER_IMAGE_TAG = 'latest'
      }

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/RiikkaKoo/OTP2_GUI_localization_task.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Generate Report') {
            steps {
                bat 'mvn jacoco:report'
            }
        }

// No tests done for this project
        //stage('Publish Test Results') {
            //steps {
                //junit '**/target/surefire-reports/*.xml'
            //}
        //}


        stage('Publish Coverage Report') {
            steps {
                recordCoverage(tools: [[parser: 'JACOCO']])
            }
        }

         stage('Build Docker Image') {
              steps {
                 script {
                     docker.build("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}")
                 }
              }
         }

         stage('Push Docker Image to Docker Hub') {
                  steps {
                      script {
                          docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS_ID) {
                              docker.image("${DOCKERHUB_REPO}:${DOCKER_IMAGE_TAG}").push()
                          }
                      }
                  }
              }
         }
}