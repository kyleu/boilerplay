package util

import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

import org.apache.commons.codec.binary.Base64

import scala.util.control.NonFatal

object EncryptionUtils {
  private[this] var key: Option[String] = None
  private[this] val salt = "salt-value-changeme"
  private[this] lazy val spec = keyToSpec(key.getOrElse(throw new IllegalStateException("Encryption key has not been set.")))

  def setKey(k: String) = key = Some(k)

  def encrypt(value: String) = {
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, spec)
    Base64.encodeBase64String(cipher.doFinal(value.getBytes("UTF-8")))
  }

  def decrypt(encryptedValue: String, throwOnError: Boolean = false) = try {
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
    cipher.init(Cipher.DECRYPT_MODE, spec)
    new String(cipher.doFinal(Base64.decodeBase64(encryptedValue)))
  } catch {
    case NonFatal(x) => if (throwOnError) { throw x } else { encryptedValue }
  }

  private[this] def keyToSpec(key: String) = {
    var keyBytes: Array[Byte] = (salt + key).getBytes("UTF-8")
    val sha: MessageDigest = MessageDigest.getInstance("SHA-1")
    keyBytes = sha.digest(keyBytes)
    keyBytes = java.util.Arrays.copyOf(keyBytes, 16)
    new SecretKeySpec(keyBytes, "AES")
  }
}
