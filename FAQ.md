#### Q: How do I get the beta version of Retro Music?
You can opt-in for the beta build by clicking on this link: https://play.google.com/apps/testing/code.name.monkey.retromusic


#### Q: How to restore my purchases?
Make sure to switch and use account in the Play Store app through which you purchased before installing Retro music. The account used to install the app is also used to purchase/restore the pro license.

If you already installed, remove all other accounts except the one with which you purchased premium. Then restore the purchase.


#### Q: How do I use offline synced lyrics?
There are two methods for how to get offline synced lyrics.

#### Method 1:-
##### STEP 1: 
Find the time stamped lyrics for your songs which don't have lyrics already. A time stamped lyrics looks like this, "[00:04:02] Some lyrics text" for example.
##### STEP 2: 
Copy these time stamped lyrics.
##### STEP 3: 
Open retro music and head to the song synced lyrics editor.
##### STEP 4: 
Paste the lyrics there normally and exit the editor
##### STEP 5: 
Open lyrics and you should see your time stamped lyrics there.

#### Method 2:-
##### STEP 1: 
Find the ".lrc" files for your songs which doesn't have lyrics already.
##### STEP 2: 
A ".lrc" is like a text file which contains the time stamped lyrics for example, "[00:04:02] Some lyrics text"
##### STEP 3: 
Now you have to rename the file you created in this way: <song_name> - <artist_name>.lrc or for better matching copy the <song_name> and the <artist_name> from the tag editor and then rename the file.
##### STEP 4: 
Now paste the LRC files to the following path : /sdcard/Retromusic/lyrics/
Here sdcard is your internal storage.

> If you want to skip to particular time stamp, simply scroll to the time stamp from where you want to start from and a 'Play' icon will appear left to the particular stamp. Tap on play button to play from there.


#### Q: Why isn't the artist's image downloading?
Last.fm has disabled the download of artist's images for the time being, whether functionality for this will be restored in future is uncertain. So we have moved to deezer API for artist images which have very less artists in their database and you might not able to see covers on every artist profiles.


#### Q: How do I change the theme?
Settings -> Look and feel -> Select your theme.


#### Q: Equailizer is very laggy and unstable or I am getting "No equalizer found" error. why?
The Retro music in-built equalizer was removed updates ago so the only equalizer you will have by your OEM or android which aren't made by us and have no control over them. So you can report those issues to your OEM so that they can provide a fix in next updates.

If you are seeing "No Equalizer Found" in your device, this means your device doesn't have stock equalizer MusicFx Equalizer. You can try using this one. Its made by AEX ROM developers.

https://drive.google.com/file/d/1_1bpsn6roeEyElGKikbU39lVKUH8O3xp/view?usp=drivesdk


#### Q: Why aren't last added songs showing?
Settings -> Other -> Last added playlist interval -> Select an option from the list.


#### Q: How do I enable fullscreen lockscreen controls?
Settings -> Personalize -> Fullscreen controls -> Enable (this will only be visible when songs are playing from Retro Music).


#### Q: Why are gallery or random pictures showing up as album art?
Settings -> Images -> Ignore media store covers -> Enable


#### Q: Which file types are supported?
Retro Music uses the native media player that comes with your Android phone, so as long as a file type is supported by your phone, it's supported by Retro Music.


#### Q: Why is my device slowing down when I'm using the app?
Retro Music is image intensive, it keeps images in the cache for quick loading.


#### Q: The title "Retro Music" is showing on the top of the app, how can i fix this? 
Clear the app's cache and data.


#### Q: My app is crashing, how do i fix this? (Sorry, settings have changed internally) 
Please try clear data of the app. If it doesn't work, reinstalling fresh from play store should help.


#### Q: Why has all the text gone white/dissapeared? 
Change the theme to Black or Dark and change it back to what you had before.


#### Q: Why some of my songs are not showing in my library?
Try checking up if those songs are not less than 30 seconds, if so head to settings -> other -> filter song duration. Put this to zero and see the songs should start appearimg in the library.

If this doesn't work out for you, re-scanning the media folder should help and subsquently rebooting the device to refresh media store.

At last resort, If nothing worked and your audio files are stored in SD card. Try moving them to internal memory then back to SD card.


#### Q: Why some of my songs are not showing in my library?
1. Try checking up if those songs are not less than 30 seconds, if so head to settings -> other -> filter song duration. Put this to zero and see the songs should start appearimg in the library.

2. If this doesn't work out for you, re-scanning the media folder should help and subsquently rebooting the device to refresh media store.

3. At last resort, If nothing worked and your audio files are stored in SD card. Try moving them to internal memory then back to SD card.


#### Q: Why my playlist/playlist songs keep disappearing?
Playlist/Playlist songs disappearing is based on android media store system. Save those playlist as file(Tap on three dot menu next to available playlist and save as file) and it should get fixed.


#### Q: Why does my library shows song files twice or no song at all?
If you are seeing duplication of songs in the library or no songs at all, then it's because of Media Store issue which got affected by some other app. 

To fix this:

• Head to your device settings

• Open up "Apps & notifications" (This name depends from ROM to ROM)

• Find 'Media storage' app and clear storage (both data and cache) of it.

• Then open Retro Music app and manually scan your music from your storage. 

• Reboot the device to refresh media store (Not sure if this is necessary)

NOTE: Don't panic when you will open Retro Music and see "Zero" songs there in the library. It's because you cleared Media Store which is responsible for recognising files on your device.


#### Q: I can't find folder menu anymore after latest update?
Head to settings -> personalise. And select folders from "library categories". If there is option of folders, tap on reset and select folders.


#### Q: After updating the app to latest version, font got removed. Why?
Retro Music's font have now been replaced with system font now, which means the default font your system uses will be used by Retro too. It fixes all font related issues you used to face/are facing in the app. 

If you think the font looks ugly, then you just need to change the default font from your Android settings (or use any Magisk module). If you can't, there's nothing we can do about it.


#### Q: How to export playlist:
In your built-in music player, there should be an option to save those playlist as file. Save them and import from file manager by opening it into retro music.

Note that those playlist must be of your offline music only since retro music is offline music player not an online music player. So if your playlist are of online music, it can't be opened on other offline players nor can be exported


#### Q: How to restore/import playlist:
Retro Music will automatically detect any playlist file when that playlist file is stored in InternalStorage/Playlist. However, if it doesn't, just open "file manager" and open that playlist file with Retro Music.

For restoring playlists, the location of songs must be same in both Playlist file and in your storage. For example, your music is in "Internalstorage/Music" and playlist file has songs location "Internalstorage/Songs". Then it will not going to work since both these location are different.


#### Q: Adding songs to playlist or marking them as favourite are making app crash. Why?
It's a known issue with only android 10 with its media store API when songs are in SD card due to introduction of Scoped Storage by Google. The issue have been created on Google Issue Tracker by many users. Many other players which doesn't have this issue are using a custom database for storing playlist. We will soon be implementing a custom database for playlist to fix this issue!

Workaround: You can move all songs to internal storage to fix the issue. 

ISSUE link: https://issuetracker.google.com/issues/147619577
