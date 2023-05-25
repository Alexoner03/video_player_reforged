package com.oneplay.video_player_reforged

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerControlView
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.oneplay.video_player_reforged.srt.SrtLiveStreamDataSourceFactory
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.platform.PlatformView

class FlutterVideoView internal constructor(
    context: Context,
    messenger: BinaryMessenger,
    id: Int
) :
    PlatformView, MethodCallHandler {
    private val videoView: StyledPlayerView
    private val methodChannel: MethodChannel
    private var player: ExoPlayer? = null
    private val isPlaying get() = player?.isPlaying ?: false
    private val _context: Context = context

    override fun getView(): View {
        return videoView
    }

    init {
        methodChannel = MethodChannel(messenger, "video_player_view/flutter_video_view_$id")
        // Init methodCall Listener
        methodChannel.setMethodCallHandler(this)

        player = ExoPlayer.Builder(context).build()
        videoView = StyledPlayerView(context)
        videoView.player = player
    }

    override fun onMethodCall(methodCall: MethodCall, result: MethodChannel.Result) {
        when (methodCall.method) {
            "setUrl" -> setText(methodCall, result)
            "play" -> play(methodCall, result)
            "pause" -> pause(methodCall, result)
            else -> result.notImplemented()
        }
    }

    // set and load new Url
    private fun setText(methodCall: MethodCall, result: MethodChannel.Result ) {
        val url = methodCall.arguments as String
        val (value, type) = url.split("|||")
        val mediaSource = buildMediaSource(value, type)
        // Finally assign this media source to the player
        player?.apply {
            setMediaSource(mediaSource)
            playWhenReady = true // start playing when the exoplayer has setup
            seekTo(0, 0L) // Start from the beginning
            prepare() // Change the state from idle.
        }

        result.success(null)
    }

    private fun play(methodCall: MethodCall, result: MethodChannel.Result ) {
        player?.play()
        result.success(null)
    }

    private fun pause(methodCall: MethodCall, result: MethodChannel.Result ) {
        player?.pause()
        result.success(null)
    }

    // Destroy WebView when PlatformView is destroyed
    override fun dispose() {
        player?.release()
    }

    private fun buildMediaSource(uri: String, type: String): MediaSource {

        when (type) {
            "MP4" -> {
                // create a media item.
                val mediaItem = MediaItem.Builder()
                    .setUri(uri)
                    .setMimeType(MimeTypes.APPLICATION_MP4)
                    .build()

                // Create a media source and pass the media item
                return ProgressiveMediaSource.Factory(
                    DefaultDataSource.Factory(_context) // <- context
                )
                    .createMediaSource(mediaItem)
            }
            "M3U8" -> {
                // create a media item.
                val mediaItem = MediaItem.Builder()
                    .setUri(uri)
                    .setMimeType(MimeTypes.APPLICATION_M3U8)
                    .build()

                // Create a media source and pass the media item
                return HlsMediaSource.Factory(
                    DefaultDataSource.Factory(_context) // <- context
                )
                    .createMediaSource(mediaItem)
            }
            "SRT" -> {
                val replaced = uri.substring(6)
                val (url, port) = replaced.split(":")

                Log.d("debug","*****************************************")
                Log.d("debug",url)
                Log.d("debug",replaced)
                Log.d("debug",port)
                Log.d("debug","*****************************************")

                return ProgressiveMediaSource.Factory(
                    SrtLiveStreamDataSourceFactory(
                        url,
                        port.toInt(),
                    ),
                ).createMediaSource(MediaItem.fromUri(Uri.EMPTY))
            }
            else -> {
                val mediaItem = MediaItem.Builder()
                    .setUri(uri)
                    .setMimeType(MimeTypes.APPLICATION_MP4)
                    .build()

                // Create a media source and pass the media item
                return ProgressiveMediaSource.Factory(
                    DefaultDataSource.Factory(_context) // <- context
                )
                    .createMediaSource(mediaItem)
            }
        }

    }

}