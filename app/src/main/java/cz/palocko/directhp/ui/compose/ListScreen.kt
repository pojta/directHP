package cz.palocko.directhp.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import cz.palocko.directhp.R
import cz.palocko.directhp.data.Character
import cz.palocko.directhp.data.Wand
import cz.palocko.directhp.exceptions.ServerErrorException
import cz.palocko.directhp.ui.theme.DirectHPTheme
import cz.palocko.directhp.viewmodels.ListViewModel

@Composable
fun ListScreen(
    navController: NavHostController
) {
    val viewModel = hiltViewModel<ListViewModel>()
    val characters = viewModel.getItemsLive.observeAsState().value
    val error = viewModel.getErrorLive.observeAsState().value

    if (error != null) {
        val errorText = when (error) {
            is ServerErrorException -> stringResource(R.string.api_server_error)
            else -> stringResource(R.string.unexpected_error)
        }

        Error(errorText) { viewModel.loadCharacters() }

        return
    }

    CharactersList(
        characters = characters,
        onItemClick = { navController.navigate("detail/$it") }
    )
}

@Composable
fun CharactersList(
    characters: List<Character>?,
    onItemClick: (id: String) -> Unit
) {
    characters?.apply {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            itemsIndexed(characters) { _, item ->
                CharactersListItem(item) { onItemClick(item.id) }
            }
        }
    }
}

@Composable
fun CharactersListItem(character: Character, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .padding(0.dp, 0.dp, 8.dp, 0.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .size(48.dp),
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(character.image)
                    .crossfade(true)
                    .transformations(CircleCropTransformation())
                    .placeholder(R.drawable.hp_logo)
                    .fallback(R.drawable.hp_logo)
                    .error(R.drawable.hp_logo)
                    .build()
            ),
            contentDescription = character.name,
            contentScale = ContentScale.Crop,
        )
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            Text(
                character.name,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                character.actor,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .75f),
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun CharactersListItemPreview() {

    DirectHPTheme {
        CharactersListItem(Character(
            "9e3f7ce4-b9a7-4244-b709-dae5c1f1d4a8",
            "Harry Potter",
            arrayListOf(),
            "human",
            "male",
            "Gryffindor",
            "31-07-1980",
            1980,
            true,
            "half-blood",
            "green",
            "black",
            Wand("holly", "phoenix feather", 11),
            "stag",
            true,
            false,
            "Daniel Radcliffe",
            arrayListOf(),
            true,
            "https://ik.imagekit.io/hpapi/harry.jpg"
        ), {})
    }
}
