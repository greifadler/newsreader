package nl.greimel.fabian.ui.reader.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import nl.greimel.fabian.R
import nl.greimel.fabian.model.Article
import nl.greimel.fabian.model.Category
import nl.greimel.fabian.data.ApiKeyAccountManager
import nl.greimel.fabian.ui.reader.LoadingScreen
import nl.greimel.fabian.ui.theme.Newsreader721009Theme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DetailsScreen(
    navController: NavHostController, articleId: Int, onCategoryClick: (Int) -> Unit
) {
    val viewModel: DetailsViewModel = viewModel()

    LaunchedEffect(Unit) {
        val apiKey = ApiKeyAccountManager.getInstance(navController.context).getApiKey()
        viewModel.fetchArticle(articleId)
        viewModel.setApiKey(apiKey)
        viewModel.notify = {
            Toast.makeText(navController.context, it, Toast.LENGTH_LONG).show()
        }
    }

    val article = viewModel.article.collectAsState().value
    val fetching = viewModel.fetching.collectAsState().value

    if (!fetching && article != null) {
        ArticleDetail(article, { id, liked ->
            viewModel.changeLikeArticle(id, liked)
        }, {
            navController.popBackStack()
        }, onCategoryClick
        )
    } else {
        LoadingScreen()
    }
}

@Composable
fun ArticleDetail(
    article: Article,
    changeLikeArticle: (Int, Boolean) -> Unit,
    onBack: () -> Unit,
    onCategoryClick: (Int) -> Unit
) {
    val typography = MaterialTheme.typography
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(colors.background)
        ) {
            // Back button at the top
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { onBack() }  // You may want to move this clickable to the back icon only.
                .padding(8.dp)
                .background(
                    color = colors.primary.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)
                ),
                horizontalArrangement = Arrangement.SpaceBetween,  // Changed to SpaceBetween to push like button to the end
                verticalAlignment = Alignment.CenterVertically) {
                // Back icon and text
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back Icon and Text
                    Row(horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onBack() }  // Clickable moved here for the back functionality
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = colors.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.back),
                            style = typography.titleSmall.copy(color = colors.primary)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))  // Separate the Back and Share by 16.dp

                    article.url?.let { url ->


                        Row(horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT, url
                                    )
                                    type = context.getString(R.string.share_type)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                context.startActivity(shareIntent)
                            }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = stringResource(R.string.share),
                                tint = colors.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.share),
                                style = typography.titleSmall.copy(color = colors.primary)
                            )
                        }
                    }
                }


                // Like/Unlike button
                Icon(imageVector = if (article.isLiked == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (article.isLiked == true) stringResource(R.string.liked) else stringResource(
                                            R.string.not_liked),
                    tint = if (article.isLiked == true) Color.Green else Color.Gray,
                    modifier = Modifier.clickable {

                        article.id?.let {
                            article.isLiked?.let { it1 ->
                                changeLikeArticle(
                                    it, it1
                                )
                            }
                        }
                    })
            }


            Spacer(modifier = Modifier.height(16.dp))

            article.image?.let { imageUrl ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(imageUrl)
                        .crossfade(true)
                        .placeholder(R.drawable.baseline_image_24)
                        .build(), // sadly no possible with Icons.Filled.Image (couldn't convert it, maybe you know how to? :)
                    contentDescription = imageUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)  // This ensures a 16:9 aspect ratio
                        .clip(MaterialTheme.shapes.medium)
                        .background(colors.surface),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Article Title
            Text(
                text = article.title ?: stringResource(R.string.no_title),
                style = typography.titleSmall,
                color = colors.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Article Summary
            Text(
                text = article.summary ?: stringResource(R.string.no_summary), style = typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            val originalFormat = DateTimeFormatter.ofPattern(stringResource(R.string.original_date_format))
            val desiredFormat = DateTimeFormatter.ofPattern(stringResource(R.string.desired_date_format))

            val publishDateParsed = LocalDateTime.parse(article.publishDate, originalFormat)
            val formattedDate = publishDateParsed.format(desiredFormat)

            Text(
                text = stringResource(R.string.published_date, formattedDate), style = typography.labelMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Categories
            article.categories?.let { categories ->
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = stringResource(R.string.categories), style = typography.labelMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    categories.forEach { category ->
                        CategoryBadge(category, onCategoryClick)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val uri = Uri.parse(article.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(intent)
                },
            ) {
                Text(
                    text = stringResource(R.string.full_article_desc),
                    color = colors.onPrimary  // This sets the text color. Adjust if needed.
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            article.related?.let {
                if (it.isNotEmpty()) {
                    Text(stringResource(R.string.related_hl))
                    // Related articles
                    Row {
                        article.related?.forEach { relatedArticle ->
                            ClickableText(
                                text = AnnotatedString.Builder(relatedArticle).apply {
                                    withStyle(style = SpanStyle()) {
                                        append(relatedArticle)
                                    }
                                }.toAnnotatedString(),
                                style = typography.labelMedium,
                                onClick = {
                                    val uri = Uri.parse(relatedArticle)
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    context.startActivity(intent)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}


@Composable
fun CategoryBadge(category: Category, onClick: (Int) -> Unit) {
    val typography = MaterialTheme.typography

    category.name?.let {

        Surface(
            modifier = Modifier
                .padding(end = 8.dp)
                .clickable { category.id?.let { onClick(it) } },
            shape = RoundedCornerShape(12.dp),
            color = Color.LightGray  // Change to your preferred badge background color
        ) {
            Text(
                text = it,
                style = typography.labelSmall,  // Change to your preferred badge text color
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}






