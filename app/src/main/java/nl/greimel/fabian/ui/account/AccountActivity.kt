@file:OptIn(ExperimentalMaterial3Api::class)

package nl.greimel.fabian.ui.account

import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import nl.greimel.fabian.R
import nl.greimel.fabian.data.ApiKeyAccountManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavHostController, viewModel: AccountViewModel = viewModel()) {

    LaunchedEffect(Unit) {
        val apiKey = ApiKeyAccountManager.getInstance(navController.context).getApiKey()
        viewModel.setApiKey(apiKey)
        viewModel.notify = {
            Toast.makeText(navController.context, it, Toast.LENGTH_LONG).show()
        }
    }
    var preferencesManager = remember { ApiKeyAccountManager.getInstance(navController.context) }

    val apiKey by preferencesManager.apiKeyFlow.collectAsStateWithLifecycle(preferencesManager.getApiKey())
    val username by preferencesManager.userNameFlow.collectAsStateWithLifecycle(preferencesManager.getUsername())
    var showRegisterPage: Boolean by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.account_headline)) },

                )
        }
    ) {

        Row(Modifier.padding(it)) {
            if (apiKey == "") {
                if (showRegisterPage) {
                    RegisterScreen(onRegister = { username, password ->
                        viewModel.register(username, password)
                        showRegisterPage = false
                    })
                }
                LoginScreen(onLogin = { username, password ->
                    viewModel.login(username, password) {
                        preferencesManager.saveApiKey(
                            username,
                            it
                        )
                    }

                }) {
                    showRegisterPage = true
                }
            } else {
                UserDashboard(
                    username = username,
                    onLogout = { viewModel.logout { preferencesManager.removeApiKey() } })
            }
        }


    }
}

@Composable
fun LoginScreen(onLogin: (String, String) -> Unit, onNavigateToRegister: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val (username, setUsername) = remember { mutableStateOf("") }
        val (password, setPassword) = remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = username,
            onValueChange = setUsername,
            label = { Text(stringResource(R.string.username)) })
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = setPassword,
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isPasswordVisible) stringResource(R.string.hide_pw) else stringResource(
                            R.string.show_pw
                        )
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onLogin(username, password) }) {
            Text(stringResource(R.string.login))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onNavigateToRegister) {
            Text(stringResource(R.string.register))
        }
    }
}


@Composable
fun RegisterScreen(onRegister: (String, String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val (username, setUsername) = remember { mutableStateOf("") }
        val (password, setPassword) = remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = username,
            onValueChange = setUsername,
            label = { Text(stringResource(R.string.username)) })

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = setPassword,
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isPasswordVisible) stringResource(R.string.hide_pw) else stringResource(
                            R.string.show_pw
                        )
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onRegister(username, password) }) {
            Text(stringResource(R.string.register))
        }
    }
}


@Composable
fun UserDashboard(username: String, onLogout: () -> Unit) {
    val scaleValue by animateFloatAsState(
        1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = stringResource(R.string.user_icon),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(56.dp)
                .graphicsLayer(
                    scaleX = scaleValue,
                    scaleY = scaleValue
                )
        )
        if (username.isNotEmpty()) {
            Text(text = stringResource(R.string.logged_in_personal, username), style = MaterialTheme.typography.headlineMedium)
        } else {
            Text(text = stringResource(R.string.logged_in_anonym), style = MaterialTheme.typography.titleMedium)

        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLogout) {
            Text(stringResource(R.string.logout))
        }
    }
}


