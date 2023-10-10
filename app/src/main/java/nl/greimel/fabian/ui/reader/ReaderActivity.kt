package nl.greimel.fabian.ui.reader

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import nl.greimel.fabian.R
import nl.greimel.fabian.model.Article
import nl.greimel.fabian.data.ApiKeyAccountManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    navController: NavHostController,
    viewModel: ReaderViewModel = viewModel(),
    showOnlyFavourites: Boolean = false,
    category: Int = -1,
    onDetailsClick: (Int) -> Unit
) {

    LaunchedEffect(Unit) {
        val apiKey = ApiKeyAccountManager.getInstance(navController.context).getApiKey()
        viewModel.setApiKey(apiKey)
        viewModel.setCategory(category)
        viewModel.notify = {
            Toast.makeText(navController.context, it, Toast.LENGTH_LONG).show()
        }
        viewModel.favouriteOnlyMode = showOnlyFavourites
    }

    val articles = viewModel.articles.collectAsState().value
    val fetching = viewModel.fetching.collectAsState().value
    val fetchingMoreArticles = viewModel.fetchingMoreArticles.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                    Row() {

                        if (category != -1) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(id = R.string.back),
                                Modifier.clickable {
                                    navController.popBackStack()
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Text(
                            if (category != -1) stringResource(R.string.cat_headline) else if (showOnlyFavourites) stringResource(
                                R.string.fav_headline
                            ) else stringResource(
                                R.string.home_headline
                            )
                        )
                    }

                },
                actions = {
                    IconButton(onClick = {
                        viewModel.fetchArticles()
                    }) {
                        Icon(
                            Icons.Filled.Refresh,
                            contentDescription = stringResource(R.string.refresh)
                        )
                    }
                }
            )
        }
    ) {
        if (fetching) {
            LoadingScreen()
        } else {
            if (if (showOnlyFavourites) articles.none { it.isLiked == true } else articles.isEmpty()) {
                if (showOnlyFavourites)
                    NoLikedArticlesFound()
                else
                    NoArticlesFound()
            } else {
                val listState = rememberLazyListState()

                LazyColumn(
                    state = listState,
                    contentPadding = it
                ) {
                    items(articles) { article ->
                        ArticleCard(article = article, onClick = { art ->
                            art.id?.let { it1 -> onDetailsClick(it1) }
                        }, !showOnlyFavourites)
                    }
                    if (fetchingMoreArticles)
                        item {
                            Row(Modifier.padding(16.dp)) {
                                LoadingScreen()
                            }
                        }
                }

                LaunchedEffect(listState) {
                    snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                        .collect { lastIndex ->
                            if (lastIndex == articles.size - 1) {
                                viewModel.fetchMoreArticles()
                            }
                        }
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()  // A circular loading indicator
    }
}

@Composable
fun NoArticlesFound() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.no_articles_found),
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.no_articles_found),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun NoLikedArticlesFound() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder, // Use heart border icon as it represents likes
                contentDescription = stringResource(R.string.no_liked_articles),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.no_liked_articles),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}


@Composable
fun ArticleCard(article: Article, onClick: (Article) -> Unit, showLiked: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(article) }
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            article.image?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it)
                        .crossfade(true)
                        .placeholder(R.drawable.baseline_image_24) // sadly that didn't work                         //.placeholder(Icons.Filled.Image.toDrawable(LocalContext.current))
                        .build(),
                    contentDescription = it,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surface),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            // Article details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

            }


        }
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            // Article details
            Column(modifier = Modifier.weight(1f)) {
                Text(text = truncateToWords(article.summary, 15))
            }
            if (showLiked && article.isLiked == true) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.liked),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                )
            }
        }
    }
}

fun truncateToWords(input: String?, wordLimit: Int): String {
    if (input == null) return ""

    val words = input.split(" ").take(wordLimit)
    return if (words.size < wordLimit || words.size == input.split(" ").size) {
        words.joinToString(" ")
    } else {
        "${words.joinToString(" ")}..."
    }
}
