package nl.greimel.fabian.ui.reader.details

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.greimel.fabian.model.Article
import nl.greimel.fabian.ui.BasicViewModel

class DetailsViewModel : BasicViewModel() {

    private val _fetching = MutableStateFlow(false)
    val fetching: StateFlow<Boolean> = _fetching

    private val _article = MutableStateFlow<Article?>(null)
    val article: StateFlow<Article?> = _article


    fun fetchArticle(id: Int, withFetching: Boolean = true) {
        if (withFetching)
            _fetching.value = true
        viewModelScope.launch {
            _article.value = getArticle(id)
            println("article #${_article.value?.id} fetched")
            if (withFetching)
                _fetching.value = false
        }
    }

    private suspend fun getArticle(id: Int): Article? = withContext(Dispatchers.IO) {
        try {
            val articles = api.articlesIdGet(id, null, null, null, null)
            return@withContext articles.results?.first()
        } catch (ex: Exception) {
            ex.message?.let { notify(it) }
            System.err.println(ex.message)
            return@withContext null
        }

    }

    override fun fetch() {
        article.value?.id?.let {
            fetchArticle(it)
        }
    }

    fun changeLikeArticle(id: Int, liked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (liked) {
                    api.articlesIdLikeDelete(id)
                } else {
                    api.articlesIdLikePut(id)
                }
                fetchArticle(id, false)
            } catch (ex: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    ex.message?.let { notify(it) }
                }
                System.err.println(ex.message)
            }
        }

    }

}
