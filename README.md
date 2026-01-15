# Waila (What Am I Looking At) — Hytale Mod

**Waila** is a utility mod for Hytale servers that displays a tooltip at the top of the screen with information about the block the player is currently looking at. It is a server-side implementation of the classic "What Am I Looking At" mod, utilizing Hytale's Custom UI system.

## 🌍 Localization / Языковая поддержка
This mod fully supports multi-language servers.
*   🇺🇸 **English (en-US)** — Default
*   🇷🇺 **Russian (ru-RU)** — Fully translated

## ✨ Features

When a player looks at a block, a HUD appears displaying the following information (customizable):

*   **Block Name:** The localized name of the block.
*   **Block Icon:** Renders the actual item model of the block.
*   **Mod Source:** Displays which namespace/mod the block belongs to (e.g., `Hytale`, `Minecraft`, etc.).
*   **Internal ID:** Shows the registry key (e.g., `stone`).
*   **Mining Speed:** Shows how efficient the currently held tool is against the target block.

## 📸 Screenshots

### In-Game HUD
*See exactly what you are looking at with real-time updates.*

<img width="1920" height="1080" alt="Hytale2026-01-16_00-26-00" src="https://github.com/user-attachments/assets/6a817d5f-6db8-4558-9c55-1e1d9f103f53" />

### Configuration Menu

*Fully interactive in-game GUI to toggle specific information.*

<img width="1920" height="1080" alt="Hytale2026-01-16_00-27-49" src="https://github.com/user-attachments/assets/a575438d-c133-46a7-9e82-c7cc3112989f" />

## ⚙️ Configuration & Commands

You don't need to edit files manually! The mod comes with a built-in UI for configuration.

### Command
*   `/waila` — Opens the configuration menu.

### Permissions
*   `waila.command.waila` — Allows access to the `/waila` command.

### Configurable Options
Inside the menu, every player can customize their own experience:
*   **Show Waila:** Master toggle to enable/disable the HUD.
*   **Show Block Name:** Toggle the display of the block's display name.
*   **Show Mod Name:** Toggle the namespace identifier.
*   **Show Block ID:** Toggle the raw technical ID.
*   **Show Mining Speed:** Toggle the tool efficiency calculator.
*   **Show Item Icon:** Toggle the visual icon of the block.
