# AGENTS.md

Guidance for coding agents working in this repository.

## Project Shape

- This is an Android music player, not a Compose or SwiftUI app.
- Work from the repository root and use the checked-in Gradle wrapper.
- Main modules are `:app` and `:appthemehelper`.
- App code lives under `app/src/main/java/code/name/monkey/retromusic`.
- Product flavors matter:
  - `normal` contains Play Billing, Cast, and Play-only behavior.
  - `fdroid` contains stubs or alternative implementations for store-free builds.
- Keep flavor-specific code in `app/src/normal` or `app/src/fdroid`; do not reference normal-only classes from `main`.

## Build And Verification

- Use Java 21.
- Prefer these checks before handing off code changes:
  - `./gradlew lint`
  - `./gradlew app:assemble`
- For a faster local loop, use focused module tasks such as:
  - `./gradlew :app:assembleNormalDebug`
  - `./gradlew :app:assembleFdroidDebug`
- CI currently validates wrapper integrity, `./gradlew lint`, and `./gradlew app:assemble`.
- There is no substantial test suite in this checkout right now; do not invent broad test scaffolding for a narrow fix unless the change needs it.

## Architecture Rules

- `App.kt` owns app startup, Koin startup, billing, crash handling, shortcuts, wallpaper accent setup, and default preferences.
- `MainModule.kt` is the dependency injection map. Add new repositories, services, and ViewModels there instead of constructing them ad hoc in UI code.
- `Repository` and `RealRepository` are the app-level data facade. Prefer repository/ViewModel changes over direct data access from fragments or adapters.
- Room state is in `RetroDatabase` and related DAOs/entities under `db/`. If a schema changes, add a real migration and wire it into `Room.databaseBuilder(...).addMigrations(...)`.
- MediaStore, Room, tag editing, backup/restore, image decoding, and playlist operations must stay off the main thread. Prefer `viewModelScope` or lifecycle-aware scopes with `Dispatchers.IO`.
- Avoid unowned background work from adapters, dialogs, or menu helpers. If existing code uses it, keep new work tighter and lifecycle-aware.
- Keep UI state in ViewModels where possible. Fragments should coordinate views, navigation, and observers rather than becoming data/business logic owners.

## Android UI Rules

- This app uses XML layouts, Fragments, ViewBinding, Material Components, and Navigation/Safe Args. Match those patterns.
- Reuse existing base fragments, adapters, dialogs, extensions, and helper utilities before adding new framework patterns.
- Keep `app/src/main/res/navigation/*.xml` in sync with fragment argument changes.
- Put user-visible strings in resources. Use `donottranslate.xml` only for non-translatable constants.
- Respect the existing theme system: `appthemehelper`, Material attrs, `values/colors*.xml`, `values/styles*.xml`, dimensions, and night/resource qualifiers.
- Avoid introducing Jetpack Compose for isolated screens unless the user explicitly asks for a larger UI migration.

## Playback, Artwork, And Performance

- Treat `MusicPlayerRemote`, `service/`, player fragments, widgets, Android Auto, notifications, and Cast as hot paths.
- Do not block playback or service callbacks with disk, network, MediaStore, Room, or artwork work.
- Use the existing Glide integration and `RetroGlideExtension` patterns for album art, artist art, palettes, widgets, and notifications.
- Clear or replace long-lived Glide targets when updating widgets/notifications to avoid leaking stale targets.
- Be cautious with large libraries: avoid repeated full-list sorting/filtering on the main thread and avoid decoding large artwork directly in render paths.
- Do not add music downloading or streaming behavior; the app is a local music player.

## Dependencies And Generated Files

- Keep dependency versions in `gradle/libs.versions.toml`.
- Use KSP-backed processors already configured for Room and Glide.
- Do not edit generated outputs, `.gradle/`, build folders, IDE metadata, or signing/local property files.
- Do not commit secrets such as `retro.properties`, `public.properties`, keystores, or API keys.

## Change Hygiene

- Start with `git status --short` and preserve unrelated user changes.
- Keep changes scoped; do not perform broad rewrites, dependency upgrades, or style-only churn unless requested.
- Match surrounding Kotlin/Java style and resource organization. This repo has both Kotlin and Java; prefer the local file's existing language and style.
- For significant features or refactors, follow `CONTRIBUTING.md`: confirm the direction before investing in a large implementation.
