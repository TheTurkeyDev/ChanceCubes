package chanceCubes.items;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.ClientHelper;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.RewardsUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class ItemRewardSelectorPendant extends BaseChanceCubesItem
{

	public ItemRewardSelectorPendant()
	{
		super((new Item.Properties()).maxStackSize(1), "reward_selector_pendant");
		super.addLore("Shift right click to change the reward.");
		super.addLore("Right click a Chance Cube to summon the reward.");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		player.setActiveHand(hand);
		if(player.isSneaking() && world.isRemote && player.isCreative())
		{
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
			{
				ClientHelper.openRewardSelectorGUI(player, stack);
			});
		}
		return new ActionResult<>(ActionResultType.SUCCESS, stack);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context)
	{
		if(context.getWorld().isRemote)
			return ActionResultType.PASS;
		if(context.getPlayer() == null || context.getPlayer().isSneaking())
			return ActionResultType.FAIL;

		ServerWorld world = (ServerWorld) context.getWorld();

		if(context.getItem().getTag() != null && context.getItem().getTag().contains("Reward"))
		{
			if(world.getBlockState(context.getPos()).getBlock().equals(CCubesBlocks.CHANCE_CUBE))
			{
				world.setBlockState(context.getPos(), Blocks.AIR.getDefaultState());
				IChanceCubeReward reward = GlobalCCRewardRegistry.DEFAULT.getRewardByName(context.getItem().getTag().getString("Reward"));
				if(reward != null)
					GlobalCCRewardRegistry.DEFAULT.triggerReward(reward, world, context.getPos(), context.getPlayer());
				else
					RewardsUtil.sendMessageToPlayer(context.getPlayer(), "That reward does not exist for this cube!");
			}
			else if(world.getBlockState(context.getPos()).getBlock().equals(CCubesBlocks.GIANT_CUBE))
			{
				TileEntity ent = world.getTileEntity(context.getPos());
				if(!(ent instanceof TileGiantCube))
					return ActionResultType.FAIL;
				TileGiantCube giant = (TileGiantCube) ent;
				IChanceCubeReward reward = GlobalCCRewardRegistry.GIANT.getRewardByName(context.getItem().getTag().getString("Reward"));
				if(reward != null)
					GlobalCCRewardRegistry.GIANT.triggerReward(reward, world, giant.getMasterPostion(), context.getPlayer());
				else
					RewardsUtil.sendMessageToPlayer(context.getPlayer(), "That reward does not exist for this cube!");
				GiantCubeUtil.removeStructure(giant.getMasterPostion(), context.getWorld());
			}
		}
		return ActionResultType.SUCCESS;
	}
}