import org.gradle.kotlin.dsl.provideDelegate
import java.io.File
import java.io.FileInputStream
import java.util.*

object KeyHelper {

    const val KEY_STORE_FILE = "keyStoreFile"
    const val KEY_STORE_PASS = "keyStorePassword"
    const val KEY_ALIAS = "keyAlias"
    const val KEY_PASS = "keyPassword"
    const val KEY_SERVER_URL_DEV = "serverUrl_dev"
    const val KEY_SERVER_URL_PROD = "serverUrl_pro"
    const val KEY_POSTER_IMAGE_URL = "posterImageUrl"


    private val properties by lazy {
        Properties().apply { load(FileInputStream(File("key.properties"))) }
    }

    fun getValue(key: String): String {
        return properties.getProperty(key)
    }

    fun getFilePath(): Properties {
        return  properties
    }
}