---
sidebar_position: 5
title: 许可证
---

# 许可证

## 本仓库

wrapper 与文档是 MIT，见仓库根目录的 `LICENSE`。

## 上游项目

Tiptap 使用 MIT 许可证：

> MIT License
> Copyright (c) 2025, Tiptap GmbH

本仓库同样是 MIT 许可，因此 wrapper 与被封装的包条款一致。上游版权声明已出现在模块 README 与
[LICENSE](https://github.com/shijianjs/ktjs-tiptap/blob/main/LICENSE) 中。

## 哪些是开源免费，哪些不是

本 wrapper 涉及的一切都是免费开源：`@tiptap/core`、`@tiptap/starter-kit`、本仓库里的各个
`@tiptap/extension-*` 包，以及 `@tiptap/pm`。它们都是 MIT，不需要账号或许可密钥。

Tiptap 同时售卖 **Tiptap Pro**：一批商业扩展与特性，发布在 Tiptap 自家 registry（`registry.tiptap.dev`）的
`@tiptap-pro/*` scope 下，需要付费订阅与 token 才能安装。**本仓库没有封装它们中的任何一个，构建里也没有
任何一处引用** —— wrapper 只为免费包声明类型。若你自行安装 Pro 扩展，它按它自己的商业条款提供，那是你与
Tiptap 之间的事。

两个实用判断：

- 付费包从不与免费包同名，所以 `@tiptap/extension-*` 永远安全，`@tiptap-pro/*` 永远不是。
- wrapper 的 `extensions` 选项接受任意扩展对象，因此 Pro 扩展也能以 `JsAny` 传进来，wrapper 不对它作任何承诺。

上游源码：[https://github.com/ueberdosis/tiptap](https://github.com/ueberdosis/tiptap)

使用前请自行核对上游许可证原文；本页只是提示，不构成法律意见。
