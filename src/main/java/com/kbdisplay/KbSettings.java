package com.kbdisplay;

import dev.eagerlib.api.BoolOption;
import dev.eagerlib.api.ChoiceOption;
import dev.eagerlib.api.ColorOption;
import dev.eagerlib.api.EagerLibPlugin;
import dev.eagerlib.api.EagerMod;
import dev.eagerlib.api.EagerRegistry;
import dev.eagerlib.api.KeyOption;
import dev.eagerlib.api.Section;
import dev.eagerlib.api.TextHudElement;

public final class KbSettings implements EagerLibPlugin {
	public enum Style {
		ROMAN("Roman (II)"), NUMBER("Number (2)"), PREFIXED("Prefixed (KB II)");

		final String label;

		Style(String label) { this.label = label; }
	}

	public enum Corner {
		TOP_LEFT("Top Left"), TOP_RIGHT("Top Right"), BOTTOM_LEFT("Bottom Left");

		final String label;

		Corner(String label) { this.label = label; }
	}

	public static BoolOption ENABLED;
	public static BoolOption BOOKS;
	public static KeyOption TOGGLE_KEY;
	public static ChoiceOption<Style> STYLE;
	public static ChoiceOption<Corner> CORNER;
	public static ColorOption COLOR;
	public static BoolOption SHADOW;

	@Override
	public void register(EagerRegistry registry) {
		EagerMod.Builder mod = registry.mod("knockbackdisplay")
			.name("Knockback Display")
			.description("Shows the Knockback level of your items right on the slot, so you always pick the right weapon.")
			.icon("assets/knockbackdisplay/icon.png")
			.changelog("1.1.0", "2026-11-26",
				"Settings now live in the EagerLib menu (Right Shift, or Mod Menu)",
				"Choose Roman numerals, numbers or a KB prefix",
				"Pick the corner, colour and shadow of the label",
				"New Held Knockback HUD element (enable it in Edit HUD)")
			.changelog("1.0.0", "2026-11-25",
				"Knockback level shown as a Roman numeral on items and enchanted books");

		Section general = mod.section("General");
		ENABLED = general.toggle("enabled", "Mod Enabled", true);
		BOOKS = general.toggle("books", "Enchanted Books", true).describe("Also label enchanted books that carry Knockback.");
		TOGGLE_KEY = general.keybind("toggle_key", "Toggle Labels", KeyOption.NONE);
		TOGGLE_KEY.onPress(() -> ENABLED.toggle());

		Section style = mod.section("Style");
		STYLE = style.choice("style", "Numerals", Style.ROMAN, s -> s.label);
		CORNER = style.choice("corner", "Position", Corner.TOP_LEFT, c -> c.label).describe("Which corner of the slot the label sits in.");
		COLOR = style.color("color", "Label Colour", 0xFFFFFF);
		SHADOW = style.toggle("shadow", "Text Shadow", true);

		mod.hud(new TextHudElement("held_knockback", "Held Knockback", () -> {
			int level = KnockbackLabel.heldLevel();
			return level > 0 ? "Knockback " + KnockbackLabel.numeral(level) : "";
		}, "Knockback II").color(() -> COLOR.get() | 0xFF000000).at(0.44F, 0.78F));

		mod.build();
	}
}
