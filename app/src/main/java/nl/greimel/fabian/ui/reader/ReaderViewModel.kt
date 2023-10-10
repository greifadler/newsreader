package nl.greimel.fabian.ui.reader

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.greimel.fabian.model.Article
import nl.greimel.fabian.ui.BasicViewModel

class ReaderViewModel : BasicViewModel() {

    private val _fetching = MutableStateFlow(false)
    val fetching: StateFlow<Boolean> = _fetching

    private val _fetchingMoreArticles = MutableStateFlow(false)
    val fetchingMoreArticles: StateFlow<Boolean> = _fetchingMoreArticles

    private val _articles = MutableStateFlow<MutableList<Article>>(mutableListOf())
    val articles: StateFlow<List<Article>> = _articles

    private var category: Int = -1
    private var nextId: Int = -1
    public var favouriteOnlyMode = false

    private val count = 20

    fun fetchArticles() {
        println("$count articles will be fetched")

        _fetching.value = true
        viewModelScope.launch {
            _articles.value.clear()
            _articles.value.addAll(getArticles())
            println("${_articles.value.size} articles fetched")
            _fetching.value = false
        }
    }

    private suspend fun getArticles(): List<Article> = withContext(Dispatchers.IO) {
        try {
            val articles = if (favouriteOnlyMode) {
                api.articlesLikedGet()
            } else {
                api.articlesGet(count, null, null, if (category != -1) category else null)
            }

            articles.nextId?.let { nextId = it }
            return@withContext articles.results ?: emptyList()
        } catch (ex: Exception) {
            ex.message?.let {
                viewModelScope.launch(Dispatchers.Main) {
                    notify(it)
                }
                System.err.println(ex.message)
            }
            return@withContext emptyList()
        }

    }

    private suspend fun getMoreArticles(lastOne: Int): List<Article> = withContext(Dispatchers.IO) {
        try {
            val articles =
                api.articlesIdGet(
                    lastOne,
                    count,
                    null,
                    null,
                    if (category != -1) category else null
                )
            articles.nextId?.let { nextId = it }
            return@withContext articles.results ?: emptyList()
        } catch (ex: Exception) {
            ex.message?.let {
                viewModelScope.launch(Dispatchers.Main) {
                    notify(it)
                }
                System.err.println(ex.message)
            }
            return@withContext emptyList()
        }

    }

    override fun fetch() {
        fetchArticles()
    }

    fun fetchMoreArticles() {
        if (nextId != -1 && !favouriteOnlyMode) {
            println("$count extra articles will be fetched")

            _fetchingMoreArticles.value = true
            viewModelScope.launch {
                _articles.value.addAll(getMoreArticles(nextId))
                _articles.value.forEach { it ->
                    println("${it.id}  ${it.title}")
                }
                println("${_articles.value.size} extra articles fetched")
                _fetchingMoreArticles.value = false
            }
        }
    }

    fun setCategory(category: Int) {
        this.category = category
    }


}
