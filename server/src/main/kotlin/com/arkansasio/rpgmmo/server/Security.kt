package com.arkansasio.rpgmmo.server

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object Passwords {
    private const val ITERATIONS = 600_000
    private const val KEY_BITS = 256
    private val random = SecureRandom()

    fun hash(password: String): String {
        require(password.length >= 8) { "Password must be at least 8 characters" }
        val salt = ByteArray(16).also(random::nextBytes)
        val hash = derive(password, salt, ITERATIONS)
        return "pbkdf2-sha256$$ITERATIONS$${b64(salt)}$${b64(hash)}"
    }

    fun verify(password: String, encoded: String): Boolean = runCatching {
        val parts = encoded.split('$')
        if (parts.size != 4 || parts[0] != "pbkdf2-sha256") return false
        val iterations = parts[1].toInt()
        val salt = Base64.getUrlDecoder().decode(parts[2])
        val expected = Base64.getUrlDecoder().decode(parts[3])
        MessageDigest.isEqual(expected, derive(password, salt, iterations))
    }.getOrDefault(false)

    private fun derive(password: String, salt: ByteArray, iterations: Int): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, KEY_BITS)
        return try { SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded } finally { spec.clearPassword() }
    }

    private fun b64(value: ByteArray) = Base64.getUrlEncoder().withoutPadding().encodeToString(value)
}

object Tokens {
    private val random = SecureRandom()

    fun access(userId: String, config: ServerConfig, now: Long = System.currentTimeMillis() / 1000): String {
        val exp = now + config.accessTtlSeconds
        val header = b64("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".toByteArray())
        val payload = b64("{\"sub\":\"${escape(userId)}\",\"iat\":$now,\"exp\":$exp,\"aud\":\"rpgmmo-api\"}".toByteArray())
        return "$header.$payload.${sign("$header.$payload", config.jwtSecret)}"
    }

    fun verifyAccess(token: String, config: ServerConfig, now: Long = System.currentTimeMillis() / 1000): String? = runCatching {
        val parts = token.split('.')
        if (parts.size != 3) return null
        val expected = sign("${parts[0]}.${parts[1]}", config.jwtSecret)
        if (!MessageDigest.isEqual(expected.toByteArray(), parts[2].toByteArray())) return null
        val payload = String(Base64.getUrlDecoder().decode(parts[1]))
        val sub = Regex("\\\"sub\\\":\\\"([^\\\"]+)\\\"").find(payload)?.groupValues?.get(1) ?: return null
        val exp = Regex("\\\"exp\\\":(\\d+)").find(payload)?.groupValues?.get(1)?.toLong() ?: return null
        if (exp <= now) return null
        sub
    }.getOrNull()

    fun refresh(): String = ByteArray(48).also(random::nextBytes).let { b64(it) }
    fun hashRefresh(token: String): String = MessageDigest.getInstance("SHA-256").digest(token.toByteArray()).joinToString("") { "%02x".format(it) }

    private fun sign(input: String, secret: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(javax.crypto.spec.SecretKeySpec(secret.toByteArray(), "HmacSHA256"))
        return b64(mac.doFinal(input.toByteArray()))
    }
    private fun b64(value: ByteArray) = Base64.getUrlEncoder().withoutPadding().encodeToString(value)
    private fun escape(value: String) = value.replace("\\", "\\\\").replace("\"", "\\\"")
}
