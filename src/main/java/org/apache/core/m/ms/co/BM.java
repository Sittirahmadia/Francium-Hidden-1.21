package org.apache.core.m.ms.co;

import net.minecraft.client.MinecraftClient;
import org.apache.core.e.e.ItemUseListener;
import org.apache.core.e.e.PlayerTickListener;
import org.apache.core.m.C;
import org.apache.core.m.M;
import org.apache.core.m.s.BS;
import org.apache.core.m.s.IS;
import net.minecraft.block.BedBlock;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BedItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.apache.core.mx.MinecraftClientAccessor;

import static org.apache.core.Client.MC;

public class BM extends M implements PlayerTickListener, ItemUseListener {

    private final BS checkDimensions = BS.Builder.newInstance()
            .setName("Check Dimensions")
            .setDescription("check dimensions")
            .setModule(this)
            .setValue(true)
            .setAvailability(() -> true)
            .build();

    private final IS explodeDelay = IS.Builder.newInstance()
            .setName("Explode Delay")
            .setDescription("delay after placing bed")
            .setModule(this)
            .setValue(0)
            .setMin(0)
            .setMax(20)
            .setAvailability(() -> true)
            .build();

    private final IS switchDelay = IS.Builder.newInstance()
            .setName("Switch Delay")
            .setDescription("delay after breaking bed")
            .setModule(this)
            .setValue(0)
            .setMin(0)
            .setMax(20)
            .setAvailability(() -> true)
            .build();

    enum BedStatus {
        IDLE,
        SWITCHED,
        PLACED
    }

    private BedStatus bedStatus = BedStatus.IDLE;
    private int placeClock = 0;
    private int breakClock = 0;
    private int switchClock = 0;

    public BM() {
        super("Bed Macro", "auto bed explode", false, C.COMBAT);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        eventManager.add(PlayerTickListener.class, this);
        eventManager.add(ItemUseListener.class, this);

        bedStatus = BedStatus.IDLE;
        placeClock = 0;
        breakClock = 0;
        switchClock = 0;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        eventManager.remove(PlayerTickListener.class, this);
        eventManager.remove(ItemUseListener.class, this);
    }

    public static boolean hasBedInInv() {
        PlayerInventory inv = MC.player.getInventory();

        for (int i = 9; i < 36; i++) {
            if (inv.getStack(i).getItem() instanceof BedItem)
                return true;
        }
        return false;
    }

    public static boolean hasBedInHotbar() {
        PlayerInventory inv = MC.player.getInventory();

        for (int i = 0; i < 9; i++) {
            if (inv.getStack(i).getItem() instanceof BedItem)
                return true;
        }
        return false;
    }

    public static void selectBedFromHotbar() {
        PlayerInventory inv = MC.player.getInventory();

        for (int i = 0; i < 9; i++) {
            if (inv.getStack(i).getItem() instanceof BedItem) {
                inv.selectedSlot = i;
                return;
            }
        }
    }

    @Override
    public void onPlayerTick() {

        if (checkDimensions.get()) {
            if (!(MC.player.clientWorld.getDimensionEffects() instanceof DimensionEffects.End
                    || MC.player.clientWorld.getDimensionEffects() instanceof DimensionEffects.Nether))
                return;
        }

        if (MC.player.isSneaking())
            return;

        if (bedStatus == BedStatus.PLACED) {

            boolean bedInHotbar = hasBedInHotbar();
            boolean bedInInv = hasBedInInv();

            if (bedInHotbar || bedInInv) {
                if (switchClock != switchDelay.get()) {
                    switchClock++;
                    return;
                }

                switchClock = 0;
            }

            if (bedInHotbar) {
                selectBedFromHotbar();
            } else if (bedInInv) {
                if (MC.crosshairTarget instanceof BlockHitResult hit) {
                    if (MC.world.getBlockState(hit.getBlockPos()).getBlock() instanceof BedBlock) {
                        ((MinecraftClientAccessor) MinecraftClient.getInstance()).middleClick();
                    }
                }
            } else {
                bedStatus = BedStatus.SWITCHED;
            }

            if (MC.player.getMainHandStack().getItem() instanceof BedItem)
                bedStatus = BedStatus.SWITCHED;

        }

        if (bedStatus == BedStatus.SWITCHED) {
            if (MC.crosshairTarget instanceof BlockHitResult hit) {
                if (MC.world.getBlockState(hit.getBlockPos()).getBlock() instanceof BedBlock) {
                    if (breakClock != explodeDelay.get()) {
                        breakClock++;
                        return;
                    }

                    breakClock = 0;

                    MC.interactionManager.interactBlock(MC.player, Hand.MAIN_HAND, ((BlockHitResult) MC.crosshairTarget));
                    MC.player.swingHand(Hand.MAIN_HAND);
                    bedStatus = BedStatus.IDLE;
                }
            }
        }

    }

    @Override
    public void onItemUse(ItemUseEvent event) {
        if (bedStatus != BedStatus.PLACED)
            if (MC.player.getMainHandStack().getItem() instanceof BedItem)
                if (MC.crosshairTarget instanceof BlockHitResult hit)
                    if (!(MC.world.getBlockState(hit.getBlockPos()).getBlock() instanceof BedBlock))
                        bedStatus = BedStatus.PLACED;
    }
}
