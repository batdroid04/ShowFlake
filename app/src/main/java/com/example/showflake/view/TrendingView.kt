@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.showflake.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.showflake.ApiConstants
import com.example.showflake.R
import com.example.showflake.tmdb.model.Program
import com.example.showflake.ui.ShowFlakeTheme
import com.example.showflake.viewmodel.MainViewModel
import com.google.accompanist.pager.*


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalPagerApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)

@Composable
fun TrendingMoviesScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()

    val trendingMovies by viewModel.items.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
            )
        },
        content = {
            TrendingMoviesGrid(navController, trendingMovies.results.toList(), viewModel)
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrendingMoviesGrid(
    navigation: NavController,
    trendingMovies: List<Program>,
    viewModel: MainViewModel
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(trendingMovies) { movie ->
            TrendingMovieItem(navigation, movie = movie, viewModel)
        }
    }
}

@Composable
fun TrendingMovieItem(navigation: NavController, movie: Program, viewModel: MainViewModel) {
    val favList by viewModel.favoriteList.collectAsState()

    LaunchedEffect(favList) {
        viewModel.getFavoriteMovies()
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .size(260.dp)
            .fillMaxSize()
            .clickable { /* Handle item click */
                navigation.navigate("SecondScreen")
                viewModel.selectedContent = movie
                viewModel.getSimilarShows(movie.id ?: 0)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.inversePrimary
                        )
                    )
                )
                .fillMaxSize()
        ) {
            Column {
                Image(
                    painter = rememberAsyncImagePainter(ApiConstants.getPosterPath(movie.posterPath.orEmpty())),
                    contentDescription = null,
                    contentScale = ContentScale.None,
                    modifier = Modifier
                        .size(220.dp)
                        .background(Color.White)
                )
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Row {
                        Text(
                            text = movie?.title ?: movie.originalTitle ?: "",
                            Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            maxLines = 2,
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            val showLike =
                if (favList.firstOrNull { fav -> fav.id == movie.id?.toLong() } != null) 1f else 0f

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Search Icon",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .alpha(showLike)
                    .size(24.dp)
                    .clip(CircleShape),
                tint = Color.Red
            )
        }
    }
}

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun SearchAppBar(onSearchQueryChanged: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchQuery by remember { mutableStateOf("") }

    TopAppBar(
        title = {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    onSearchQueryChanged(it)
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                searchQuery = ""
                                onSearchQueryChanged("")
                                focusManager.clearFocus()
                                keyboardController?.hide()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Search Icon"
                            )
                        }
                    }
                },
                placeholder = { Text(text = stringResource(id = R.string.search_hint)) },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
        },
    )
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun ErrorView(errorMessage: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(navigation: NavController, viewModel: MainViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    ShowFlakeTheme {
        val trendingMovies by viewModel.items.collectAsState()
        Column {
            SearchAppBar {
                searchQuery = it
                if (searchQuery.isNotEmpty())
                    viewModel.searchContent(searchQuery) else viewModel.getTrending()
            }
            if (trendingMovies.results.isNullOrEmpty())
                ErrorView(stringResource(R.string.something_went_wrong)) {
                    viewModel.getTrending()
                } else TrendingMoviesGrid(navigation, trendingMovies.results, viewModel)
        }
    }
}
