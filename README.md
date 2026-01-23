**Waila** is a utility mod for Hytale servers that displays a tooltip at the top of the screen with information about the block the player is currently looking at. It is a server-side implementation of the classic "What Am I Looking At" mod, utilizing Hytale's Custom UI system.

### Support **[MultipleHUD](https://www.curseforge.com/hytale/mods/multiplehud)**

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
*   **Farming Status:** Shows current growth percentage and level.

## 📸 Screenshots

### In-Game HUD
*See exactly what you are looking at with real-time updates.*

<img width="1920" height="1080" alt="Hytale2026-01-19_21-45-11" src="https://github.com/user-attachments/assets/bafe54b4-4b0e-419e-9f17-ca049b7c9e6f" />

### Configuration Menu

*Fully interactive in-game GUI to toggle specific information and adjust HUD position.*

<img width="1920" height="1080" alt="Hytale2026-01-24_02-18-02" src="https://github.com/user-attachments/assets/075a12b3-36a5-4c4d-b2ca-94dfac0a747d" />

## ⚙️ Configuration & Commands

You don't need to edit files manually! The mod comes with a built-in UI for configuration.

### Command
*   `/waila` — Opens the configuration menu.

### Permissions
*   `waila.command.waila` — Allows access to the `/waila` command.

### Configurable Options
Inside the menu, every player can customize their own experience:

#### Content Settings
*   **Show Waila:** Master toggle to enable/disable the HUD.
*   **Show Block Name:** Toggle the display of the block's display name.
*   **Show Mod Name:** Toggle the namespace identifier.
*   **Show Block ID:** Toggle the raw technical ID.
*   **Show Mining Speed:** Toggle the tool efficiency calculator.
*   **Show Farming Info:** Toggle crop growth stage and harvestability status.
*   **Show Item Icon:** Toggle the visual icon of the block.

#### Visual & Layout Settings
*   **Mirror Orientation:** Flips the HUD layout (Useful for different screen setups).
*   **HUD Scale:** Adjust the size of the Waila tooltip.

*   **Offset X / Y:** Finely tune the position of the HUD on your screen.
