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

- Player cover gestures (written 2026-11-14, not built/tested): `views/CoverGestureLayout` is now the root of `fragment_player_album_cover.xml`. Drag up/down on the cover = music volume with a small bar (`volumeOverlay`); double tap right/left half = +/-10 s (`seekFeedback`). Plain taps are held back for the double-tap timeout and then replayed so the cover's own click (lyrics dialog) still works. The player panel can no longer be collapsed by dragging the cover itself (disallowIntercept on DOWN). Horizontal swipes still change songs through the ViewPager.

## Next, in order (updated 2026-11-15; the user wants steps done in this order and ONLY when he says go)

Status legend: WRITTEN = code exists on the branch but was never compiled/tested on a device; check the CI build first.

1. Check the CI build of the latest push (Actions). Steps 2-3 below and the player gestures are WRITTEN only. Fix any compile errors before anything else.
2. Home cover wall like the DroidDeck reference video the user shared (look at DroidDeck only for layout and motion, never copy its logo or art): the wall is the full-screen BACKGROUND of the home screen, also behind the status bar; tall tilted COLUMNS (about 12-15 degrees) drifting slowly downwards along the tilt; rounded tile corners, small gaps; uneven darkness (darker on the left, edges and bottom, brighter on the right); the logo and the main buttons are overlaid at the bottom; scrolling up reveals the lists. Tapping a cover plays that song. STATUS 2026-11-15 (WRITTEN, not built): `TiltedCoverGridView` now draws tilted COLUMNS drifting down, rounded tiles (pre-rounded bitmaps), vignette darker on the left; the wall is still the hero block at the top of the scrolling content (height = visible area), NOT yet a background behind the toolbar/status bar, and the logo/buttons are not yet overlaid at the bottom of it.
2b. Wall interaction (user request 2026-11-15, not written yet): tapping a cover plays that song and it shows in the mini player (the oval from step 4); it must NOT open the full Now Playing screen. Long-pressing a cover opens a small menu with (1) Pin / Unpin: a pinned song keeps its cover on the wall permanently (store pinned song ids in preferences; pinned ones are always included before the last/most played ones), and (2) Go to song: opens the full Now Playing screen for that song (plays it first). Needs a long-press branch in `TiltedCoverGridView` (GestureDetector.onLongPress + hitTest) and a callback `onSongLongClick`.
3. Now Playing screen: (a) prev/next arrows: WRITTEN in `AbsPlayerControlsFragment.setUpPrevNext` (mirrors both buttons with scaleX=-1 when the layout is RTL so arrows point outwards); themes that do not extend this base class (Classic, Gradient, Circle, Peek, DriveMode) are NOT covered yet, (b) border with transparent fill: WRITTEN (`updatePrevNextBorder`, ripple + stroke in the controls colour; the stroke colour is 0/transparent until `updatePrevNextColor` runs, check that), (c) the screen takes its colours from the current song cover (some themes already do; find which ones do not), (d) volume drag and double-tap seek on the cover are WRITTEN (`CoverGestureLayout`), test them.
4. Mini player: replace the rectangular bar at the bottom with a floating OVAL at the TOP-LEFT of the screen inside the app. It must stay at the physical top-left and must NOT flip with the language direction (RTL). Settings for its size, shape and background (cover colour / transparent / fixed colour).
5. Bug: on an artist page the songs list shows only titles and no cover images. Fix.
6. Backup settings: let the user pick a folder for backups (Storage Access Framework, persisted URI permission) and schedule automatic backups every N hours (6 or 7 as options; WorkManager). Reuse BackupHelper.
7. App icon picker: Settings lets the user choose between several app icons (activity-alias per icon). Offer the existing green cobra plus a few alternatives; ask the user whether he has own artwork before designing.
8. Rename to "R Music": README and repo description first, then app name, new package id, signing key (keep it safe). The user renames the GitHub repo himself in Settings.
   - DONE (2026-11-14): launcher icon from the user's green cobra (dark #282828 background, green #1BE7A4): adaptive foreground + monochrome PNGs in `drawable-*dpi/`, legacy `mipmap-*` PNGs for API 24-25, Play Store/README PNGs, `ic_launcher_background` = #282828. Default accent = `R.color.logo_green` (App.kt, theme version bumped 3 -> 4 so existing installs get it once).
   - Not done: the status-bar `ic_notification` still has the old art (needs a simplified single-color cobra silhouette; note `ic_notification.xml` in drawable/ and PNGs in drawable-*dpi/ both exist, check which one wins before replacing). On Android 12+ with Material You on (default), the accent comes from the wallpaper, not from logo_green.
9. Merge artists (dropdown of existing artists) and album rename that regroups songs, written into the MP3 tags (existing tag editor in activities/tageditor).
10. Synced lyrics from LRCLIB (no key, no limit), fetched automatically when a song has none and saved; LrcView already exists; tap a line to seek. The user explicitly wants lyrics to be added on their own.
11. Artist info button (MusicBrainz + TheAudioDB + Wikipedia, cached for offline).
12. English translation of lyrics (ML Kit offline) and romanization (Korean etc.).
13. Progress bar styles (wavy etc.).
14. Status-bar notification icon: simplified single-colour cobra silhouette (see the note under Rename).

## How to work with this user
- He writes Egyptian Arabic by dictation, wants short mobile-friendly replies, and says "do not start working" when he is only collecting requirements; wait for his go.
- Claude Code can not push without a token; he sends a classic token (repo scope, 7 days) and deletes it afterwards. Never store a token in git config or files; use a one-off `git -c http.extraheader=...` push. Fine-grained tokens failed with 403 twice (no Contents write), classic worked.
- He tests only by installing the debug APK from the Actions run (Artifacts). Say clearly what was not built or tested.

## Constraints
- GPL-3: fork must stay public if APK is shared.
- Follow AGENTS.md in the repo (Room migrations, no main-thread IO, strings in resources).
- Oto Music is closed source; do not try to edit its APK.
- Commit trailers: Co-Authored-By + Claude-Session lines as per the session attribution rule.
