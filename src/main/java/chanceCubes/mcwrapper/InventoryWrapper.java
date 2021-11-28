package chanceCubes.mcwrapper;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Inventory;

public class InventoryWrapper
{

	public static void copyInvAToB(Inventory a, Inventory b)
	{
		// Ew?
		b.load(a.save(new ListTag()));
	}
}
