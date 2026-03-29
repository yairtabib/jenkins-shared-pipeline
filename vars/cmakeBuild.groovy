/**
 * Run parallel cmake builds: Debug+ASan and Release.
 *
 * Usage:
 *   cmakeBuild()
 *   cmakeBuild(srcDir: '.', jobs: '4')
 */
def call(Map args = [:]) {
    String srcDir = args.get('srcDir', '.')
    String jobs   = args.get('jobs',   '$(nproc)')

    parallel(
        'Build (Debug + ASan)': {
            sh """
                cmake -B build -S ${srcDir} \\
                    -DCMAKE_BUILD_TYPE=Debug \\
                    -DENABLE_SANITIZERS=ON \\
                    -DCMAKE_EXPORT_COMPILE_COMMANDS=ON
                cmake --build build --parallel ${jobs}
            """
        },
        'Build (Release)': {
            sh """
                cmake -B build-release -S ${srcDir} \\
                    -DCMAKE_BUILD_TYPE=Release
                cmake --build build-release --parallel ${jobs}
            """
        }
    )
}
