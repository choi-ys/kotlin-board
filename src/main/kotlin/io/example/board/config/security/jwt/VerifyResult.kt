package io.example.board.config.security.jwt

import com.auth0.jwt.interfaces.Claim

data class VerifyResult(
    val success: Boolean,
    val issuer: String,
    val subject: String,
    val audience: String,
    val issuedAt: String,
    val expiresAt: String,
    val principal: String
) {
    companion object {
        fun mapFor(success:Boolean, claims: Map<String, Claim>): VerifyResult {
            return VerifyResult(
                success = success,
                issuer = claims[ClaimKey.ISS.value]!!.asString(),
                subject = claims[ClaimKey.SUB.value]!!.asString(),
                audience = claims[ClaimKey.AUD.value]!!.asString(),
                issuedAt = claims[ClaimKey.IAT.value]!!.asString(),
                expiresAt = claims[ClaimKey.EXP.value]!!.asString(),
                principal = claims[ClaimKey.PRINCIPAL.value]!!.asString(),
            )
        }
    }
}