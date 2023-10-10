package nl.greimel.fabian.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@Deprecated("replaced by ApiKeyAccountManager - a more secure approach")
class ApiKeyPreferencesManager private constructor(private val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ApiKeyPreferencesManager? = null

        fun getInstance(context: Context): ApiKeyPreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiKeyPreferencesManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    private val PREFS_NAME = "app_prefs"
    private val KEY_API_KEY = "api_key"
    private val KEY_API_USER = "api_username"

    init {
        updateFlow()
    }

    private fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    private fun removeData(key: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, "")
        editor.apply()
    }



    fun saveApiKey(userName: String, apiKey: String) {
        saveData(KEY_API_USER, userName)
        saveData(KEY_API_KEY, apiKey)
        updateFlow()
    }

    fun getApiKey(): String {
        return getData(KEY_API_KEY, "")
    }

    fun getUsername(): String {
        return getData(KEY_API_USER, "")
    }


    private val _apiKeyFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val apiKeyFlow: SharedFlow<String> get() = _apiKeyFlow.asSharedFlow()

    val userNameFlow: MutableSharedFlow<String> = MutableSharedFlow()

    fun removeApiKey() {
        removeData(KEY_API_KEY)
        removeData(KEY_API_USER)

        updateFlow()

    }

    private fun updateFlow() {
        GlobalScope.launch(Dispatchers.Main) {
            _apiKeyFlow.emit(getApiKey())
            userNameFlow.emit(getUsername())
        }
    }
}