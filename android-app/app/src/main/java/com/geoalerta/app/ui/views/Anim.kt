package com.geoalerta.app.ui.views

import android.provider.Settings
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Acessibilidade: respeita "Remover animacoes" do sistema lendo a escala de
 * duracao do animador. Quando o usuario desliga animacoes, devolvemos true e os
 * helpers abaixo entregam o estado final instantaneamente (regra reduced-motion).
 */
@Composable
fun rememberReducedMotion(): Boolean {
    val context = LocalContext.current
    return remember {
        Settings.Global.getFloat(
            context.contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            1f
        ) == 0f
    }
}

/**
 * Clique com feedback de pressao: aplica um leve scale (0.96) via [graphicsLayer]
 * enquanto pressionado, com mola para sensacao natural. Nao altera bounds de
 * layout (sem jitter). Mantem o ripple padrao do Material.
 */
fun Modifier.pressClickable(
    enabled: Boolean = true,
    pressedScale: Float = 0.96f,
    onClick: () -> Unit
): Modifier = composed {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "pressScale"
    )
    this
        .graphicsLayer { scaleX = scale; scaleY = scale }
        .clickable(
            interactionSource = interaction,
            indication = LocalIndication.current,
            enabled = enabled,
            onClick = onClick
        )
}

/**
 * Entrada escalonada de itens de lista: fade + translacao vertical curta, com
 * atraso proporcional ao indice (stagger ~40ms). Usa apenas transform/opacity.
 */
@Composable
fun StaggeredItem(
    index: Int,
    content: @Composable () -> Unit
) {
    val reduced = rememberReducedMotion()
    var visible by remember { mutableStateOf(reduced) }
    LaunchedEffect(Unit) {
        if (!reduced) {
            delay(index * 40L)
            visible = true
        }
    }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "staggerAlpha"
    )
    val offset by animateFloatAsState(
        targetValue = if (visible) 0f else 20f,
        animationSpec = tween(durationMillis = 300),
        label = "staggerOffset"
    )
    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .alpha(alpha)
            .graphicsLayer { translationY = with(density) { offset.dp.toPx() } }
    ) {
        content()
    }
}
