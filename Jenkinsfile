pipeline {
  agent any

  stages {
    stage('Build') {
      steps {
        sh "sbt -no-colors -batch dist"
        archiveArtifacts artifacts: '**/target/universal/*.zip', fingerprint: true
      }
    }

    stage('Publish') {
      steps {
        echo 'TODO: publish'
      }
    }
  }
}
