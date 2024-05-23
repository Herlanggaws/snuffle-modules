package com.logixmates.snuffle.auth.data.model

/**
 * Designed and developed by Herlangga Wicaksono on 31/12/23.
 * @LinkedIn (https://www.linkedin.com/in/herlangga-wicaksono-4072a5a2/)
 */
enum class VerificationStatus(val value: String) {
    VERIFIED_STATUS("VERIFIED"),
    NEED_VERIFICATION_STATUS("NEED VERIFICATION"),
    WAITING_FOR_VERIFICATION_STATUS("WAITING FOR VERIFICATION")
}