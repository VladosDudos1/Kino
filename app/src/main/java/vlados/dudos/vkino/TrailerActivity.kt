package vlados.dudos.vkino

import android.os.Bundle
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_trailer.*


class TrailerActivity : YouTubeBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trailer)

        arrow_trailer.setOnClickListener {
            super.onBackPressed()
        }
        youtube_view.initialize(
            Case.googleKey,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    youTubePlayer: YouTubePlayer,
                    b: Boolean
                ) {
                    youTubePlayer.loadVideo(Case.key)
                    youTubePlayer.setFullscreen(true)

                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    youTubeInitializationResult: YouTubeInitializationResult
                ) {
                }
            })
    }
}