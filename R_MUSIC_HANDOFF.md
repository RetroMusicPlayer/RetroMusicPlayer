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

## Next, in order
3. Home screen: tilted dark cover grid (last played / most played), optional animation, settings for speed/angle/darkness/count/source; tapping the logo opens stats. Own design, do not copy DroidDeck or Retro Music logo art.
4. Merge artists (dropdown of existing artists) and album rename that regroups songs, written into the MP3 tags (existing tag editor in activities/tageditor).
5. Synced lyrics from LRCLIB (no key, no limit); LrcView already exists; tap a line to seek.
6. Artist info button (MusicBrainz + TheAudioDB + Wikipedia, cached for offline).
7. English translation (ML Kit offline) and romanization (Korean etc.) of lyrics.
8. Progress bar styles (wavy etc.).
9. Rename app to "R Music" + new package id, signing key (keep it safe), launcher icon from the user's green cobra logo (needs simplified small-size version, adaptive icon, transparent background), accent color = the logo green.

## Constraints
- GPL-3: fork must stay public if APK is shared.
- Follow AGENTS.md in the repo (Room migrations, no main-thread IO, strings in resources).
- Oto Music is closed source; do not try to edit its APK.
- Commit trailers: Co-Authored-By + Claude-Session lines as per the session attribution rule.
