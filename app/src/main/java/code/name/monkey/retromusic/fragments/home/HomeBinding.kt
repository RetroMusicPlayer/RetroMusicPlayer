package code.name.monkey.retromusic.fragments.home

import code.name.monkey.retromusic.databinding.FragmentHomeBinding

class HomeBinding(
    homeBinding: FragmentHomeBinding
) {
    val root = homeBinding.root
    val container = homeBinding.container
    val contentContainer = homeBinding.contentContainer
    val appBarLayout = homeBinding.appBarLayout
    val toolbar = homeBinding.appBarLayout.toolbar
    val bannerImage = homeBinding.imageLayout.bannerImage
    val userImage = homeBinding.imageLayout.userImage
    val lastAdded = homeBinding.absPlaylists.lastAdded
    val topPlayed = homeBinding.absPlaylists.topPlayed
    val actionShuffle = homeBinding.absPlaylists.actionShuffle
    val history = homeBinding.absPlaylists.history
    val recyclerView = homeBinding.homeContent.recyclerView
    val titleWelcome = homeBinding.imageLayout.titleWelcome
    val suggestions = homeBinding.homeContent.suggestions
    val coverHeader = homeBinding.coverHeader
    val coverGrid = homeBinding.coverGrid
    val coverLogo = homeBinding.coverLogo
    val coverOverlay = homeBinding.coverOverlay
    val playlistsHolder = homeBinding.playlistsHolder
    val absPlaylistsView = homeBinding.absPlaylists.root
}