package com.chicohan.samplelistapp.helper

import android.annotation.SuppressLint
import android.security.keystore.KeyProperties
import android.util.Log
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import java.util.Base64
import javax.crypto.spec.SecretKeySpec
import android.security.keystore.KeyGenParameterSpec


object EncryptionHelper  {

    /**
    - singleton helper use to encrypt user data
     */
    private const val AES_KEY_SIZE = 256
    private const val GCM_IV_LENGTH = 12
    private const val GCM_TAG_LENGTH = 128
    private const val KEY_ALIAS = "my_secure_aes_key"
    init {
        generateAndStoreKey()
    }
    // Generate a 256-bit AES key
    private fun generateAndStoreKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }

        // Check if the key already exists in the keystore
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(AES_KEY_SIZE)
                .build()

            keyGen.init(keyGenParameterSpec)
            return keyGen.generateKey()
        } else {
            // Retrieve the existing key
            return keyStore.getKey(KEY_ALIAS, null) as SecretKey
        }
    }
    fun getKey(): SecretKey? {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
            load(null)
        }

        return keyStore.getKey(KEY_ALIAS, null) as? SecretKey
    }
    fun encrypt(data: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv // Get the automatically generated IV
        val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        val combined = iv + encryptedData // Prepend the IV to the encrypted data
        return Base64.getEncoder().encodeToString(combined) // Convert to Base64 string
    }
    fun decrypt(encryptedData: String, secretKey: SecretKey): String {
        val decodedData = Base64.getDecoder().decode(encryptedData) // Decode Base64 string to ByteArray
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val iv = decodedData.copyOfRange(0, GCM_IV_LENGTH) // Extract the IV (first 12 bytes)
        val actualEncryptedData = decodedData.copyOfRange(GCM_IV_LENGTH, decodedData.size)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        val decryptedData = cipher.doFinal(actualEncryptedData)
        return String(decryptedData, Charsets.UTF_8)
    }
}