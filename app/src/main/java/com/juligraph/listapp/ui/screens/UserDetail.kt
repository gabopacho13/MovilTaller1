package com.juligraph.listapp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.juligraph.listapp.data.User
import com.juligraph.listapp.network.KtorClient
import com.juligraph.listapp.ui.components.Loader
import kotlinx.coroutines.delay

@Composable
fun UserDetail(id: Int, apiClient: KtorClient = KtorClient()) {
    val context = LocalContext.current
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(key1 = id) {
        user = apiClient.getUser(id)
    }

    if (user == null) {
        Loader(modifier = Modifier.fillMaxSize())
    } else {
        val infiniteTransition = rememberInfiniteTransition()
        val gradientOffset by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 2000f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing), // Aumenta la velocidad de la animación
                repeatMode = RepeatMode.Reverse
            )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFF06292), Color(0xFFCE93D8)), // Rosa más fuerte y morado
                        start = Offset(gradientOffset, 0f),
                        end = Offset(gradientOffset + 500f, 1000f)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = user!!.image,
                        contentDescription = "User picture",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .shadow(24.dp, CircleShape) // Más sombra
                            .border(
                                width = 6.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFFE91E63), Color(0xFFAB47BC)), // Borde más vibrante
                                    start = Offset(gradientOffset, 0f),
                                    end = Offset(500f, gradientOffset + 100f)
                                ),
                                shape = CircleShape
                            )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                val gradientBrush = Brush.linearGradient(
                    colors = listOf(Color(0xFFE91E63), Color(0xFFAB47BC)),
                    start = Offset(0f, gradientOffset),
                    end = Offset(500f, gradientOffset + 100f),
                    tileMode = TileMode.Repeated
                )

                Text(
                    text = "${user!!.firstName} ${user!!.lastName}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.background(brush = gradientBrush)
                )
                Spacer(modifier = Modifier.height(4.dp))

                Button(onClick = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:${user!!.phone}")
                    }
                    context.startActivity(intent)
                }) {
                    Icon(Icons.Filled.Phone, contentDescription = "Call")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call")
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text("birth date: ${user!!.birthDate}", color = Color(0xFFD81B60))
                Text("Age: ${user!!.age}", color = Color(0xFFD81B60))
                Text("Email: ${user!!.email}", color = Color(0xFFD81B60))
                Text("Blood Group: ${user!!.bloodGroup}", color = Color(0xFFD81B60))
                Text("Height: ${user!!.height} cm", color = Color(0xFFD81B60))
                Text("Weight: ${user!!.weight} kg", color = Color(0xFFD81B60))
                Text("Eye Color: ${user!!.eyeColor}", color = Color(0xFFD81B60))
                Text("Hair Color: ${user!!.hair.color}", color = Color(0xFFD81B60))
                Text("Address: ${user!!.address.address}, ${user!!.address.city}, ${user!!.address.country}", color = Color(0xFFD81B60))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Company: ${user!!.company.name}", fontWeight = FontWeight.Bold, color = Color(0xFF8E24AA))
            }
        }
    }
}