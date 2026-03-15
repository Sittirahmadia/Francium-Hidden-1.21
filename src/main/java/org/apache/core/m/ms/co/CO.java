package org.apache.core.m.ms.co;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.core.m.C;
import org.apache.core.m.M;

public class CO extends M {

    public CO() {
        super("Crystal Optimizer", "haram client crystal remover", false, C.COMBAT);
    }

    public static boolean removeCrystal;
    public static boolean explodesClientSide(EndCrystalEntity crystal, DamageSource source, float amount) {
        if (!removeCrystal || !crystal.getWorld().isClient() || crystal.isRemoved() ||
                crystal.isInvulnerableTo(source) || source.getSource() instanceof EnderDragonEntity || amount <= 0) return false;

        // Hrukjang Studios Moment.
        if (source.getSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getSource();
            if (player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) <= 0) return false;
            amount = Math.min(amount, (float) player.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
            return amount > 0;
        }

        return true;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        removeCrystal = true;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        removeCrystal = false;
    }

}
