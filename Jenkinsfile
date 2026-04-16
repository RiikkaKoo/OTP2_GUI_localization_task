pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
    }

    environment {
          SONARQUBE_SERVER = 'SonarQubeServer'
          PATH = "C:\\Program Files\\Docker\\Docker\\resources\\bin;${env.PATH}"
          DOCKERHUB_CREDENTIALS_ID = 'Docker_Hub'
          DOCKERHUB_REPO = 'riikkakoo/otp2-gui-localization'
          DOCKER_IMAGE_TAG = 'latest'
      }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'DB-localization',
                            url: 'https://github.com/RiikkaKoo/OTP2_GUI_localization_task.git'
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

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                recordCoverage(tools: [[parser: 'JACOCO']])
            }
        }

// Add Jenkins credentials for SonarQube: Settings --> Credentials --> Add Credentials --> Secret text
// --> Secret = the SonarQube token you generated, ID = SonarQube or something similar
// In Jenkins --> System --> SonarQube servers,
//select the SonarQube token in the Server authentication token section for your installed SonarQubeServer.

// Make sure you have SonarScanner on your computer and the path to it set as an environmental variable SONAR_SCANNER_HOME.
// (e.g. "C:\SonarScanner\sonar-scanner-8.0.1.6346-windows-x64")
// Add SonarScanner tool to Jenkins: Settings --> Tools --> Scroll to SonarQube Scanner --> Add SonarQube Scanner
// --> Name: SONAR_SCANNER_HOME, SONAR_RUNNER_HOME = the same path you have put in the environmental variables
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    bat """
                        ${tool 'SONAR_SCANNER_HOME'}\\bin\\sonar-scanner ^
                        -Dsonar.projectKey=trip_calculator_sonar ^
                        -Dsonar.sources=src/main ^
                        -Dsonar.projectName=trip_calculator ^
                        -Dsonar.host.url=http://localhost:9000 ^
                        -Dsonar.java.binaries=target/classes
                    """
                }
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