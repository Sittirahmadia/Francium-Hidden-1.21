package org.apache.core.mx;

import org.apache.core.m.ms.r.NHC;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Inject(at = @At("HEAD"), method = "bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", cancellable = true)
	private void bobViewWhenHurt(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
		if (!NHC.doHurtCam) {
			ci.cancel();
		}
	}
}
