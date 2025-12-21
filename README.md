<p align="center">
  <img src="https://avatars.githubusercontent.com/u/248530250" alt="T-Ruby" height="120">
</p>

<h1 align="center">T-Ruby for JetBrains IDEs</h1>

<p align="center">
  <a href="https://type-ruby.github.io">Official Website</a>
  &nbsp;&nbsp;•&nbsp;&nbsp;
  <a href="https://github.com/type-ruby/t-ruby">GitHub</a>
  &nbsp;&nbsp;•&nbsp;&nbsp;
  <a href="https://plugins.jetbrains.com/plugin/29335-t-ruby">JetBrains Marketplace</a>
</p>

<p align="center">
  <a href="https://plugins.jetbrains.com/plugin/29335-t-ruby"><img src="https://img.shields.io/jetbrains/plugin/v/29335-t-ruby?label=JetBrains%20Marketplace" alt="JetBrains Plugin"></a>
  <a href="https://plugins.jetbrains.com/plugin/29335-t-ruby"><img src="https://img.shields.io/jetbrains/plugin/d/29335-t-ruby" alt="JetBrains Downloads"></a>
  <a href="https://rubygems.org/gems/t-ruby"><img src="https://img.shields.io/gem/v/t-ruby?label=T-Ruby%20Compiler" alt="T-Ruby Compiler"></a>
  <a href="LICENSE"><img src="https://img.shields.io/github/license/type-ruby/t-ruby-jetbrains" alt="License"></a>
</p>

---

T-Ruby language support for JetBrains IDEs. Provides syntax highlighting, LSP-based code intelligence, and development tools for [T-Ruby](https://github.com/type-ruby/t-ruby) - a TypeScript-style static type system for Ruby.

> **Note**: This plugin works with all JetBrains IDEs including IntelliJ IDEA, RubyMine, WebStorm, and more.

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

## Development

### Quick Iteration (Hot Reload)

For rapid development with automatic rebuilds:

```bash
./scripts/dev.sh
```

This starts the IDE and watches for file changes. When you save a file, the plugin rebuilds automatically and reloads in the running IDE.

**Requirements**: `fswatch` (`brew install fswatch`)

## Quick Start Example

1. Create a new file `hello.trb`:

```trb
type UserId = String

interface User
  id: UserId
  name: String
  age: Integer
end

def greet(user: User): String
  "Hello, #{user.name}!"
end
```

2. Save the file - you'll see syntax highlighting and real-time diagnostics

3. Hover over types to see their definitions

4. Use `Ctrl+Space` for autocomplete suggestions

## Troubleshooting

### Plugin not working

1. Check if `trc` is installed: `which trc`
2. Verify the path in settings: `Settings` > `Tools` > `T-Ruby`
3. Check IDE logs: `Help` > `Show Log in Finder/Explorer`

### No syntax highlighting

1. Ensure file has `.trb` or `.d.trb` extension
2. Check file type association: `Settings` > `Editor` > `File Types`

### Performance issues

- Disable diagnostics for large files
- Restart the IDE

## Contributing

Issues and pull requests are welcome!
https://github.com/type-ruby/t-ruby-jetbrains/issues

## Related

- [T-Ruby Compiler](https://github.com/type-ruby/t-ruby) - The main T-Ruby compiler
- [T-Ruby VS Code](https://github.com/type-ruby/t-ruby-vscode) - VS Code extension
- [T-Ruby Vim](https://github.com/type-ruby/t-ruby-vim) - Vim/Neovim plugin

## License

MIT
