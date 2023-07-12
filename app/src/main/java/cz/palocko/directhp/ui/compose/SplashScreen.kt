package cz.palocko.directhp.ui.compose

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.palocko.directhp.R
import kotlinx.coroutines.delay
import androidx.compose.animation.core.Animatable

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        Modifier.fillMaxSize()
    ) {
        val scale = remember { Animatable(0.0f) }

        LaunchedEffect(key1 = true) {
            scale.animateTo(
                targetValue = 0.7f,
                animationSpec = tween(800, easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
            )
            delay(1000)
            navController.navigate("main") {
                popUpTo("splash") {
                    inclusive = true
                }
            }
        }

        Image(
            painter = painterResource(id = R.drawable.hp_logo),
            contentDescription = "Logo",
            alignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)
                .scale(scale.value)
        )
    }
}
