package org.apache.core.m.ms.co;

import org.apache.core.u.BU;
import org.apache.core.u.IU;
import org.apache.core.e.e.KeyPressListener;
import org.apache.core.e.e.PlayerTickListener;
import org.apache.core.k.K;
import org.apache.core.m.C;
import org.apache.core.m.M;
import org.apache.core.m.s.IS;
import org.apache.core.m.s.KS;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

import static org.apache.core.Client.MC;

public class AMC extends M implements PlayerTickListener, KeyPressListener {

    public AMC() {
        super("Auto MineCart", "auto minecart bruh", false, C.COMBAT);
    }

    private IS bowCharge = IS.Builder.newInstance()
            .setName("Bow Charge")
            .setDescription("interval")
            .setModule(this)
            .setValue(5)
            .setMin(5)
            .setMax(20)
            .setAvailability(() -> true)
            .build();

    private IS shootInterval = IS.Builder.newInstance()
            .setName("Shoot Interval")
            .setDescription("interval")
            .setModule(this)
            .setValue(0)
            .setMin(0)
            .setMax(5)
            .setAvailability(() -> true)
            .build();

    private IS placeMinecartInterval = IS.Builder.newInstance()
            .setName("Place Minecart Interval")
            .setDescription("interval")
            .setModule(this)
            .setValue(0)
            .setMin(0)
            .setMax(5)
            .setAvailability(() -> true)
            .build();

    public final KS activateKey = new KS.Builder()
            .setName("Activate Key")
            .setDescription("the key to activate it")
            .setModule(this)
            .setValue(new K("", GLFW.GLFW_KEY_C,false,false,null))
            .build();

    private int minecartPlaceClock;
    private int shootDelay;
    private int cnt;

    private boolean isThatRails;
    private boolean needToPlaceRails;
    private boolean shooted;
    private boolean needToShoot;
    private boolean needToPlaceMinecart;

    @Override
    public void onEnable() {
        super.onEnable();
        eventManager.add(PlayerTickListener.class, this);
        eventManager.add(KeyPressListener.class, this);

        minecartPlaceClock = 0;
        shootDelay = 0;
        cnt = 0;

        isThatRails = false;
        needToPlaceMinecart = false;
        needToPlaceRails = false;
        needToShoot = false;
        shooted = false;
    }

    private boolean check() {
        return isThatRails || needToPlaceMinecart || needToPlaceRails || needToShoot || shooted;
    }

    private boolean checkHotBar() {
        return IU.hasItemInHotbar(Items.TNT_MINECART) && IU.hasItemInHotbar(Items.RAIL)
                && IU.hasItemInHotbar(Items.BOW);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        eventManager.remove(PlayerTickListener.class, this);
        eventManager.remove(KeyPressListener.class, this);
    }

    @Override
    public void onPlayerTick() {

        if (GLFW.glfwGetKey(MC.getWindow().getHandle(), activateKey.get().getKey()) != GLFW.GLFW_PRESS) {
            isThatRails = false;
            needToPlaceMinecart = false;
            needToPlaceRails = false;
            needToShoot = false;
            shooted = false;
            cnt = 0;
            return;
        }

        if (cnt == 0) {
            if (needToPlaceRails) {

                if (MC.crosshairTarget instanceof BlockHitResult hit
                        && !BU.isBlock(Blocks.AIR, hit.getBlockPos())) {
                    IU.selectItemFromHotbar(Items.RAIL);
                    ActionResult result = MC.interactionManager.interactBlock(MC.player, Hand.MAIN_HAND, hit);
                    if (result.isAccepted() && result.shouldSwingHand()) MC.player.swingHand(Hand.MAIN_HAND);
                }

                needToPlaceMinecart = false;
                needToPlaceRails = false;
                needToShoot = false;
                shooted = false;
                isThatRails = true;
            }

            if (isThatRails && shootDelay != shootInterval.get()) {
                shootDelay++;
                return;
            } else if (isThatRails) {
                shootDelay = 0;
                isThatRails = false;
                needToPlaceMinecart = false;
                needToPlaceRails = false;
                shooted = false;
                needToShoot = true;
            }

            if (needToShoot) {

                IU.selectItemFromHotbar(Items.BOW);

                if (MC.player.getItemUseTime() >= bowCharge.get()) {
                    MC.player.stopUsingItem();
                    MC.interactionManager.stopUsingItem(MC.player);
                    MC.options.useKey.setPressed(false);

                    isThatRails = false;
                    needToPlaceMinecart = false;
                    needToPlaceRails = false;
                    needToShoot = false;
                    shooted = true;
                } else {
                    MC.options.useKey.setPressed(true);
                }

            }

            if (shooted && minecartPlaceClock != placeMinecartInterval.get()) {
                minecartPlaceClock++;
                return;
            } else if (shooted) {
                minecartPlaceClock = 0;
                isThatRails = false;
                needToPlaceRails = false;
                needToShoot = false;
                shooted = false;
                needToPlaceMinecart = true;
            }

            if (needToPlaceMinecart) {

                if (MC.crosshairTarget instanceof BlockHitResult hit
                        && BU.isBlock(Blocks.RAIL, hit.getBlockPos())) {
                    IU.selectItemFromHotbar(Items.TNT_MINECART);
                    ActionResult result = MC.interactionManager.interactBlock(MC.player, Hand.MAIN_HAND, hit);
                    if (result.isAccepted() && result.shouldSwingHand())
                        MC.player.swingHand(Hand.MAIN_HAND);

                }

                isThatRails = false;
                needToPlaceMinecart = false;
                needToPlaceRails = false;
                needToShoot = false;
                shooted = false;
                cnt++;
            }
        }

    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getKeyCode() == activateKey.get().getKey() && !check() && checkHotBar()) {
            if (MC.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                BlockHitResult hit = (BlockHitResult) MC.crosshairTarget;
                if (BU.isBlock(Blocks.RAIL, hit.getBlockPos())) {
                    isThatRails = true;
                } else if (!BU.isBlock(Blocks.AIR, hit.getBlockPos())) {
                    needToPlaceRails = true;
                }
            }
        }
    }
}
