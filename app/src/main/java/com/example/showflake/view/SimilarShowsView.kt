package com.example.showflake.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.showflake.ApiConstants
import com.example.showflake.R
import com.example.showflake.tmdb.model.Program
import com.example.showflake.tmdb.model.ProgramInfoList

@Composable
fun HorizontalShowList(
    similarShows: ProgramInfoList, onShowClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(Color.White)
    ) {

        if (!similarShows.results.isNullOrEmpty()) Text(text = stringResource(R.string.similar_shows_title))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(similarShows.results) { show ->
                ShowItem(show = show, onShowClicked = onShowClicked)
            }
        }
    }
}

@Composable
fun ShowItem(show: Program, onShowClicked: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color.White)
            .clickable {
//                onShowClicked(show.id)
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(ApiConstants.getPosterPath(show.posterPath.orEmpty())),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = show.title.orEmpty(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2
            )
        }
    }
}
