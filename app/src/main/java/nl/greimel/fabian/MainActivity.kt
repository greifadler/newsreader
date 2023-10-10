package nl.greimel.fabian

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import nl.greimel.fabian.ui.account.AccountScreen
import nl.greimel.fabian.ui.reader.ReaderScreen
import nl.greimel.fabian.ui.theme.Newsreader721009Theme
import android.Manifest
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import nl.greimel.fabian.data.ApiKeyAccountManager
import nl.greimel.fabian.ui.reader.details.DetailsScreen


enum class Screen(val route: String, val resourceId: Int = 0, val icon: ImageVector) {
    HOME("home", R.string.home, Icons.Filled.Home),
    FAVORITES("favourites", R.string.favourites, Icons.Filled.Favorite),
    DETAILS("details/{articleId}", R.string.favourites, Icons.Filled.Favorite),
    CATEGORY("category/{categoryId}", R.string.favourites, Icons.Filled.Favorite),
    ACCOUNT("account", R.string.account, Icons.Filled.AccountCircle);
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val REQUEST_CODE = 1234

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.GET_ACCOUNTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.GET_ACCOUNTS),
                REQUEST_CODE
            )
        }

        setContent {
            Newsreader721009Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val items = mutableListOf(
                        Screen.HOME,
                        Screen.FAVORITES,
                        Screen.ACCOUNT
                    )

                    val preferencesManager = ApiKeyAccountManager.getInstance(applicationContext)

                    var selectedItem by remember { mutableStateOf(Screen.HOME) }

                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination

                            val apiKey by preferencesManager.apiKeyFlow.collectAsStateWithLifecycle(
                                preferencesManager.getApiKey()
                            )

                            NavigationBar {
                                items.forEachIndexed { index, item ->

                                    if (!(apiKey == "" && item == Screen.FAVORITES)) {


                                        NavigationBarItem(
                                            icon = {
                                                Icon(
                                                    item.icon,
                                                    contentDescription = item.route
                                                )
                                            },
                                            label = { Text(stringResource(item.resourceId).uppercase()) },
                                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                            onClick = {
                                                navController.navigate(item.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        )
                                    }

                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController,
                            startDestination = Screen.HOME.route,
                            Modifier.padding(innerPadding)
                        ) {
                            composable(
                                Screen.HOME.route,
                                enterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                                        animationSpec = tween(700)
                                    )
                                },
                            ) {
                                ReaderScreen(navController, onDetailsClick = { articleId ->
                                    navController.navigate(
                                        Screen.DETAILS.route.replace(
                                            "{articleId}",
                                            "$articleId"
                                        )
                                    )
                                })
                            }
                            composable(Screen.FAVORITES.route) {
                                ReaderScreen(
                                    navController,
                                    showOnlyFavourites = true,
                                    onDetailsClick = { articleId ->
                                        navController.navigate(
                                            Screen.DETAILS.route.replace(
                                                "{articleId}",
                                                "$articleId"
                                            )
                                        )
                                    })
                            }
                            composable(Screen.ACCOUNT.route) { AccountScreen(navController) }
                            composable(
                                Screen.DETAILS.route,
                                arguments = listOf(
                                    navArgument("articleId") {
                                        type = NavType.IntType
                                    }
                                ),
                                enterTransition = {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                                        animationSpec = tween(700)
                                    )
                                }
                            ) {
                                DetailsScreen(
                                    navController,
                                    articleId = it.arguments?.getInt("articleId") ?: -1,
                                    onCategoryClick = { categoryId ->
                                        navController.navigate(
                                            Screen.CATEGORY.route.replace(
                                                "{categoryId}",
                                                "$categoryId"
                                            )
                                        )
                                    }
                                )
                            }
                            composable(
                                Screen.CATEGORY.route,
                                arguments = listOf(navArgument("categoryId") {
                                    type = NavType.IntType
                                })
                            ) {
                                ReaderScreen(
                                    navController,
                                    category = it.arguments?.getInt("categoryId") ?: -1,
                                    onDetailsClick = { articleId ->
                                        navController.navigate(
                                            Screen.DETAILS.route.replace(
                                                "{articleId}",
                                                "$articleId"
                                            )
                                        )
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}

