package com.securingsam

/**
 * Shared pipeline configuration constants.
 */
class PipelineConfig implements Serializable {
    static final String SHARED_LIB_NAME        = 'securingsam-shared-pipeline'
    static final int    DEFAULT_TIMEOUT_MIN     = 45
    static final int    MAX_BUILDS_TO_KEEP      = 20
    static final String KOALA_TOKEN_CRED_ID     = 'koala-token'
    static final String KOALA_USERNAME_CRED_ID  = 'koala-username'
    static final String GITHUB_WEBHOOK_CRED_ID  = 'github-webhook-secret'
    static final String GITHUB_APP_CRED_ID      = 'github-app'
}
