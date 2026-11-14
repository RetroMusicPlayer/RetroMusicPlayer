# R Music handoff (read this first)

User (GitHub: ELRFAAY) writes Egyptian Arabic; reply in Egyptian Arabic, short, mobile-friendly.
App: "R Music", a fork of Retro Music (Kotlin, XML views, Room, Koin, jaudiotagger). Base repo: RetroMusicPlayer/RetroMusicPlayer. Fork: ELRFAAY/RetroMusicPlayer (default branch dev).
Work lives on branch `claude/backup-listening-data` (includes `claude/arabic-complete`). Code was NOT compiled locally (no Android SDK in the sandbox); the CI workflow `.github/workflows/build-branch.yml` builds a debug APK on every push to `claude/**`. The user must enable Actions in the fork first.

## Done
- Arabic strings completed (values-ar-rSA).
- Backup/restore of listening history + play counts (BackupHelper, BackupContent.LISTENING_DATA, checkbox in activity_restore.xml). Matching on restore is by title/artist/album/duration.
- Step 1: CI build of the backup branch passed (2026-11-14).
- Step 2 (written 2026-11-14, not yet confirmed by CI): listening events.
  - Room: `ListeningEventEntity` / `ListeningEventDao` (db/), DB version 25, `MIGRATION_24_25` in RoomMigrations.kt, DAO registered in MainModule.
  - Logging: `service/ListeningTracker` measures real play time per song (sessions under 5 s are skipped); `MusicService` closes a session on META_CHANGED and on onDestroy, inserts off the main thread.
  - Stats page: `fragments/stats/` (fragment, ViewModel, adapter), layouts `fragment_listening_stats` / `item_listening_event`, destination `listening_stats_fragment` in main_graph.xml. Temporary entry point: Home toolbar overflow menu ("Listening stats"); step 3 moves it to tapping the logo.
  - Backup: listening.json is now version 2 with an `events` array; restore skips duplicates (same played_at + title + artist) and still imports events for songs missing from the library.
  - Known limit: if the app is killed while a song is playing/paused, that last session is lost (flush only happens on song change and service destroy).

- Step 3 (written 2026-11-14, not yet confirmed by CI): home cover wall.
  - `views/TiltedCoverGridView` draws a tilted, darkened grid of album covers (rows alternate direction, seamless scroll, stops when hidden / speed 0 / system animations off). Covers loaded with Glide as small bitmaps.
  - `fragment_home.xml` (+ `layout-land`): `coverHeader` FrameLayout under the user image, holds the grid and `coverLogo` (launcher foreground). Tapping the logo opens `listening_stats_fragment`. The Home overflow "Listening stats" entry now only shows when the wall is turned off.
  - `LibraryViewModel.loadHomeCovers(source, count)`: one song per album from history (0) or top played (1); falls back to a random library pick when empty.
  - Settings in pref_ui.xml (Home category): switch + source / speed / angle / darkness / count as list prefs. Keys in Constants.kt (`HOME_COVERS_*`), accessors in PreferenceUtil.
  - Strings in values and values-ar-rSA.
  - Revision (user feedback, 2026-11-14): the wall is now full screen (height = visible area, set in `HomeFragment.sizeCoverHeader()`), sits at the very top; the user image and lists start below it and appear when scrolling up. Tapping any cover plays that song right away (`MusicPlayerRemote.openQueue` with the wall's songs, starting at the tapped one; hit-testing in `TiltedCoverGridView.hitTest`). Tiles are sized from the short side of the view; default 24 covers (12/24/36/48). Fade to background over the last 30%. Not done: wall behind the toolbar/status bar (toolbar still sits above it).

## Next, in order
4. Merge artists (dropdown of existing artists) and album rename that regroups songs, written into the MP3 tags (existing tag editor in activities/tageditor).
5. Synced lyrics from LRCLIB (no key, no limit); LrcView already exists; tap a line to seek.
6. Artist info button (MusicBrainz + TheAudioDB + Wikipedia, cached for offline).
7. English translation (ML Kit offline) and romanization (Korean etc.) of lyrics.
8. Progress bar styles (wavy etc.).
9. Rename app to "R Music" + new package id, signing key (keep it safe).
   - DONE (2026-11-14): launcher icon from the user's green cobra (dark #282828 background, green #1BE7A4): adaptive foreground + monochrome PNGs in `drawable-*dpi/`, legacy `mipmap-*` PNGs for API 24-25, Play Store/README PNGs, `ic_launcher_background` = #282828. Default accent = `R.color.logo_green` (App.kt, theme version bumped 3 -> 4 so existing installs get it once).
   - Not done: the status-bar `ic_notification` still has the old art (needs a simplified single-color cobra silhouette; note `ic_notification.xml` in drawable/ and PNGs in drawable-*dpi/ both exist, check which one wins before replacing). On Android 12+ with Material You on (default), the accent comes from the wallpaper, not from logo_green.

## Constraints
- GPL-3: fork must stay public if APK is shared.
- Follow AGENTS.md in the repo (Room migrations, no main-thread IO, strings in resources).
- Oto Music is closed source; do not try to edit its APK.
- Commit trailers: Co-Authored-By + Claude-Session lines as per the session attribution rule.
