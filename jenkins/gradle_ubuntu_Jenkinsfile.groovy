pipeline {
    agent any
    environment {
        strDockerTag = "${BUILD_ID}"
        strDockerImage ="cyannara/memo:${strDockerTag}"
    }    
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
                sh '''
                    chmod +x gradlew
                    ./gradlew  clean build -x test
                    '''
            }
            
            post {
                success {
                    archiveArtifacts 'build/libs/*.jar'
                }
            }
        }      
        
        stage('Docker Image Build') {
            steps {
                script {
                    //oDockImage = docker.build(strDockerImage)
                    oDockImage = docker.build(strDockerImage, "--build-arg VERSION=${strDockerTag} -f Dockerfile .")
                }
            }
        }    
        
        stage('Docker Image Push') {
            steps {
                script {
                    docker.withRegistry('', 'DockerHub_Credential') {
                        oDockImage.push()
                    }
                }
            }
        } 
        
        stage('Staging Deploy') {
            steps {
                sshagent(credentials: ['Staging-PrivateKey']) {
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@3.39.9.98 docker container rm -f memo"
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@3.39.9.98 docker container run \
                                        -d \
                                        -p 8080:80 \
                                        --name=memo \
                                        ${strDockerImage} "
                }
            }
        }  

        stage ('JMeter LoadTest') {
            steps { 
                sh '/var/lib/jenkins/sw/apache-jmeter-5.6.3/bin/jmeter.sh -j jmeter.save.saveservice.output_format=xml -n -t src/main/jmx/memo_loadtest.jmx -l loadtest_result.jtl' 
                perfReport filterRegex: '', showTrendGraphs: true, sourceDataFiles: 'loadtest_result.jtl' 
            } 
        }  

        stage('Archive Report') {
            steps {
                archiveArtifacts artifacts: 'loadtest-report/**', fingerprint: true
            }
        }                       
    }
}