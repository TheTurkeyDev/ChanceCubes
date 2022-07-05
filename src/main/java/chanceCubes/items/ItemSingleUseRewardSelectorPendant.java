package chanceCubes.items;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.ClientHelper;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.tileentities.TileGiantCube;
import chanceCubes.util.GiantCubeUtil;
import chanceCubes.util.RewardsUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class ItemSingleUseRewardSelectorPendant extends BaseChanceCubesItem
{

	public ItemSingleUseRewardSelectorPendant()
	{
		super((new Item.Properties()).stacksTo(1));
		super.addLore("Right click a Chance Cube to summon the reward.");
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack stack = player.getItemInHand(hand);
		if(player.isCrouching() && level.isClientSide() && player.isCreative())
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHelper.openRewardSelectorGUI(player, stack));
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		if(context.getPlayer() == null || context.getPlayer().isCrouching())
			return InteractionResult.FAIL;
		if(context.getLevel().isClientSide())
			return InteractionResult.PASS;

		ServerLevel level = (ServerLevel) context.getLevel();

		if(context.getItemInHand().getTag() != null && context.getItemInHand().getTag().contains("Reward"))
		{
			Player player = context.getPlayer();
			if(player != null)
			{
				if(level.getBlockState(context.getClickedPos()).getBlock().equals(CCubesBlocks.CHANCE_CUBE.get()))
				{
					level.setBlockAndUpdate(context.getClickedPos(), Blocks.AIR.defaultBlockState());
					IChanceCubeReward reward = GlobalCCRewardRegistry.DEFAULT.getRewardByName(context.getItemInHand().getTag().getString("Reward"));
					if(reward != null)
					{
						GlobalCCRewardRegistry.triggerReward(reward, level, context.getClickedPos(), context.getPlayer());
						player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
					}
					else
					{
						RewardsUtil.sendMessageToPlayer(player, "That reward does not exist for this cube!");
					}
				}
				else if(level.getBlockState(context.getClickedPos()).getBlock().equals(CCubesBlocks.GIANT_CUBE.get()))
				{
					BlockEntity ent = level.getBlockEntity(context.getClickedPos());
					if(!(ent instanceof TileGiantCube giant))
						return InteractionResult.FAIL;

					IChanceCubeReward reward = GlobalCCRewardRegistry.GIANT.getRewardByName(context.getItemInHand().getTag().getString("Reward"));
					if(reward != null)
					{
						GlobalCCRewardRegistry.triggerReward(reward, level, giant.getMasterPostion(), context.getPlayer());
						GiantCubeUtil.removeStructure(giant.getMasterPostion(), level);
						player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
					}
					else
					{
						RewardsUtil.sendMessageToPlayer(player, "That reward does not exist for this cube!");
					}
				}
			}
		}
		return InteractionResult.SUCCESS;
	}
}