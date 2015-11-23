package chanceCubes.hookins.mods;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.rewards.defaultRewards.BasicReward;
import chanceCubes.rewards.rewardparts.ItemPart;
import chanceCubes.rewards.rewardparts.OffsetBlock;
import chanceCubes.rewards.type.BlockRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import cpw.mods.fml.common.registry.GameRegistry;

public class ExtraUtilsModHook extends BaseModHook
{

	public ExtraUtilsModHook()
	{
		super("ExtraUtilities");
		loadRewards();
	}

	@Override
	public void loadRewards()
	{
		ItemStack stack;

		if(GameRegistry.findItem(super.modId, "unstableingot") != null)
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":unstableingot", -100, new ItemRewardType(new ItemPart(GameRegistry.findItemStack(super.modId, "unstableingot", 1)))
			{
				@Override
				public void trigger(ItemPart s, World world, int x, int y, int z, EntityPlayer player)
				{
					ItemStack stack1 = s.getItemStack().copy();
					NBTTagCompound ts = new NBTTagCompound();
					if(ts.hasKey("crafting"))
					{
						ts.removeTag("crafting");
					}
					if(stack1.getItemDamage() > 0)
					{
						return;
					}
					ts.setInteger("dimension", world.provider.dimensionId);
					ts.setLong("time", world.getTotalWorldTime());
					stack1.setTagCompound(ts);

					world.setBlock((int) player.posX, (int) player.posY, (int) player.posZ, Blocks.crafting_table);
					player.displayGUIWorkbench((int) player.posX, (int) player.posY, (int) player.posZ);
					player.inventory.setInventorySlotContents(player.inventory.currentItem, stack1);
					if((player instanceof EntityPlayerMP))
					{
						((EntityPlayerMP) player).mcServer.getConfigurationManager().syncPlayerInventory((EntityPlayerMP) player);
					}
				}
			}));
		}

		if(GameRegistry.findItem(super.modId, "generator.64") != null)
		{
			stack = GameRegistry.findItemStack(super.modId, "generator.64", 1);
			stack.setStackDisplayName("Useless Generator");
			stack.setItemDamage(9);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Pink_Generator", 30, new ItemRewardType(new ItemPart(stack))));
		}

		if(GameRegistry.findItem(super.modId, "watering_can") != null)
		{
			stack = GameRegistry.findItemStack(super.modId, "watering_can", 1);
			stack.setItemDamage(2);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Broken_Watering_Can", 15, new ItemRewardType(new ItemPart(stack))));
		}

		if(GameRegistry.findItem(super.modId, "drum") != null)
		{
			stack = GameRegistry.findItemStack(super.modId, "drum", 1);
			stack.setItemDamage(1);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Bedrockium_Drum", 65, new ItemRewardType(new ItemPart(stack))));
		}

		if(GameRegistry.findItem(super.modId, "golden_bag") != null)
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Golden_Bag", 90, new ItemRewardType(new ItemPart(GameRegistry.findItemStack(super.modId, "golden_bag", 1)))));
		}

		if(GameRegistry.findItem(super.modId, "plant/ender_lilly") != null)
		{
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Ender_Lilly", 45, new ItemRewardType(new ItemPart(GameRegistry.findItemStack(super.modId, "plant/ender_lilly", 3)))));
		}

		if(GameRegistry.findItem(super.modId, "endConstructor") != null)
		{
			stack = GameRegistry.findItemStack(super.modId, "endConstructor", 10);
			stack.setItemDamage(2);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Ender_Flux_Crystal", 80, new ItemRewardType(new ItemPart(stack))));
		}

		if(GameRegistry.findItem(super.modId, "cobblestone_compressed") != null)
		{
			stack = GameRegistry.findItemStack(super.modId, "cobblestone_compressed", 4);
			stack.setItemDamage(5);
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Compressed_Cobble", 20, new ItemRewardType(new ItemPart(stack))));
		}

		if(GameRegistry.findBlock(super.modId, "spike_base") != null)
		{
			OffsetBlock[] spikes = new OffsetBlock[50];
			for(int x = 0; x < 5; x++)
			{
				for(int z = 0; z < 5; z++)
				{
					spikes[x * 5 + z] = new OffsetBlock(x - 2, -1, z - 2, GameRegistry.findBlock(super.modId, "spike_base"), false).setRelativeToPlayer(true);
					spikes[x * 5 + z].setData((byte) 1);
					spikes[25 + (x * 5 + z)] = new OffsetBlock(x - 2, 2, z - 2, GameRegistry.findBlock(super.modId, "spike_base"), false).setRelativeToPlayer(true);

				}
			}
			ChanceCubeRegistry.INSTANCE.registerReward(new BasicReward(this.modId + ":Spikes", -40, new BlockRewardType(spikes)));
		}
	}
}
