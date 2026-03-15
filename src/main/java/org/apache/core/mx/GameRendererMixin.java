package org.apache.core.mx;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
    // bobViewWhenHurt removed in 1.21
}
