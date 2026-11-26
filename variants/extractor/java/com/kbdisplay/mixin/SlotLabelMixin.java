package com.kbdisplay.mixin;

import com.kbdisplay.KnockbackLabel;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the Knockback level in the top-left corner of any item slot
 * (hotbar, inventory, containers), next to the vanilla stack count.
 */
@Mixin(GuiGraphicsExtractor.class)
public abstract class SlotLabelMixin {

	@Inject(
		method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;itemCount(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
			shift = At.Shift.AFTER
		)
	)
	private void knockbackdisplay$drawLevel(Font font, ItemStack stack, int x, int y, String countText, CallbackInfo ci) {
		String label = KnockbackLabel.forStack(stack);
		if (label != null) {
			// White text with shadow, same style as the vanilla stack count.
			((GuiGraphicsExtractor) (Object) this).text(font, label, KnockbackLabel.x(font, label, x), KnockbackLabel.y(y), KnockbackLabel.color(), KnockbackLabel.shadow());
		}
	}
}
