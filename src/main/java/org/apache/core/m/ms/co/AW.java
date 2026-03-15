package org.apache.core.m.ms.co;

import org.apache.core.e.e.AttackEntityListener;
import org.apache.core.e.e.PlayerTickListener;
import org.apache.core.m.C;
import org.apache.core.m.M;
import org.apache.core.m.s.IS;

import static org.apache.core.Client.MC;

public class AW extends M implements AttackEntityListener, PlayerTickListener {

    private IS delay = IS.Builder.newInstance()
            .setName("Delay")
            .setDescription("delay in ticks")
            .setModule(this)
            .setValue(0)
            .setMin(0)
            .setMax(10)
            .setAvailability(() -> true)
            .build();

    private int delayClock = 0;
    private boolean reset = false;

    public AW() {
        super("Auto WTap", "automaticly reset sprint", false, C.COMBAT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        eventManager.add(AttackEntityListener.class, this);
        eventManager.add(PlayerTickListener.class, this);

        reset = false;
        delayClock = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        eventManager.remove(AttackEntityListener.class, this);
        eventManager.remove(PlayerTickListener.class, this);;
    }

    @Override
    public void onPlayerTick() {
        if (reset && delayClock != delay.get())
            delayClock++;
        else if (reset) {
            MC.options.sprintKey.setPressed(true);
            reset = false;
            delayClock = 0;
        }
    }

    @Override
    public void onAttackEntity(AttackEntityEvent event) {
        if (MC.player.isSprinting()) {
            MC.options.sprintKey.setPressed(false);
            reset = true;
        }
    }
}
