// Copyright © FunctionalHub.com 2018. All rights reserved.

package com.functionalkotlin.bandhookkotlin.data.mapper.album

import com.functionalkotlin.bandhookkotlin.data.lastfm.model.LastFmAlbum
import com.functionalkotlin.bandhookkotlin.data.lastfm.model.LastFmAlbumDetail
import com.functionalkotlin.bandhookkotlin.data.lastfm.model.LastFmArtist
import com.functionalkotlin.bandhookkotlin.data.lastfm.model.LastFmImage
import com.functionalkotlin.bandhookkotlin.data.lastfm.model.LastFmTrack
import com.functionalkotlin.bandhookkotlin.data.mapper.artist.transform
import com.functionalkotlin.bandhookkotlin.data.mapper.image.getMainImageUrl
import com.functionalkotlin.bandhookkotlin.data.mapper.track.transform
import com.functionalkotlin.bandhookkotlin.domain.entity.Album
import com.functionalkotlin.bandhookkotlin.domain.entity.Artist
import com.functionalkotlin.bandhookkotlin.domain.entity.Track

fun transform(albums: List<LastFmAlbum>): List<Album> =
    albums
        .filter { it.hasQualityInfo() }
        .mapNotNull { transform(it) }

fun transform(
    album: LastFmAlbumDetail, imageMapper: ((List<LastFmImage>?) -> String?) = ::getMainImageUrl,
    trackMapper: ((List<LastFmTrack>?) -> List<Track>) = { transform(it) }) = album.mbid?.let {
    Album(
        it, album.name, Artist("", album.artist), imageMapper(album.images),
        trackMapper(album.tracks.tracks))
}

fun transform(
    album: LastFmAlbum, artistMapper: ((LastFmArtist) -> Artist?) = { transform(it) },
    imageMapper: ((List<LastFmImage>?) -> String?) = ::getMainImageUrl,
    trackMapper: ((List<LastFmTrack>?) -> List<Track>) = { transform(it) }) =
    album.mbid?.let {
        Album(
            it, album.name, artistMapper(album.artist), imageMapper(album.images),
            trackMapper(album.tracks?.tracks))
    }

private fun LastFmAlbum.hasQualityInfo(): Boolean = !hasEmptyMbid() && images.isNotEmpty()

private fun LastFmAlbum.hasEmptyMbid(): Boolean = mbid.isNullOrEmpty()
