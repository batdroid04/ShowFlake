package com.example.showflake.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.showflake.ApiConstants
import com.example.showflake.R
import com.example.showflake.db.Movie
import com.example.showflake.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import kotlin.coroutines.coroutineContext

@Composable
fun ProgramInfoScreen(navigation: NavController, viewModel: MainViewModel) {
    val scrollState = rememberScrollState()

    val favList by viewModel.favoriteList.collectAsState()
    LaunchedEffect(favList) { viewModel.getFavoriteMovies() }
    viewModel.selectedContent?.let {
        val similarShows by viewModel.similarShowList.collectAsState()
        var thumbIconLiked by remember {
            mutableStateOf(
                favList.firstOrNull { fav -> fav.id == it.id?.toLong() }?.isFavorite ?: false
            )
        }

        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color.White
                        )
                    )
                )
                .fillMaxSize()
                .scrollable(state = scrollState, orientation = Orientation.Vertical)

        ) {

            Column(Modifier.fillMaxSize()) {
                Image(
                    painter = rememberAsyncImagePainter(ApiConstants.getPosterPath(it.backdropPath.orEmpty())),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.White)
                )
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Text(
                        text = it?.title ?: it.originalTitle.orEmpty(),
                        Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.description),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = it.overview.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                HorizontalShowList(similarShows) {}
            }
            Icon(
                imageVector = if (thumbIconLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Search Icon",
                Modifier
                    .clickable {
                        thumbIconLiked = !thumbIconLiked
                        Log.d(">>>>detail", "ProgramInfoScreen: ${thumbIconLiked}")

                        val content = Movie(
                            it.id?.toLong() ?: 0L,
                            it.title.orEmpty(),
                            it.overview.orEmpty(),
                            it.posterPath.orEmpty(),
                            thumbIconLiked
                        )
                        viewModel.updateFavorite(content, thumbIconLiked)
                    }
                    .align(Alignment.TopEnd)
                    .padding(horizontal = 8.dp, 8.dp),
                tint = Color.Red

            )
        }
    }
}