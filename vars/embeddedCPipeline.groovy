/**
 * Full embedded-C pipeline template.
 * Stages: lint -> build -> unit tests -> integration tests -> E2E gate
 *
 * Minimal Jenkinsfile usage:
 *
 *   @Library('securingsam-shared-pipeline') _
 *   embeddedCPipeline()
 *
 * With overrides:
 *
 *   @Library('securingsam-shared-pipeline') _
 *   embeddedCPipeline(srcDir: 'src/', skipE2E: true)
 */
def call(Map config = [:]) {
    String  srcDir  = config.get('srcDir',  'src/')
    boolean skipE2E = config.get('skipE2E', false)

    pipeline {
        agent any

        options {
            buildDiscarder(logRotator(numToKeepStr: '20'))
            timeout(time: 45, unit: 'MINUTES')
            timestamps()
            disableConcurrentBuilds()
        }

        environment {
            BUILD_TYPE    = 'Debug'
            ASAN_OPTIONS  = 'abort_on_error=1:detect_leaks=1'
            UBSAN_OPTIONS = 'abort_on_error=1:print_stacktrace=1'
        }

        stages {
            stage('Lint') {
                steps { script { cppLint(srcDir: srcDir) } }
            }

            stage('Build') {
                steps { script { cmakeBuild(srcDir: '.') } }
            }

            stage('Unit Tests') {
                steps { script { ctestRun(label: 'unit', buildDir: 'build') } }
            }

            stage('Integration Tests') {
                steps {
                    script {
                        ctestRun(label: 'integration', buildDir: 'build', jobs: '4')
                    }
                }
            }

            stage('E2E Gate') {
                when {
                    allOf {
                        expression { !skipE2E }
                        anyOf {
                            branch 'main'
                            branch 'release/*'
                        }
                    }
                }
                steps { script { koalaE2E(buildDir: 'build') } }
            }
        }

        post {
            always {
                archiveArtifacts artifacts: 'build/**/*.log',
                                 allowEmptyArchive: true
                cleanWs()
            }
            failure {
                echo 'Build failed — check logs above'
            }
        }
    }
}
