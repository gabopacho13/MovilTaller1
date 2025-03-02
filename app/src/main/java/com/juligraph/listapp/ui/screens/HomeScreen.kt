package com.juligraph.listapp.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.juligraph.listapp.Routes
import com.juligraph.listapp.data.User
import com.juligraph.listapp.network.KtorClient
import com.juligraph.listapp.ui.components.Loader
import com.juligraph.listapp.ui.theme.AppTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    apiClient: KtorClient = KtorClient()
) {
    val context = LocalContext.current
    var users by remember { mutableStateOf(listOf<User>()) }

    LaunchedEffect(key1 = null) {
        users = apiClient.getUsers()
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        stickyHeader {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Users: ${users.size}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        if (users.isEmpty()) {
            item {
                Loader(modifier = Modifier.fillParentMaxSize())
            }
        } else {
            items(users) { usr ->
                ListItem(
                    headlineContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){Text(
                            "${usr.firstName} ${usr.lastName} - (${usr.gender})",
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${usr.phone}")
                                }
                                context.startActivity(intent)
                            }) {
                                Icon(Icons.Filled.Phone, contentDescription = "Call")
                                }
                        }
                    },
                    supportingContent = {
                        Text(
                            usr.company.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingContent = {
                        AsyncImage(
                            model = usr.image,
                            contentDescription = null,
                            modifier = Modifier.clip(CircleShape)
                        )
                    },
                    trailingContent = {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Tap on ${usr.firstName}", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.UserDetail(usr.id))
                    },
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        HomeScreen(navController = rememberNavController())
    }
}