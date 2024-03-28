package com.example.marketplacepuj.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.marketplacepuj.ui.login.ui.theme.MarketplacePUJTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarketplacePUJTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

/*
@Composable
fun LoginScreen(
    navigateToHome: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppBar(
                title = { Text("Iniciar sesión") },
            )
        },
        body = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Mostrar un campo de texto para el correo electrónico
                TextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Mostrar un campo de texto para la contraseña
                TextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Mostrar un botón para iniciar sesión
                Button(
                    onClick = {
                        // Validar el correo electrónico y contraseña
                        if (validateCredentials(emailState.value, passwordState.value)) {
                            // Iniciar sesión y redirigir al usuario a la pantalla de inicio
                            navigateToHome()
                        } else {
                            // Mostrar un mensaje de error
                            Toast.makeText(context, "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar sesión")
                }

                // Mostrar un enlace para recuperar la contraseña
                TextButton(
                    onClick = onForgotPassword,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("¿Olvidaste tu contraseña?")
                }
            }
        }
    )
}

 */

@Composable
fun AppBar(title: () -> Unit) {

}

private fun validateCredentials(email: String, password: String): Boolean {
    // Implementar la lógica de validación de credenciales
    return true
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MarketplacePUJTheme {
        Greeting2("Android")
    }
}