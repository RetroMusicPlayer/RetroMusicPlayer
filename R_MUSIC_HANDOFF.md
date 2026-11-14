# R Music handoff (read this first)

User (GitHub: ELRFAAY) writes Egyptian Arabic; reply in Egyptian Arabic, short, mobile-friendly.
App: "R Music", a fork of Retro Music (Kotlin, XML views, Room, Koin, jaudiotagger). Base repo: RetroMusicPlayer/RetroMusicPlayer. Fork: ELRFAAY/RetroMusicPlayer (default branch dev).
Work lives on branch `claude/backup-listening-data` (includes `claude/arabic-complete`). Code was NOT compiled locally (no Android SDK in the sandbox); the CI workflow `.github/workflows/build-branch.yml` builds a debug APK on every push to `claude/**`. The user must enable Actions in the fork first.

## Done
- Arabic strings completed (values-ar-rSA).
- Backup/restore of listening history + play counts (BackupHelper, BackupContent.LISTENING_DATA, checkbox in activity_restore.xml). Matching on restore is by title/artist/album/duration.

## Next, in order
1. Verify CI build passes; fix compile errors if any (ask user to paste the error).
2. New Room table for listening events (song key, played_at, listened_ms) + migration 24->25 in RoomMigrations.kt; log from the playback service; stats page (hours, song count, history); include in backup JSON.
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
