package chanceCubes.items;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.gui.RewardSelectorPendantGui;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.registry.GiantCubeRegistry;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemSingleUseRewardSelectorPendant extends BaseChanceCubesItem
{

	public ItemSingleUseRewardSelectorPendant()
	{
		super((new Item.Properties()).maxStackSize(1), "single_use_reward_selector_pendant");
		super.addLore("Right click a Chance Cube to summon the reward.");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		player.setActiveHand(hand);
		ItemStack stack = player.getHeldItem(hand);
		if(player.isSneaking() && world.isRemote && player.isCreative())
			Minecraft.getInstance().displayGuiScreen(new RewardSelectorPendantGui(player, stack));
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public EnumActionResult onItemUse(ItemUseContext context)
	{
		if(context.isPlacerSneaking())
			return EnumActionResult.FAIL;
		if(context.getWorld().isRemote)
			return EnumActionResult.PASS;
		if(context.getItem().getTag() != null && context.getItem().getTag().hasKey("Reward"))
		{
			if(context.getWorld().getBlockState(context.getPos()).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
			{
				context.getWorld().setBlockState(context.getPos(), Blocks.AIR.getDefaultState());
				IChanceCubeReward reward = ChanceCubeRegistry.INSTANCE.getRewardByName(context.getItem().getTag().getString("Reward"));
				if(reward != null)
				{
					ChanceCubeRegistry.INSTANCE.triggerReward(reward, context.getWorld(), context.getPos(), context.getPlayer());
					context.getPlayer().setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
				}
				else
				{
					context.getPlayer().sendMessage(new TextComponentString("That reward does not exist for this cube!"));

				}
			}
			else if(context.getWorld().getBlockState(context.getPos()).getBlock().equals(CCubesBlocks.GIANT_CUBE))
			{
				TileEntity ent = context.getWorld().getTileEntity(context.getPos());
				if(ent == null || !(ent instanceof TileGiantCube))
					return EnumActionResult.FAIL;
				TileGiantCube giant = (TileGiantCube) ent;
				IChanceCubeReward reward = GiantCubeRegistry.INSTANCE.getRewardByName(context.getItem().getTag().getString("Reward"));
				if(reward != null)
				{
					GiantCubeRegistry.INSTANCE.triggerReward(reward, context.getWorld(), giant.getMasterPostion(), context.getPlayer());
					GiantCubeUtil.removeStructure(giant.getMasterPostion(), context.getWorld());
					context.getPlayer().setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
				}
				else
				{
					context.getPlayer().sendMessage(new TextComponentString("That reward does not exist for this cube!"));
				}

			}
		}
		return EnumActionResult.SUCCESS;
	}
}