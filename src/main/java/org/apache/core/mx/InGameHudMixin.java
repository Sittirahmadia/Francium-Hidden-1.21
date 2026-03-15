package org.apache.core.mx;

import org.apache.core.e.EM;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.apache.core.e.e.RenderHudListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin
{
	@Inject(method = "render", at = @At("TAIL"))
	private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
		RenderHudListener.RenderHudEvent event = new RenderHudListener.RenderHudEvent(context.getMatrices(), tickDelta);
		EM.fire(event);
	}
}
