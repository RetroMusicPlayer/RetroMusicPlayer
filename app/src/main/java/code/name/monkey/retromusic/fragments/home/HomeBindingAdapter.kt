package code.name.monkey.retromusic.fragments.home

import code.name.monkey.retromusic.databinding.FragmentBannerHomeBinding
import code.name.monkey.retromusic.databinding.FragmentHomeBinding

class HomeBindingAdapter(
    homeBinding: FragmentHomeBinding?,
    bannerHomeBinding: FragmentBannerHomeBinding?
) {
    val root = homeBinding?.root ?: bannerHomeBinding?.root!!
    val container = homeBinding?.container ?: bannerHomeBinding?.container!!
    val contentContainer = homeBinding?.contentContainer ?: bannerHomeBinding?.contentContainer!!
    val appBarLayout = homeBinding?.appBarLayout ?: bannerHomeBinding?.appBarLayout!!
    val toolbar = homeBinding?.toolbar
        ?: bannerHomeBinding?.toolbar!!
    val bannerImage = bannerHomeBinding?.bannerImage
    val userImage = homeBinding?.userImage
        ?: bannerHomeBinding?.userImage!!
    val lastAdded = homeBinding?.homeContent?.absPlaylists?.lastAdded
        ?: bannerHomeBinding?.homeContent?.absPlaylists?.lastAdded!!
    val topPlayed = homeBinding?.homeContent?.absPlaylists?.topPlayed
        ?: bannerHomeBinding?.homeContent?.absPlaylists?.topPlayed!!
    val actionShuffle = homeBinding?.homeContent?.absPlaylists?.actionShuffle
        ?: bannerHomeBinding?.homeContent?.absPlaylists?.actionShuffle!!
    val history = homeBinding?.homeContent?.absPlaylists?.history
        ?: bannerHomeBinding?.homeContent?.absPlaylists?.history!!
    val recyclerView = homeBinding?.homeContent?.recyclerView
        ?: bannerHomeBinding?.homeContent?.recyclerView!!
    val titleWelcome = homeBinding?.titleWelcome ?: bannerHomeBinding?.titleWelcome!!
    val appNameText = homeBinding?.appNameText ?: bannerHomeBinding?.appNameText!!
}