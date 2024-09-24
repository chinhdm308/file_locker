package com.base.presentation.component.patternlockview

class LockPatternUtils {
    companion object {
        /**
         * The minimum number of dots in a valid pattern.
         */
        const val MIN_LOCK_PATTERN_SIZE = 4

        /**
         * The maximum number of incorrect attempts before the user is prevented
         * from trying again for [.FAILED_ATTEMPT_TIMEOUT_MS].
         */
        const val FAILED_ATTEMPTS_BEFORE_TIMEOUT = 5

        /**
         * The minimum number of dots the user must include in a wrong pattern
         * attempt for it to be counted against the counts that affect
         * [.FAILED_ATTEMPTS_BEFORE_TIMEOUT] and
         * [.FAILED_ATTEMPTS_BEFORE_RESET]
         */
        const val MIN_PATTERN_REGISTER_FAIL = MIN_LOCK_PATTERN_SIZE

        /**
         * How long the user is prevented from trying again after entering the wrong
         * pattern too many times.
         */
        const val FAILED_ATTEMPT_TIMEOUT_MS = 30000L
    }
}