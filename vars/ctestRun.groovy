/**
 * Run ctest with a specific label and publish JUnit results.
 *
 * Usage:
 *   ctestRun(label: 'unit')
 *   ctestRun(label: 'integration', buildDir: 'build', jobs: '4')
 */
def call(Map args = [:]) {
    if (!args.label) { error('ctestRun requires label') }
    String label    = args.label
    String buildDir = args.get('buildDir', 'build')
    String jobs     = args.get('jobs',     '$(nproc)')

    sh """
        cd ${buildDir}
        ctest -L ${label} --output-on-failure -j ${jobs}
    """

    junit allowEmptyResults: true,
          testResults: "${buildDir}/test-results/${label}/*.xml"
}
