package org.apache.core.m.ms.co;

import org.apache.core.u.IU;
import org.apache.core.m.s.IS;
import net.minecraft.item.Items;
import org.apache.core.e.e.PlayerTickListener;
import org.apache.core.m.C;
import org.apache.core.m.M;
import net.minecraft.text.Text;

import static org.apache.core.Client.MC;

public class AR extends M implements PlayerTickListener
{

	private final IS kitNum = IS.Builder.newInstance()
			.setName("kit number")
			.setDescription("the kit number you want to use")
			.setModule(this)
			.setValue(1)
			.setMin(1)
			.setMax(9)
			.setAvailability(() -> true)
			.build();

	private final IS minTotemCount = IS.Builder.newInstance()
			.setName("min Totem Count")
			.setDescription("when the amount of totems you have in your inventory is below this value, Auto Rekit will be triggered")
			.setModule(this)
			.setValue(1)
			.setMin(1)
			.setMax(36)
			.setAvailability(() -> true)
			.build();

	private final IS cooldown = IS.Builder.newInstance()
			.setName("cooldown")
			.setDescription("the cooldown for Auto Rekit (measured in ticks)")
			.setModule(this)
			.setValue(20)
			.setMin(0)
			.setMax(100)
			.setAvailability(() -> true)
			.build();

	private int cooldownClock = 0;

	public AR()
	{
		super("Auto Rekit", "automatically rekits when you run out of totems", false, C.COMBAT);
	}

	@Override
	public void onEnable()
	{
		super.onEnable();
		eventManager.add(PlayerTickListener.class, this);
		cooldownClock = 0;
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
		if (cooldownClock > 0)
		{
			cooldownClock--;
			return;
		}
		if (IU.countItem(Items.TOTEM_OF_UNDYING) >= minTotemCount.get())
			return;
		MC.player.sendMessage(Text.of("/k" + kitNum.get()));
		cooldownClock = cooldown.get();
	}
}
