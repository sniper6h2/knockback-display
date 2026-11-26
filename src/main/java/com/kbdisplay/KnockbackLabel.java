package com.kbdisplay;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class KnockbackLabel {
	private static final String[] NUMERALS = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

	private KnockbackLabel() {}

	public static String forStack(ItemStack stack) {
		if (KbSettings.ENABLED != null && !KbSettings.ENABLED.get()) return null;
		int level = level(stack.get(DataComponents.ENCHANTMENTS));
		if (level <= 0 && (KbSettings.BOOKS == null || KbSettings.BOOKS.get())) {
			level = level(stack.get(DataComponents.STORED_ENCHANTMENTS));
		}
		if (level <= 0) return null;
		KbSettings.Style style = KbSettings.STYLE == null ? KbSettings.Style.ROMAN : KbSettings.STYLE.get();
		return switch (style) {
			case NUMBER -> Integer.toString(level);
			case PREFIXED -> "KB" + roman(level);
			default -> roman(level);
		};
	}

	public static String numeral(int level) {
		return KbSettings.STYLE != null && KbSettings.STYLE.get() == KbSettings.Style.NUMBER ? Integer.toString(level) : roman(level);
	}

	private static String roman(int level) {
		return level < NUMERALS.length ? NUMERALS[level] : Integer.toString(level);
	}

	public static int heldLevel() {
		Player player = Minecraft.getInstance().player;
		return player == null ? 0 : level(player.getMainHandItem().get(DataComponents.ENCHANTMENTS));
	}

	public static int x(Font font, String label, int x) {
		return KbSettings.CORNER != null && KbSettings.CORNER.get() == KbSettings.Corner.TOP_RIGHT ? x + 17 - font.width(label) : x;
	}

	public static int y(int y) {
		return KbSettings.CORNER != null && KbSettings.CORNER.get() == KbSettings.Corner.BOTTOM_LEFT ? y + 9 : y;
	}

	public static int color() {
		return KbSettings.COLOR == null ? 0xFFFFFFFF : KbSettings.COLOR.get() | 0xFF000000;
	}

	public static boolean shadow() {
		return KbSettings.SHADOW == null || KbSettings.SHADOW.get();
	}

	private static int level(ItemEnchantments enchantments) {
		if (enchantments == null || enchantments.isEmpty()) return 0;
		for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
			if (entry.getKey().is(Enchantments.KNOCKBACK)) {
				return entry.getIntValue();
			}
		}
		return 0;
	}
}
