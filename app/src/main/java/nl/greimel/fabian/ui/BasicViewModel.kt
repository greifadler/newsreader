package nl.greimel.fabian.ui

import androidx.lifecycle.ViewModel
import nl.greimel.fabian.api.ArticlesApi

abstract class BasicViewModel : ViewModel() {

    val api = ArticlesApi("https://inhollandbackend.azurewebsites.net/api")
    var notify: (String) -> Unit = {}


    abstract fun fetch()


    fun setApiKey(apiKey: String) {
        api.apiKey = apiKey
        fetch()
    }

}