# securingsam Shared Pipeline Library

Jenkins shared pipeline library for all securingsam embedded-C projects.
Registered in Jenkins as **`securingsam-shared-pipeline`**.

## Available Steps

| Step | Description |
|------|-------------|
| `cppLint` | cppcheck + clang-tidy static analysis |
| `cmakeBuild` | Parallel CMake build: Debug+ASan and Release |
| `ctestRun` | CTest runner with label filter and JUnit output |
| `koalaE2E` | E2E gate via Koala lab hardware (credentials from store) |
| `embeddedCPipeline` | Full pipeline template — drop-in for inline Jenkinsfile |

## Usage

### Minimal Jenkinsfile (full template)

```groovy
@Library('securingsam-shared-pipeline') _
embeddedCPipeline()
```

### Individual steps

```groovy
@Library('securingsam-shared-pipeline') _

pipeline {
    agent any
    stages {
        stage('Lint')  { steps { cppLint() } }
        stage('Build') { steps { cmakeBuild() } }
        stage('Test')  { steps { ctestRun(label: 'unit') } }
    }
}
```

### Step parameters

```groovy
cppLint(srcDir: 'src/', buildDir: 'build/')
cmakeBuild(srcDir: '.', jobs: '4')
ctestRun(label: 'integration', buildDir: 'build', jobs: '4')
koalaE2E(buildDir: 'build')
embeddedCPipeline(srcDir: 'src/', skipE2E: true)
```

## Required Jenkins Credentials

All credentials live in the Jenkins credential store — never in code.

| Credential ID | Type | Description |
|--------------|------|-------------|
| `koala-token` | Secret text | Koala lab API token |
| `koala-username` | Secret text | Koala lab username |
| `github-webhook-secret` | Secret text | GitHub webhook HMAC secret |

## Jenkins Infrastructure (`docker/`)

Docker Compose + JCasC setup for running Jenkins locally or on EC2.

```bash
cd docker/
cp env.example .env
# Fill in .env with real credentials
docker compose up -d
# Jenkins at http://localhost:8080
```

### Production (`jenkins.ci.securingsam.io`)

Production Jenkins runs on an EC2 instance in `eu-central-1`.
If unreachable: start the EC2 instance, verify Docker service auto-started.
The `CASC_JENKINS_CONFIG` env var points to the JCasC config on startup.

### After repo transfer to `securingsam` org

Update the `remote` URL in `docker/casc/jenkins.yaml`:
```yaml
remote: "https://github.com/securingsam/jenkins-shared-pipeline.git"
```
