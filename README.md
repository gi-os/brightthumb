<img src="docs/icon.png" alt="" width="72" align="left" />

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
- **Voice typing (v1.1.0, better model in v1.2.0)**: fully offline speech-to-text
  built in. NVIDIA's Parakeet TDT 110M (int8) runs on-device through
  [sherpa-onnx](https://github.com/k2-fsa/sherpa-onnx); the model ships inside
  the APK, so nothing is downloaded and no audio ever leaves the phone —
  BrightThumb's no-network promise holds. Trigger it by swiping **up on the
  return key** (or the mic swipe on the special-action key), talk, then tap ✓.
  Punctuated, capitalized text lands at the cursor. The first use asks for mic
  permission and takes a few extra seconds while the model loads.

  v1.2.0 replaced Whisper tiny.en, which was noticeably weak on ordinary
  conversational speech (~12.8% vs ~7.5% WER on the eight-domain Open ASR
  average). Parakeet is also about 2.5x faster in practice: Whisper decodes a
  full 30-second window no matter how short the clip, while a transducer decodes
  only the audio you gave it, so brief dictation got much quicker as well as
  more accurate.

- **Close-keyboard key**: swipe **down-right on the emoji key** to dismiss the
  keyboard. Thumb-Key defines this key but never places it in a layout, so
  upstream there is no way to put the keyboard away from the keyboard itself.

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

<!-- bright-footer:begin -->
---

## Bright\*

**It's not Light, it's Bright.**

26 open-source apps for the **Light Phone III** — camera, music, maps, messages,
reading, transit, games. The phone has no app store, so they install by sideload: scan one
code from **[brightmarket.gzl.dev](https://brightmarket.gzl.dev)** and BrightMarket keeps them updated.

[Roll](https://github.com/gi-os/Roll) · [BrightNotebook](https://github.com/gi-os/BrightNotebook) · [BrightControl](https://github.com/gi-os/BrightControl) · [BrightWay](https://github.com/gi-os/BrightWay) · [BrightChat](https://github.com/gi-os/BrightChat) · [browse all 26 →](https://brightmarket.gzl.dev)

The Light Phone does not sponsor or endorse any of these. Built by
[Giovanni Lupo](https://github.com/gi-os) — if this one is useful to you, a ⭐ helps the next
person find it.
<!-- bright-footer:end -->
