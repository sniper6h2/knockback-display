# Knockback Display

Shows the **Knockback** level of your items right on their slot, in your hotbar, inventory and every container, so you always pick the right weapon. Enchanted books carrying Knockback are labelled too.

**Requires [EagerLib](https://github.com/sniper6h2/EagerLib)** for the same Minecraft version. Press **Right Shift** in game (or use Mod Menu) to:
- pick Roman numerals (II), numbers (2) or a KB prefix (KB II)
- choose the corner, colour and text shadow
- turn the book labels on or off
- set a toggle key
- enable the *Held Knockback* HUD element under **Edit HUD**

It's client-side only and works on any server.

| Minecraft | Jar | Java |
|---|---|---|
| 26.3 / 26.2 / 26.1 | `knockback-display-1.1.0+mc26.x.jar` | 25 |
| 1.21.11 / 1.21.8 / 1.21.6 / 1.21.4 / 1.21 – 1.21.1 | `knockback-display-1.1.0+mc1.21.x.jar` | 21 |

## Building

This repository includes [EagerLib](https://github.com/sniper6h2/EagerLib) as a git submodule, so clone it with submodules:

```bash
git clone --recursive https://github.com/sniper6h2/knockback-display.git
```

If you already cloned it without them, run:

```bash
git submodule update --init
```

You need JDK 25, which also builds the Java 21 jars. Then run:

```bash
./gradlew buildAll
```

The jars land in `dist/`, one per Minecraft version. EagerLib is built from the submodule automatically.

To move to the newest EagerLib, run:

```bash
git submodule update --remote eagerlib
```

### In-game smoke test (dev only)

```bash
./scripts/smoke-test.sh 26.3
```

This generates a flat test world and launches the game with this mod and EagerLib. It screenshots the HUD and every EagerLib page, then quits. Screenshots land in `versions/<mc>/run/screenshots/`.

## License

MIT

The 512 px sword icon is `modrinth/icon.png`. `tools/Icon.java` is the program that generated it.
