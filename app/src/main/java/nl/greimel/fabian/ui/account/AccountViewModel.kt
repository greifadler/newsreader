package nl.greimel.fabian.ui.account

import android.accounts.Account
import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import nl.greimel.fabian.R
import nl.greimel.fabian.model.UserLogin
import nl.greimel.fabian.model.UserRegister
import nl.greimel.fabian.ui.BasicViewModel

class AccountViewModel : BasicViewModel() {


    private val _fetching = MutableStateFlow(false)
    val fetching: StateFlow<Boolean> = _fetching

    fun login(userName: String, password: String, saveApiKey: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val loginResponse = api.usersLoginPost(
                    UserLogin(
                        userName,
                        password
                    )
                )

                loginResponse.authToken?.let {
                    saveApiKey(it)
//                    viewModelScope.launch(Dispatchers.Main) {
//                        run { notify(stringResource(R.string.msg_login_success)) }
//                    }
                }


            } catch (ex: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    ex.message?.let { notify(it) }
                }
                System.err.println(ex.message)
            }
        }
    }


    fun logout(removeApiKey: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            removeApiKey()
        }

    }

    fun register(userName: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val registerResponse = api.usersRegisterPost(
                    UserRegister(
                        userName,
                        password
                    )
                )

                registerResponse.success?.let {
                    viewModelScope.launch(Dispatchers.Main) {
                        registerResponse.message?.let { msg -> notify(msg) }
                    }
                }


            } catch (ex: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    ex.message?.let { notify(it) }
                }
                System.err.println(ex.message)
            }
        }
    }

    override fun fetch() {
    }

}

