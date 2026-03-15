package org.apache.core.u;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

import static org.apache.core.Client.MC;

public enum IU
{
	;

	public static boolean selectItemFromHotbar(Predicate<Item> item)
	{
		PlayerInventory inv = MC.player.getInventory();

		for (int i = 0; i < 9; i++)
		{
			ItemStack itemStack = inv.getStack(i);
			if (!item.test(itemStack.getItem()))
				continue;
			inv.selectedSlot = i;
			return true;
		}

		return false;
	}

	public static boolean selectItemFromHotbar(Item item)
	{
		return selectItemFromHotbar(i -> i == item);
	}

	public static boolean hasItemInHotbar(Item item)
	{
		PlayerInventory inv = MC.player.getInventory();

		for (int i = 0; i < 9; i++)
		{
			ItemStack itemStack = inv.getStack(i);
			if (item.equals(itemStack.getItem()))
				return true;
		}
		return false;
	}

	public static int countItem(Predicate<Item> item)
	{
		PlayerInventory inv = MC.player.getInventory();

		int count = 0;

		for (int i = 0; i < 36; i++)
		{
			ItemStack itemStack = inv.getStack(i);
			if (item.test(itemStack.getItem()))
				count += itemStack.getCount();
		}

		return count;
	}

	public static int countItem(Item item)
	{
		return countItem(i -> i == item);
	}

	public static int findSplashHealthPotion() {
		PlayerInventory inv = MC.player.getInventory();
		for (int i = 0; i < 9; i++) {
			ItemStack stack = inv.getStack(i);
			if (isSplashHealthPotion(stack)) return i;
		}
		return -1;
	}

	public static boolean isSplashHealthPotion(ItemStack stack) {
		if (!stack.isOf(Items.SPLASH_POTION)) return false;
		PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
		if (contents == null) return false;
		for (net.minecraft.entity.effect.StatusEffectInstance e : contents.getEffects()) {
			if (e.getEffectType().value() == StatusEffects.INSTANT_HEALTH.value()) return true;
		}
		return false;
	}

}