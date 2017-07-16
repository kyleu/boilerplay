package util

import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

import org.apache.commons.codec.binary.Base64

object PasswordEncryptUtils {
  private[this] val key: String = "DmNB6cZ6aOtYO8WxhdDW1poREkXm7swKCcOVmbsB"

  private[this] val encryptCipher = {
    val c = Cipher.getInstance("AES/ECB/PKCS5Padding")
    c.init(Cipher.ENCRYPT_MODE, keyToSpec(key))
    c
  }

  private[this] val decryptCipher = {
    val c = Cipher.getInstance("AES/ECB/PKCS5Padding")
    c.init(Cipher.DECRYPT_MODE, keyToSpec(key))
    c
  }

  def encrypt(value: String) = {
    if (value.isEmpty) {
      ""
    } else {
      val encrypted = encryptCipher.doFinal(value.getBytes("UTF-8"))
      Base64.encodeBase64String(encrypted)
    }
  }

  def decrypt(encryptedValue: String) = {
    if (encryptedValue.isEmpty) {
      ""
    } else {
      val decoded = Base64.decodeBase64(encryptedValue)
      new String(decryptCipher.doFinal(decoded))
    }
  }

  private[this] def keyToSpec(key: String): SecretKeySpec = {
    var keyBytes = key.getBytes("UTF-8")
    val sha = MessageDigest.getInstance("SHA-1")
    keyBytes = sha.digest(keyBytes).take(16)
    new SecretKeySpec(keyBytes, "AES")
  }
}

