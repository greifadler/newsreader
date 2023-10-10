package nl.greimel.fabian.ui.widget

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import nl.greimel.fabian.MainActivity
import nl.greimel.fabian.R
import nl.greimel.fabian.data.ApiKeyAccountManager
import nl.greimel.fabian.model.Article
import nl.greimel.fabian.ui.reader.LoadingScreen
import nl.greimel.fabian.ui.reader.NoArticlesFound
import nl.greimel.fabian.ui.reader.ReaderViewModel

class LatestArticleWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            // create your AppWidget here
            MyContent(context)
        }
    }

    @Composable
    private fun MyContent(context: Context, viewModel: ReaderViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {


        LaunchedEffect(Unit) {
//            val apiKey = ApiKeyAccountManager.getInstance(context).getApiKey()
//            viewModel.setApiKey(apiKey)
            viewModel.fetchArticles()
        }

        val articles = viewModel.articles.collectAsState().value
        val fetching = viewModel.fetching.collectAsState().value

        if (!fetching)
            if (articles.isNotEmpty())
                WidgetArticleCard(
                    article = articles.first(),
                    onClick = { actionStartActivity<MainActivity>() }, context)
            else
                NoArticlesFound()
        else
            LoadingScreen()
    }

    @Composable
    private fun WidgetArticleCard(article: Article, onClick: () -> Unit, context: Context) {
        Row(
            modifier = GlanceModifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            article.image?.let {
//                AsyncImage(
//                    model = ImageRequest.Builder(context)
//                        .data(it)
//                        .crossfade(true)
//                        .placeholder(R.drawable.baseline_image_24) // sadly that didn't work                         //.placeholder(Icons.Filled.Image.toDrawable(LocalContext.current))
//                        .build(),
//                    contentDescription = it,
//                    modifier = Modifier
//                        .size(100.dp)
//                        .clip(MaterialTheme.shapes.medium)
//                        .background(MaterialTheme.colorScheme.surface),
//                    contentScale = ContentScale.Crop
//                )
            }
            Spacer(modifier = GlanceModifier.width(8.dp))

            // Article details
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = article.title ?: "",
                )
            }


        }
    }
}
