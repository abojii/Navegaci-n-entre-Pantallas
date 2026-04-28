package com.example.navegacioncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data class Tarea(
    val titulo: String,
    val descripcion: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskFlowApp()
        }
    }
}

@Composable
fun TaskFlowApp() {
    val navController = rememberNavController()
    val tareas = remember { mutableStateListOf<Tarea>() }
    var nombreUsuario by remember { mutableStateOf("") }

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = "login"
        ) {
            composable("login") {
                PantallaLogin(
                    nombreUsuario = nombreUsuario,
                    onNombreChange = { nombreUsuario = it },
                    iniciarSesion = {
                        if (nombreUsuario.isNotBlank()) {
                            navController.navigate("crear_tarea")
                        }
                    }
                )
            }

            composable("crear_tarea") {
                PantallaCrearTarea(
                    nombreUsuario = nombreUsuario,
                    agregarTarea = { nuevaTarea ->
                        tareas.add(nuevaTarea)
                    },
                    irALista = {
                        navController.navigate("lista_tareas")
                    }
                )
            }

            composable("lista_tareas") {
                PantallaListaTareas(
                    nombreUsuario = nombreUsuario,
                    tareas = tareas,
                    volverAgregar = {
                        navController.navigate("crear_tarea")
                    },
                    cerrarSesion = {
                        nombreUsuario = ""
                        tareas.clear()
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PantallaLogin(
    nombreUsuario: String,
    onNombreChange: (String) -> Unit,
    iniciarSesion: () -> Unit
) {
    FondoApp {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TaskFlow",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F46E5)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Organizá tus tareas universitarias",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(28.dp))

                OutlinedTextField(
                    value = nombreUsuario,
                    onValueChange = onNombreChange,
                    label = { Text("Nombre de usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = iniciarSesion,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4F46E5)
                    )
                ) {
                    Text("Iniciar sesión")
                }
            }
        }
    }
}

@Composable
fun PantallaCrearTarea(
    nombreUsuario: String,
    agregarTarea: (Tarea) -> Unit,
    irALista: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    FondoApp {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Hola, $nombreUsuario 👋",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Agregá una nueva tarea",
                color = Color.White.copy(alpha = 0.85f)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text("Título de la tarea") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (titulo.isNotBlank()) {
                                agregarTarea(
                                    Tarea(
                                        titulo = titulo,
                                        descripcion = descripcion.ifBlank { "Sin descripción" }
                                    )
                                )
                                titulo = ""
                                descripcion = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981)
                        )
                    ) {
                        Text("Agregar tarea")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = irALista,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver tareas pendientes")
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaListaTareas(
    nombreUsuario: String,
    tareas: List<Tarea>,
    volverAgregar: () -> Unit,
    cerrarSesion: () -> Unit
) {
    FondoApp {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tareas de $nombreUsuario",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Pendientes: ${tareas.size}",
                color = Color.White.copy(alpha = 0.85f)
            )

            if (tareas.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        text = "No hay tareas todavía. Agregá una nueva tarea.",
                        modifier = Modifier.padding(24.dp),
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(tareas) { tarea ->
                        CardTarea(tarea)
                    }
                }
            }

            Button(
                onClick = volverAgregar,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981)
                )
            ) {
                Text("Agregar otra tarea")
            }

            OutlinedButton(
                onClick = cerrarSesion,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
fun CardTarea(tarea: Tarea) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(Color(0xFF4F46E5), CircleShape)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = tarea.titulo,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = tarea.descripcion,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun FondoApp(contenido: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4F46E5),
                        Color(0xFF7C3AED),
                        Color(0xFFEC4899)
                    )
                )
            ),
        contentAlignment = Alignment.Center,
        content = contenido
    )
}