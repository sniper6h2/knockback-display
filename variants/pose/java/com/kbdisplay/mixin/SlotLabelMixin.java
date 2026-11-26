package com.kbdisplay.mixin;

import com.kbdisplay.KnockbackLabel;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Draws the Knockback level in the top-left corner of any item slot
 * (hotbar, inventory, containers), next to the vanilla stack count.
 * On this version the GUI is still 3D, so the text is lifted to z=200
 * (the same depth vanilla uses for the stack count) to sit above the item.
 */
@Mixin(GuiGraphics.class)
public abstract class SlotLabelMixin {

	@Inject(
		method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
		at = @At("TAIL")
	)
	private void knockbackdisplay$drawLevel(Font font, ItemStack stack, int x, int y, String countText, CallbackInfo ci) {
		String label = KnockbackLabel.forStack(stack);
		if (label != null) {
			GuiGraphics self = (GuiGraphics) (Object) this;
			self.pose().pushPose();
			self.pose().translate(0.0F, 0.0F, 200.0F);
			// White text with shadow, same style as the vanilla stack count.
			self.drawString(font, label, KnockbackLabel.x(font, label, x), KnockbackLabel.y(y), KnockbackLabel.color(), KnockbackLabel.shadow());
			self.pose().popPose();
		}
	}
}
