pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                // Get some code from a GitHub repository
                git (branch: 'main'
                    , url:'https://github.com/cyannara/memo.git')
            }
        }
 
        stage('Build') {
            steps {
                bat "./gradlew  clean build -x test"
            }
            
            post {
                success {
                    archiveArtifacts 'build/libs/*.jar'
                }
            }
        }      
       
        stage('Unit Test') {
            steps {
                bat './gradlew test'
            }
            
            post {
                always {
                    junit 'build/test-results/test/TEST-*.xml'
                }
            }
        }        
    }
}
