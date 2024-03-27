package com.example.talkey_android.data.utils

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.biometric.BiometricPrompt.CryptoObject
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class EncryptDecryptManager {
    companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val AES_MODE =
            "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_CBC}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}"
        private const val SECRET_KEY_NAME = "SECRET_KEY_ENCRYPT_DECRYPT_IKIGII"
        private const val SEPARATOR = "-"

        private fun generateSecretKey() {
            Log.d(">", "l> generateSecretKey")
            try {
                val keyGenerator =
                    KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
                val keyGenParameterSpecBuilder = KeyGenParameterSpec.Builder(
                    SECRET_KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setRandomizedEncryptionRequired(true)
                    .setKeySize(256)
                    .setUserAuthenticationRequired(true)
                    .setInvalidatedByBiometricEnrollment(true)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    keyGenParameterSpecBuilder.setUserAuthenticationParameters(
                        60,
                        KeyProperties.AUTH_BIOMETRIC_STRONG
                    )
                } else {
                    keyGenParameterSpecBuilder.setUserAuthenticationValidityDurationSeconds(60)
                }

                keyGenerator.init(keyGenParameterSpecBuilder.build())
                keyGenerator.generateKey()
            } catch (noSuchAlgorithmException: NoSuchAlgorithmException) {
                Log.d(">", "l> noSuchAlgorithmException: ${noSuchAlgorithmException.message}")
                throw RuntimeException(
                    "Failed to create a symmetric key noSuchAlgorithmException",
                    noSuchAlgorithmException
                )
            } catch (noSuchProviderException: NoSuchProviderException) {
                Log.d(">", "l> noSuchProviderException: ${noSuchProviderException.message}")
                throw RuntimeException(
                    "Failed to create a symmetric key noSuchProviderException",
                    noSuchProviderException
                )
            } catch (invalidAlgorithmParameterException: InvalidAlgorithmParameterException) {
                Log.d(
                    ">",
                    "l> invalidAlgorithmParameterException: ${invalidAlgorithmParameterException.message}"
                )
                throw RuntimeException(
                    "Failed to create a symmetric key invalidAlgorithmParameterException",
                    invalidAlgorithmParameterException
                )
            }
        }

        private fun keyExists(keyStore: KeyStore): Boolean {
            val aliases = keyStore.aliases()
            while (aliases.hasMoreElements()) {
                if (SECRET_KEY_NAME == aliases.nextElement()) {
                    return true
                }
            }
            return false
        }

        private fun getKeyFromKeyStore(): SecretKey {
            val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)
            if (!keyExists(keyStore)) {
                generateSecretKey()
            }

            return keyStore.getKey(SECRET_KEY_NAME, null) as SecretKey
        }

        private fun getCryptoObjectEncrypt(): CryptoObject {
            val cipher = Cipher.getInstance(AES_MODE)
            cipher.init(Cipher.ENCRYPT_MODE, getKeyFromKeyStore())
            return CryptoObject(cipher)
        }

        private fun getCryptoObjectDecrypt(encrypted: String): CryptoObject {
            val cipher = Cipher.getInstance(AES_MODE)
            val iv = encrypted.split(SEPARATOR)[1].replace("\n", "")
            cipher.init(
                Cipher.DECRYPT_MODE,
                getKeyFromKeyStore(),
                IvParameterSpec(Base64.decode(iv.toByteArray(Charsets.UTF_8), Base64.DEFAULT))
            )
            return CryptoObject(cipher)
        }

        fun encrypt(plainText: String): String {
            return try {
                val cryptoObject = getCryptoObjectEncrypt()
                val cipher = cryptoObject.cipher
                if (cipher != null) {
                    Base64.encodeToString(
                        cipher.doFinal(plainText.toByteArray(Charsets.UTF_8)),
                        Base64.DEFAULT
                    ) + SEPARATOR + Base64.encodeToString(
                        cipher.iv,
                        Base64.DEFAULT
                    )
                } else {
                    ""
                }
            } catch (exception: Exception) {
                Log.e(
                    ">",
                    "l> Hemos tenenido un problema encriptando ${exception::class.simpleName}: ${exception.message}, ${exception.cause}"
                )
                ""
            }
        }

        fun decrypt(encrypted: String): String {
            return try {
                val cryptoObject = getCryptoObjectDecrypt(encrypted)
                val message = encrypted.split(SEPARATOR)[0]
                val cipher = cryptoObject.cipher
                cipher?.doFinal(Base64.decode(message, Base64.DEFAULT))?.toString(Charsets.UTF_8)
                    ?: ""
            } catch (exception: Exception) {
                Log.e(
                    ">",
                    "l> Hemos tenenido un problema desencriptando ${exception::class.simpleName}: ${exception.message}, ${exception.cause}"
                )
                ""
            }
        }

        fun removeKey() {
            val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)
            if (keyExists(keyStore)) {
                keyStore.deleteEntry(SECRET_KEY_NAME)
            }
        }
    }
}