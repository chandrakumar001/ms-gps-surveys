def pomVersion
pipeline {
    // sh --> linux
    // bat--> windows
    agent any
    triggers {
        pollSCM('H/2 * * * *')
    }

    stages {
        // Build
        stage('Build') {
            steps {
                // Clean before build
                cleanWs()
                // We need to explicitly checkout from SCM here
                checkout scm
                bat 'mvn clean compile'
            }
            post {
                // Clean after build
                always {
                    cleanWs(cleanWhenNotBuilt: false,
                            deleteDirs: true,
                            disableDeferredWipeout: true,
                            notFailBuild: true,
                            patterns: [[pattern: '.gitignore', type: 'INCLUDE'],
                                       [pattern: '.propsfile', type: 'EXCLUDE']])
                }
            }
        }
        stage('Unit and Integration Test') {
            steps {

                bat 'mvn test -Ddependency-check.skip=true'
                bat 'mvn verify -Dskip.unit.tests=true -Ddependency-check.skip=true -Dsonar.skip=true'
            }
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }
        stage('Cucumber Report') {
            steps {
                cucumber buildStatus: 'UNSTABLE',
                        fileIncludePattern: '**/feature.*.*.json',
                        jsonReportDirectory: 'target'
            }
        }
        stage('OWASP Dependency-Check Vulnerabilities') {
            steps {
                bat 'mvn dependency-check:check -Dformat=ALL'
                dependencyCheckPublisher pattern: 'target/dependency-check-report.xml'
            }
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv(credentialsId: 'chandran-edu-sonarqube-secret', installationName: 'chandran-edu-sonarqube-server') {
                    //   withSonarQubeEnv('chandran-edu-sonarqube') {
                    bat 'mvn sonar:sonar -Dsonar.dependencyCheck.jsonReportPath=target/dependency-check-report.json -Dsonar.dependencyCheck.xmlReportPath=target/dependency-check-report.xml -Dsonar.dependencyCheck.htmlReportPath=target/dependency-check-report.html'
                }
            }
        }
        // Package
        stage('Package') {
            steps {
                bat 'mvn package -Dmaven.test.skip=true'
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    pomVersion = getPomVersion(pomVersion)
                }
                withDockerRegistry(credentialsId: 'chandran-edu-docker-treescale-secret', url: 'https://repo.treescale.com/') {
                    bat 'docker build . -t repo.treescale.com/chandrakumar001/ms-gps-surveys:' + pomVersion
                    bat 'echo the image to docker'
                    bat 'docker push repo.treescale.com/chandrakumar001/ms-gps-surveys:' + pomVersion

                    bat 'echo the latest image to docker'
                    bat 'docker tag repo.treescale.com/chandrakumar001/ms-gps-surveys:' + pomVersion + ' repo.treescale.com/chandrakumar001/ms-gps-surveys:latest'
                    bat 'docker push repo.treescale.com/chandrakumar001/ms-gps-surveys:latest'

                    bat 'echo Delete the image from jenkins'
                    bat 'docker rmi -f repo.treescale.com/chandrakumar001/ms-gps-surveys:' + pomVersion + ' repo.treescale.com/chandrakumar001/ms-gps-surveys:latest'
                }
            }
        }
        // stage('Deploy') {
        //     steps {
        //         withKubeConfig([credentialsId: 'chandran-edu-kubernetes-config']) {
        //             sh 'kubectl set image deployment/ms-gps-surveys ms-gps-surveys=repo.treescale.com/chandrakumar001/ms-gps-surveys:'+pomVersion
        //         }
        //     }
        // }
        //end
    }
}

private String getPomVersion(pomVersion) {

    if (pomVersion == null) {
        def pom = readMavenPom file: 'pom.xml'
        pomVersion = pom.version
        printf('Test Version: %s', pomVersion)
        return pomVersion
    }
    return pomVersion
}
