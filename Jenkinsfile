pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'us-east-1'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/lucklaksh/iot-cdk-aws.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Synthesize') {
            steps {
                sh 'cdk synth'
            }
        }

        stage('Deploy') {
            steps {
                withAWS(credentials: 'your-aws-creds-id', region: 'us-east-1') {
                    sh 'cdk deploy --require-approval never'
                }
            }
        }
    }
}
