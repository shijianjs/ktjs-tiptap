---
sidebar_position: 5
title: License
---

# License

## This repository

The wrapper and its documentation are MIT — see `LICENSE` at the repository root.

## The upstream project

Tiptap is MIT licensed:

> MIT License
> Copyright (c) 2025, Tiptap GmbH

This repository is MIT licensed as well, so the wrapper and the wrapped packages share the same terms.
The upstream copyright notice is reproduced in the module README and in
[LICENSE](https://github.com/shijianjs/ktjs-tiptap/blob/main/LICENSE).

## What is open source, and what is not

Everything this wrapper touches is free and open source: `@tiptap/core`, `@tiptap/starter-kit`, the
individual `@tiptap/extension-*` packages in this repository, and `@tiptap/pm`. They are all MIT, no
account or license key needed.

Tiptap also sells **Tiptap Pro**: a set of commercial extensions and features published under the
`@tiptap-pro/*` scope on Tiptap's own registry (`registry.tiptap.dev`). Those require a paid
subscription and a token to install. **None of them is wrapped here, and none is referenced by this
repository's build** — the wrapper only declares types for the free packages. If you install a Pro
extension yourself, it arrives under its own commercial terms, which are between you and Tiptap.

Two practical notes:

- The paid packages never share a name with a free one, so `@tiptap/extension-*` is always safe and
`@tiptap-pro/*` never is.
- The wrapper's own `extensions` option accepts any extension object, so a Pro extension can be passed
  in as `JsAny` without the wrapper claiming anything about it.

Upstream source: [https://github.com/ueberdosis/tiptap](https://github.com/ueberdosis/tiptap)

Check the upstream license text yourself before relying on it; this page is a pointer, not legal
advice.
