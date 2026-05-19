import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.example.sicenetmultiplatform.data.DefaultAppContainer
import com.example.sicenetmultiplatform.ui.MarsPhotosApp
import com.example.sicenetmultiplatform.ui.SicenetViewModel
import com.example.sicenetmultiplatform.ui.theme.MarsPhotosTheme

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val container = DefaultAppContainer()
    val viewModel = SicenetViewModel(container.sicenetRepository, container.sicenetLocalRepository)

    ComposeViewport(viewportContainerId = "ComposeTarget") {
        MarsPhotosTheme {
            MarsPhotosApp(viewModel)
        }
    }
}
