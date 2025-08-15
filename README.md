# Viz-It

Viz-It — Because sometimes, you just need to see it.    

**Viz-It** is a Minecraft mod and API for creating, managing, and visualizing structures using [Display Entities](https://minecraft.wiki/w/Display).  
It’s designed for **map makers**, **server admins**, and **mod developers** who need in-world previews, holograms, and complex visual aids — without placing a single block.

---

## ✨ Features

### Core (v1.0)
- **Display Entity Management**
    - Create **Block Displays**, **Text Displays**, and **Item Displays**
    - All entity properties supported: position, rotation, glow, glow color, light level, scaling, custom data
    - Type-specific properties (e.g. text alignment, background color, opacity for Text Displays)
    - Fully serialized — entities persist across restarts

- **Shapes**
    - Built-in **Cuboid** and **Sphere** shapes
    - Freely position and rotate
    - Multiple render styles: **Frame**, **Hull**, **Minimal**, **Marked**

- **Composite Structures**
    - Build from sets of block states
    - Move and rotate as a single structure
    - Combine with shapes for advanced previews

---

### Planned Features
#### v1.1 – Per-Player Visibility
- Control which players can see each display entity
- Grant or restrict access individually

#### v1.2 – Schematic Loading & Builder Assist
- Import schematics from other supported mods
- Show ghost structures with correct block placement checks
- Give builders real-time feedback on progress

---

## 📜 Commands

All commands are under `/vizit`.

**Examples:**

### Main Commands
- `/vizit create block <name>`
- `/vizit create text <name>`
- `/vizit create item <name>`
- `/vizit create shape cuboid <name> <from> <to>`
- `/vizit create shape sphere <name> <center> <radius>`
- `/vizit create composite <name> from-blockstates <states...>`
- `/vizit set <id> <property> <value>` (properties depend on entity type)
- `/vizit move <id> <x> <y> <z> [yRot] [xRot]`
- `/vizit rotate <id> <axis> <degrees>`
- `/vizit delete <id>`
- `/vizit list [type]`
- `/vizit show <id> [player]`
- `/vizit hide <id> [player]`

## ⚠ Performance Notice

> **Hull visualizations** of large regions can cause severe lag, especially for players with lower-end machines.  
> For big areas, use **Frame** or **Minimal** styles instead.  
> _(Added following recent player-reported issues)_

---

## 🛠 Programming API

**Viz-It** provides a simple Java API for mod developers.

```java
DisplayManager manager = DisplayManager.get(level);

// Create a text display
UUID id = manager.textDisplays().create(pos, Component.literal("Hello"));

// Update a block display
manager.blockDisplays().findById(id).ifPresent(display -> {
    display.setGlow(true);
    display.setLightLevel(15);
});

```

# 🧩 License
This project is licensed under the MIT License.

# 🤝 Contributing
Pull requests, bug reports, and suggestions are welcome.
Open an issue to discuss major changes.


# Display Entity Properties

The properties match the properties of Minecraft's display entities, allowing you to customize their appearance and behavior. For further reference, see the [Minecraft Wiki on Display Entities](https://minecraft.wiki/w/Display#Data_values).

## Common Properties
| Property              | Type       | Description                                                                                    |
|-----------------------|------------|------------------------------------------------------------------------------------------------|
| `Pos`                 | Vec3       | Position in world (x, y, z)                                                                    |
| `yRot`                | float      | Rotation in degrees                                                                            |
| `xRot`                | float      | Rotation in degrees                                                                            |
| `billboard`           | String     | Controls entity pivoting                                                                       |
| `view_range`          | float      | Maximum view range of the entity                                                               |
| `Glowing`             | boolean    | Enables or disables entity glowing                                                             |
| `glow_color_override` | string     | Overrides default glow color. Valid values are the 16 default team colors. Defaults to `white` |
| `brightness`          | int (0–15) | Sets both block and sky light levels                                                           |

Note: Valid colors for `glow_color_override` are `black`, `dark_blue`, `dark_green`, `dark_aqua`, `dark_red`, `dark_purple`, `gold`, `gray`, `dark_gray`, `blue`, `green`, `aqua`, `red`, `light_purple`, `yellow`, or `white`.

## Block Display Properties

| Property      | Type   | Description                                     |
|---------------|--------|-------------------------------------------------|
| `block_state` | string | Namespaced block state (e.g. `minecraft:stone`) |

Note: The important property of the `block_state` is the `Name` property, which is the namespaced ID of the block. It defines which block is displayed.

## Item Display Properties
| Property       | Type   | Description                                         |
|----------------|--------|-----------------------------------------------------|
| `item`         | string | Namespaced item ID (e.g. `minecraft:diamond_sword`) |
| `item_display` | string | Describes item model transform applied to the item  |

Note: `item_display` can be one of: `none`, `thirdperson_lefthand`, `thirdperson_righthand`, `firstperson_lefthand`, `firstperson_righthand`, `head`, `gui`, `ground`, or `fixed`. Defaults to `none`.

## Text Display Properties

| Property             | Type         | Description                                      |
|----------------------|--------------|--------------------------------------------------|
| `text`               | JSON text    | Raw JSON text to display                         |
| `alignment`          | string       | `center`, `left`, or `right` (default: `center`) |
| `background`         | int (ARGB)   | Background color (alpha < 26 makes transparent)  |
| `default_background` | boolean      | Uses default text background color               |
| `line_width`         | int          | Max width before wrapping (default: 200)         |
| `see_through`        | boolean      | Text visible through blocks. Defaults to `false` |
| `shadow`             | boolean      | Draw text shadow. Defaults to `false`            |
| `text_opacity`       | byte (0–255) | Text opacity                                     |
