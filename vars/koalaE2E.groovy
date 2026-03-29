/**
 * Run E2E gate tests against real hardware via the Koala lab.
 * Credentials are injected from the Jenkins credential store — never hardcoded.
 *
 * Only runs on main/release/* branches (gate semantics).
 *
 * Usage:
 *   koalaE2E()
 *   koalaE2E(buildDir: 'build')
 */
def call(Map args = [:]) {
    String buildDir = args.get('buildDir', 'build')

    withCredentials([
        string(credentialsId: 'koala-token',    variable: 'KOALA_TOKEN'),
        string(credentialsId: 'koala-username', variable: 'KOALA_USERNAME'),
    ]) {
        sh """
            cd ${buildDir}
            ctest -L e2e --output-on-failure
        """
    }
}
