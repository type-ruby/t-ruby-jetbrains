# T-Ruby for JetBrains IDEs

[![JetBrains Plugin](https://img.shields.io/jetbrains/plugin/v/29335-t-ruby?label=JetBrains%20Marketplace)](https://plugins.jetbrains.com/plugin/29335-t-ruby)
[![JetBrains Downloads](https://img.shields.io/jetbrains/plugin/d/29335-t-ruby)](https://plugins.jetbrains.com/plugin/29335-t-ruby)
[![T-Ruby Compiler](https://img.shields.io/gem/v/t-ruby?label=T-Ruby%20Compiler)](https://rubygems.org/gems/t-ruby)
[![License](https://img.shields.io/github/license/type-ruby/t-ruby-jetbrains)](LICENSE)

T-Ruby language support for JetBrains IDEs. Provides syntax highlighting, LSP-based code intelligence, and development tools for [T-Ruby](https://github.com/type-ruby/t-ruby) - a TypeScript-style static type system for Ruby.

## Supported IDEs

- IntelliJ IDEA 2024.2+ (Ultimate & Community)
- RubyMine 2024.2+
- WebStorm 2024.2+
- PyCharm 2024.2+
- GoLand 2024.2+
- And other JetBrains IDEs based on IntelliJ Platform 2024.2+

## Features

- Syntax highlighting for `.trb` and `.d.trb` files
- LSP-based code intelligence (via LSP4IJ):
  - Real-time diagnostics (type errors)
  - Autocomplete suggestions
  - Go to definition
  - Hover information
- Actions:
  - `Compile T-Ruby File` (Ctrl+Shift+T / Cmd+Shift+T)
  - `Generate Declaration File` (Ctrl+Shift+D / Cmd+Shift+D)

## Requirements

- [T-Ruby Compiler](https://github.com/type-ruby/t-ruby) (`trc`) must be installed and available in your PATH
- [LSP4IJ](https://plugins.jetbrains.com/plugin/23257-lsp4ij) plugin (will be installed automatically as dependency)

```bash
gem install t-ruby
```

## Installation

Install from the [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/29335-t-ruby):

1. Open your JetBrains IDE
2. Go to Settings/Preferences → Plugins
3. Search for "T-Ruby"
4. Click Install

## Configuration

Configure the T-Ruby compiler path in:
Settings/Preferences → Tools → T-Ruby

| Setting | Default | Description |
|---------|---------|-------------|
| `trc Path` | `trc` | Path to the T-Ruby compiler executable |
| `Enable LSP` | `true` | Enable Language Server Protocol support |
| `Enable Diagnostics` | `true` | Enable real-time diagnostics |
| `Enable Completion` | `true` | Enable autocomplete suggestions |

## Compatibility

| Plugin Version | T-Ruby Compiler | JetBrains IDEs |
|----------------|-----------------|----------------|
| 0.1.x          | >= 0.0.30       | 2024.2 - 2025.4 |

## Building from Source

```bash
# Build the plugin
./gradlew buildPlugin

# Run IDE with plugin for testing
./gradlew runIde

# Run tests
./gradlew test
```

## Related

- [T-Ruby Compiler](https://github.com/type-ruby/t-ruby) - The main T-Ruby compiler
- [T-Ruby VS Code](https://github.com/type-ruby/t-ruby-vscode) - VS Code extension
- [T-Ruby Vim](https://github.com/type-ruby/t-ruby-vim) - Vim/Neovim plugin

## License

MIT
