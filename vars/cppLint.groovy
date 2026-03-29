/**
 * Run cppcheck + clang-tidy static analysis.
 *
 * Usage:
 *   cppLint()
 *   cppLint(srcDir: 'src/', buildDir: 'build/')
 */
def call(Map args = [:]) {
    String srcDir   = args.get('srcDir',   'src/')
    String buildDir = args.get('buildDir', 'build/')

    sh """
        cppcheck --enable=all --error-exitcode=1 \\
            --suppress=missingIncludeSystem \\
            ${srcDir}
        run-clang-tidy -p ${buildDir} ${srcDir} || true
    """
}
