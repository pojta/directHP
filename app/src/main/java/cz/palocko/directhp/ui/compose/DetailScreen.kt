package cz.palocko.directhp.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import cz.palocko.directhp.R
import cz.palocko.directhp.data.Character
import cz.palocko.directhp.data.Wand
import cz.palocko.directhp.exceptions.EntityNotFoundException
import cz.palocko.directhp.exceptions.ServerErrorException
import cz.palocko.directhp.ui.theme.GryffindorPrimary
import cz.palocko.directhp.ui.theme.GryffindorSecondary
import cz.palocko.directhp.ui.theme.HufflePuffPrimary
import cz.palocko.directhp.ui.theme.HufflePuffSecondary
import cz.palocko.directhp.ui.theme.RavenclawPrimary
import cz.palocko.directhp.ui.theme.RavenclawSecondary
import cz.palocko.directhp.ui.theme.SlytherinPrimary
import cz.palocko.directhp.ui.theme.SlytherinSecondary
import cz.palocko.directhp.viewmodels.DetailViewModel

@Composable
fun DetailScreen(id: String) {
    val viewModel = hiltViewModel<DetailViewModel>()

    viewModel.loadCharacter(id)

    val character = viewModel.getItemLive.observeAsState().value
    val error = viewModel.getErrorLive.observeAsState().value

    if (error != null) {
        val errorText = when (error) {
            is ServerErrorException -> stringResource(R.string.api_server_error)
            is EntityNotFoundException -> stringResource(R.string.api_character_not_found_error)
            else -> stringResource(R.string.unexpected_error)
        }

        Error(errorText) { viewModel.loadCharacter(id) }

        return
    }

    if (character != null) {
        Detail(character)
    }
}

@Composable
fun Detail(character: Character) {
    val status = when {
        character.hogwartsStaff -> stringResource(R.string.hogwarts_staff)
        character.hogwartsStudent -> stringResource(R.string.hogwarts_student, if (character.house != "") ": " + character.house else "")
        else -> ""
    }

    val houseColor = when(character.house) {
        "Gryffindor" -> GryffindorPrimary
        "Slytherin" -> SlytherinPrimary
        "Hufflepuff" -> HufflePuffPrimary
        "Ravenclaw" -> RavenclawPrimary
        else -> Color.DarkGray
    }

    val houseSecondaryColor = when(character.house) {
        "Gryffindor" -> GryffindorSecondary
        "Slytherin" -> SlytherinSecondary
        "Hufflepuff" -> HufflePuffSecondary
        "Ravenclaw" -> RavenclawSecondary
        else -> Color.White
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 48.dp)
                .fillMaxWidth()
                .height(160.dp)
                .background(houseColor)
        ) {
            val (image, name, type) = createRefs()

            Image(
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 40.dp)
                    .constrainAs(image) {
                        start.linkTo(parent.start, margin = 16.dp)
                        top.linkTo(parent.bottom)
                        bottom.linkTo(parent.bottom)
                    }
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .width(100.dp)
                    .height(100.dp),
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
                contentScale = ContentScale.FillBounds,
            )

            Text(
                character.name,
                modifier = Modifier.constrainAs(name) {
                    start.linkTo(image.end, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 12.dp)
                },
                fontSize = 22.sp,
                color = houseSecondaryColor,
            )

            Text(
                status,
                modifier = Modifier.constrainAs(type) {
                    start.linkTo(image.end, margin = 16.dp)
                    top.linkTo(parent.bottom, margin = 8.dp)
                },
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        InfoRow(title = stringResource(R.string.gender), value = character.gender)

        InfoRow(title = stringResource(R.string.wizard), value = if (character.wizard) "Yes" else "No")
        if (character.wizard) {
            InfoRow(title = stringResource(R.string.wand), value = character.wand.toString())

            if (character.patronus != "") {
                InfoRow(title = stringResource(R.string.patronus), value = character.patronus)
            }
        }

        InfoRow(title = stringResource(R.string.actor), value = character.actor)

        if (character.alternate_actors.isNotEmpty()) {
            InfoRow(
                title = stringResource(R.string.alternate_actors),
                value = character.alternate_actors.joinToString(", ")
            )
        }
    }
}

@Composable
fun InfoRow(title: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onTertiary,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary)
                .padding(8.dp)
        )
        Text(
            text = value.replaceFirstChar{ ch -> ch.uppercaseChar() },
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp, 8.dp, 20.dp)
        )
    }
}

@Preview
@Composable
fun DetailPreview() {
    Detail(
        character = Character(
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
        )
    )
}
