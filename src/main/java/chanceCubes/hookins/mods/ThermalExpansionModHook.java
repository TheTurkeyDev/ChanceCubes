package chanceCubes.hookins.mods;

import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.MessagePart;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ThermalExpansionModHook extends BaseModHook
{
	
	public ThermalExpansionModHook()
	{
		super("ThermalExpansion");
		loadRewards();
	}

	@Override
	public void loadRewards()
	{
		ItemStack stack;

		if(GameRegistry.findItem(super.modId, "florb") != null)
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":florbs", 60, new MessageRewardType(new MessagePart("Florbs!!")), new ItemRewardType(new ItemPart(GameRegistry.findItemStack(super.modId, "florb", 2)))
			{
				@Override
				public void trigger(ItemPart s, World world, int x, int y, int z, EntityPlayer player)
				{
					NBTTagCompound nbt;
					for(int i = 0; i < 5; i++)
					{
						nbt = new NBTTagCompound();
						nbt.setString("Fluid", ChanceCubeRegistry.getRandomFluid().getName());
						s.getItemStack().stackTagCompound = nbt;
						super.spawnStack(s, world, x, y, z, player);
					}
				}
			}));
		}

		if(GameRegistry.findItem(super.modId, "pump") != null)
		{
			stack = GameRegistry.findItemStack(super.modId, "pump", 1);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":flui-vac", 60, new MessageRewardType(new MessagePart("Well this sucks.....")), new ItemRewardType(new ItemPart(stack))));
		}

		if(GameRegistry.findItem(super.modId, "igniter") != null && GameRegistry.findItem(super.modId, "chiller") != null)
		{
			stack = GameRegistry.findItemStack(super.modId, "igniter", 1);
			stack.setStackDisplayName("Flint and Steel 2.0");
			ItemStack stack2 = GameRegistry.findItemStack(super.modId, "chiller", 1);
			stack2.setStackDisplayName("Snowman in a wand");
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Flux_Chill&Ignite", 30, new MessageRewardType(new MessagePart("Fire and Ice")), new ItemRewardType(new ItemPart(stack), new ItemPart(stack2))));
		}
		
		if(GameRegistry.findItem(super.modId, "capacitor") != null)
		{
			stack = GameRegistry.findItemStack(super.modId, "capacitor", 1);
			stack.setItemDamage(1);
			NBTTagCompound nbt = stack.stackTagCompound;
			if(nbt == null)
				nbt = new NBTTagCompound();
			nbt.setInteger("Energy", 32000);
			stack.stackTagCompound = nbt;
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Tuberous_Capacitor", 25, new MessageRewardType(new MessagePart("It's a potato!!")), new ItemRewardType(new ItemPart(stack))));
		}
	}
}
