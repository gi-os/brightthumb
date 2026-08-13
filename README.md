# BrightThumb

A privacy-first, one-thumb keyboard for the **Light Phone III**, forked from
[Thumb-Key](https://github.com/dessalines/thumb-key) and adapted to the LightOS
black-and-white panel.

Thumb-Key's core premise is perfect for the Light Phone: you type with one thumb,
the keyboard hugs one side of the screen, and everything stays on-device — no
network, no telemetry, no cloud. BrightThumb keeps all of that and re-skins it for
a monochrome display.

## What changed from Thumb-Key

- **Package**: `com.dessalines.thumbkey` → `com.gios.brightthumb`
- **LightOS theme**: new default `LightOS` color scheme (pure black & white, plus
  grays) tuned for the LPIII's B&W e-ink-style panel; dark mode is the default.
- **Launcher icon**: inverted to a white-on-black mark that sits correctly on the
  LightOS home screen.
- **Versioning**: v1.0.x, independent of upstream releases.

Everything else — the layouts (including the default one-thumb `ENThumbKey`
layout), the swipe gestures, the settings app, the Room-backed settings — is
stock Thumb-Key.

## Install

The Light Phone III runs Android, so BrightThumb is a normal Android APK.
Sideload the signed APK from the [releases page](../../releases), or use
[Obtainium](https://obtainium.imranr.com/) with this repository's GitHub
releases as the source. The APK is signed with a stable certificate (pinned in
[`signing-fingerprint.txt`](signing-fingerprint.txt)), so updates never require
an uninstall.

After installing, enable it in **Settings → System → Languages & input →
On-screen keyboard** and pick BrightThumb.

## Building

CI builds, signs, and publishes every push to `main` (see
[`.github/workflows/build.yml`](.github/workflows/build.yml)). To build locally:

```sh
./gradlew :app:assembleDebug -PversionCode=1
```

The keystore is committed so every build — CI or local — signs with the same
certificate.

## License

AGPL-3.0, inherited from [Thumb-Key](https://github.com/dessalines/thumb-key)
by [dessalines](https://github.com/dessalines). If you find Thumb-Key useful,
consider supporting the upstream project — all of the hard keyboard work lives
there.
