package org.apache.core.m.ms.co;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.apache.core.e.EM;
import org.apache.core.e.e.PlayerTickListener;
import org.apache.core.m.C;
import org.apache.core.m.M;
import org.apache.core.m.s.BS;
import org.apache.core.m.s.DS;

import static org.apache.core.Client.MC;

public class TB extends M implements PlayerTickListener
{

	private final DS cooldown = DS.Builder.newInstance()
			.setName("cooldown")
			.setDescription("the cooldown threshold")
			.setModule(this)
			.setValue(1)
			.setMin(0)
			.setMax(1)
			.setStep(0.1)
			.setAvailability(() -> true)
			.build();

	private final BS attackInAir = BS.Builder.newInstance()
			.setName("attackInAir")
			.setDescription("whether or not to attack in mid air")
			.setModule(this)
			.setValue(true)
			.setAvailability(() -> true)
			.build();

	private final BS attackOnJump = BS.Builder.newInstance()
			.setName("attackOnJump")
			.setDescription("whether or not to attack when going upwards")
			.setModule(this)
			.setValue(true)
			.setAvailability(attackInAir::get)
			.build();

	public TB()
	{
		super("TriggerBot", "automatically attacks players for you", false, C.COMBAT);
	}

	@Override
	public void onEnable()
	{
		super.onEnable();
		eventManager.add(PlayerTickListener.class, this);
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
		eventManager.remove(PlayerTickListener.class, this);
	}

	@Override
	public void onPlayerTick()
	{
		if (MC.player.isUsingItem())
			return;
		if (!(MC.player.getMainHandStack().getItem() instanceof SwordItem))
			return;
		HitResult hit = MC.crosshairTarget;
		if (hit.getType() != HitResult.Type.ENTITY)
			return;
		if (MC.player.getAttackCooldownProgress(0) < cooldown.get())
			return;
		Entity target = ((EntityHitResult) hit).getEntity();
		if (!(target instanceof PlayerEntity))
			return;
		if (!target.isOnGround() && !attackInAir.get())
			return;
		if (MC.player.getY() > MC.player.prevY && !attackOnJump.get())
			return;
		MC.interactionManager.attackEntity(MC.player, target);
		MC.player.swingHand(Hand.MAIN_HAND);
	}
}