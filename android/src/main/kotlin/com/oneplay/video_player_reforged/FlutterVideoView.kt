package com.oneplay.video_player_reforged

import android.content.Context
import android.net.Uri
import android.view.SurfaceView
import android.view.View
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
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
    private val videoView: SurfaceView
    private val methodChannel: MethodChannel
    private var player: ExoPlayer? = null
    private val _context: Context = context

    override fun getView(): View {
        return videoView
    }

    init {
        methodChannel = MethodChannel(messenger, "video_player_view/flutter_video_view_$id")
        // Init methodCall Listener
        methodChannel.setMethodCallHandler(this)
        player = ExoPlayer.Builder(context).build()
        videoView = SurfaceView(context)
        player?.setVideoSurfaceView(videoView)
    }

    override fun onMethodCall(methodCall: MethodCall, result: MethodChannel.Result) {
        when (methodCall.method) {
            "setup" -> setup(methodCall, result)
            "play" -> play(result)
            "pause" -> pause(result)
            else -> result.notImplemented()
        }
    }

    // set and load new Url
    private fun setup(methodCall: MethodCall, result: MethodChannel.Result ) {

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

    private fun play(result: MethodChannel.Result ) {
        player?.play()
        result.success(null)
    }

    private fun pause(result: MethodChannel.Result ) {
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

                return ProgressiveMediaSource.Factory(
                    SrtLiveStreamDataSourceFactory(
                        url,
                        port.toInt(),
                    ),
                ).createMediaSource(MediaItem.fromUri(Uri.EMPTY))
            }
            "RTMP" -> {
                return ProgressiveMediaSource.Factory(RtmpDataSource.Factory())
                    .createMediaSource(MediaItem.fromUri(Uri.parse(uri)))
            }
            else -> {
                throw Exception("FORMAT NOT SUPPORTED")
            }
        }

    }

}