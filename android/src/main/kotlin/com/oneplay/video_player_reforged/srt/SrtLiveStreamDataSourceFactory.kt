package com.oneplay.video_player_reforged.srt

import com.google.android.exoplayer2.upstream.DataSource

class SrtLiveStreamDataSourceFactory(
    private val srtUrl: String,
    private val port: Int,
    private val passPhrase: String? = null
) :
    DataSource.Factory {
    override fun createDataSource(): DataSource {
        return SrtLiveStreamDataSource(srtUrl, port, passPhrase)
    }
}