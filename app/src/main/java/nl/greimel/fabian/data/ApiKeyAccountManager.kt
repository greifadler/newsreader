package nl.greimel.fabian.data
import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import nl.greimel.fabian.R

class ApiKeyAccountManager private constructor(private val context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: ApiKeyAccountManager? = null

        fun getInstance(context: Context): ApiKeyAccountManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiKeyAccountManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val accountType = context.getString(R.string.account_type)
    private val accountName = context.getString(R.string.account_name)
    private val KEY_API_USER = context.getString(R.string.key_api_user)

    private val accountManager = AccountManager.get(context)
    private val account = Account(accountName, accountType)

    init {
        if (accountManager.getAccountsByType(accountType).isEmpty()) {
            accountManager.addAccountExplicitly(account, null, null)
        }
    }

    fun saveApiKey(userName: String, apiKey: String) {
        accountManager.setUserData(account, KEY_API_USER, userName)
        accountManager.setPassword(account, apiKey)
        updateFlow()
    }

    fun getApiKey(): String {
        return accountManager.getPassword(account) ?: ""
    }

    fun getUsername(): String {
        return accountManager.getUserData(account, KEY_API_USER) ?: ""
    }

    private val _apiKeyFlow: MutableSharedFlow<String> = MutableSharedFlow()
    val apiKeyFlow: SharedFlow<String> get() = _apiKeyFlow.asSharedFlow()
    val userNameFlow: MutableSharedFlow<String> = MutableSharedFlow()

    fun removeApiKey() {
        accountManager.clearPassword(account)
        accountManager.setUserData(account, KEY_API_USER, null)
        updateFlow()
    }

    private fun updateFlow() {
        GlobalScope.launch(Dispatchers.Main) {
            _apiKeyFlow.emit(getApiKey())
            userNameFlow.emit(getUsername())
        }
    }
}
