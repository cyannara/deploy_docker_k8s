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

        stage('Staging Deploy') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'Staging-PrivateKey', keyFileVariable: 'KEYFILE', usernameVariable: 'USER')]) {
                    bat """
                        echo Fixing key permissions...
                        icacls "%KEYFILE%" /inheritance:r
                        icacls "%KEYFILE%" /grant:r "NT AUTHORITY\\SYSTEM:R"
                        ssh -i "%KEYFILE%" -o StrictHostKeyChecking=no %USER%@54.237.147.247 "sh /home/ubuntu/startup.sh"
                    """
                }
            }
        }  

    }
}
