package org.apache.core.m.ms.co;

import org.apache.core.e.e.PlayerTickListener;
import org.apache.core.m.C;
import org.apache.core.m.M;
import org.apache.core.m.s.BS;
import org.apache.core.m.s.IS;
import org.apache.core.u.IU;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import static org.apache.core.Client.MC;

public class AP extends M implements PlayerTickListener {

    private final IS minHealth = IS.Builder.newInstance()
            .setName("Min Health")
            .setDescription("minimal hp to auto pot")
            .setModule(this)
            .setValue(10)
            .setMin(0)
            .setMax(20)
            .setAvailability(() -> true)
            .build();

    private final BS goToPrevSlot = BS.Builder.newInstance()
            .setName("Go To Prev Slot")
            .setDescription("go to prev slot after auto pot")
            .setModule(this)
            .setValue(false)
            .setAvailability(() -> true)
            .build();

    private final BS lookDown = BS.Builder.newInstance()
            .setName("Look Down")
            .setDescription("look down")
            .setModule(this)
            .setValue(true)
            .setAvailability(() -> true)
            .build();

    private final BS backToPrevPitch = BS.Builder.newInstance()
            .setName("Look To Prev Pitch")
            .setDescription("look to prev pitch")
            .setModule(this)
            .setValue(false)
            .setAvailability(() -> true)
            .build();

    private final IS delay = IS.Builder.newInstance()
            .setName("Delay")
            .setDescription("delay")
            .setModule(this)
            .setValue(1)
            .setMin(0)
            .setMax(10)
            .setAvailability(() -> true)
            .build();

    int clock;

    public AP() {
        super("Auto Pot", "auto pot", false, C.COMBAT);
    }

    @Override
    public void onEnable() {
        this.eventManager.add(PlayerTickListener.class, this);
        this.clock = 0;
    }

    @Override
    public void onDisable() {
        this.eventManager.remove(PlayerTickListener.class, this);
    }

    @Override
    public void onPlayerTick() {

        if (GLFW.glfwGetMouseButton(MC.getWindow().getHandle(), 1) == 1) {
            if (this.clock != 0) {
                --this.clock;
                return;
            }

            int slot = IU.findSplashHealthPotion();
            ItemStack mainHandStack = MC.player.getMainHandStack();


            if (slot != -1 && MC.currentScreen == null
                    && MC.player.getHealth() <= (float) this.minHealth.get()
                    && (mainHandStack.isOf(Items.NETHERITE_SWORD)
                        || mainHandStack.isOf(Items.DIAMOND_SWORD)
                        || mainHandStack.isOf(Items.IRON_SWORD)
                        || mainHandStack.isOf(Items.AIR)
                        || IU.isSplashHealthPotion(mainHandStack))) {
                if (this.clock != 0) {
                    return;
                }

                PlayerInventory inventory = MC.player.getInventory();
                int prevSlot = inventory.selectedSlot;
                this.clock = this.delay.get();
                float prevPitch = MC.player.getPitch();
                inventory.selectedSlot = slot;

                if (this.lookDown.get()) {
                    MC.player.setPitch(90.0F);
                }

                MC.interactionManager.interactItem(MC.player, Hand.MAIN_HAND);

                if (this.backToPrevPitch.get()) {
                    MC.player.setPitch(prevPitch);
                }

                if (this.goToPrevSlot.get()) {
                    inventory.selectedSlot = prevSlot;
                }
            }
        }
    }

}