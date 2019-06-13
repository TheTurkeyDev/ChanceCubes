package chanceCubes.rewards.giantRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import chanceCubes.network.CCubesPacketHandler;
import chanceCubes.network.PacketParticle;
import chanceCubes.rewards.defaultRewards.BaseCustomReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;

public class BlockThrowerReward extends BaseCustomReward
{
	public BlockThrowerReward()
	{
		super(CCubesCore.MODID + ":Block_Thrower", 0);
	}
	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		for(int x = -20; x < 21; x++)
		{
			for(int z = -20; z < 21; z++)
			{
				if(!world.isAirBlock(pos.add(x, 11, z)))
					for(int y = -1; y < 12; y++)
						world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
			}
		}

		Scheduler.scheduleTask(new Task("Throw_Block", 450, 2)
		{
			private List<EntityFallingBlock> blocks = new ArrayList<EntityFallingBlock>();

			@Override
			public void update()
			{
				if(this.delayLeft > 100)
				{
					int x = RewardsUtil.rand.nextInt(41) - 21;
					int z = RewardsUtil.rand.nextInt(41) - 21;
					int y = -2;
					for(y = 12; y > -2; y--)
						if(!world.isAirBlock(pos.add(x, y, z)))
							break;
					BlockPos newPos = pos.add(x, y, z);
					IBlockState state = world.getBlockState(newPos);

					if(CCubesSettings.nonReplaceableBlocks.contains(state) || state.getBlock().equals(Blocks.AIR) || state.getFluidState().getFluid() == null)
						state = Blocks.DIRT.getDefaultState();
					else
						world.setBlockState(newPos, Blocks.AIR.getDefaultState());

					EntityFallingBlock block = new EntityFallingBlock(world, newPos.getX() + 0.5, newPos.getY(), newPos.getZ() + 0.5, state);
					block.fallTime = 1;
					block.setNoGravity(true);
					block.motionX = 0f;
					block.motionY = 0.25f;
					block.motionZ = 0f;

					world.spawnEntity(block);
					blocks.add(block);
				}

				for(EntityFallingBlock b : blocks)
				{
					if(b.posY > pos.getY() + 8)
					{
						b.posY = pos.getY() + 8;
						b.motionY = 0;
					}
				}
			}

			@Override
			public void callback()
			{
				Entity ent;
				int rand = RewardsUtil.rand.nextInt(6);
				for(EntityFallingBlock b : blocks)
				{
					CCubesPacketHandler.CHANNEL.send(PacketDistributor.NEAR.with(() -> new TargetPoint(b.posX, b.posY, b.posZ, 50, world.getDimension().getType())), new PacketParticle("largeexplode", b.posX, b.posY, b.posZ, 0, 0, 0));

					b.remove();

					if(rand == 0)
					{
						ent = new EntityCreeper(world);
						((EntityCreeper) ent).addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 20, 3));
					}
					else if(rand == 1)
					{
						ent = new EntityTNTPrimed(world);
					}
					else if(rand == 2)
					{
						ent = new EntityItem(world);
						((EntityItem) ent).setItem(new ItemStack(Items.DIAMOND));
					}
					else if(rand == 3)
					{
						ent = new EntityItem(world);
						((EntityItem) ent).setItem(new ItemStack(Items.MELON_SLICE));
					}
					else if(rand == 4)
					{
						ent = new EntityBat(world);
					}
					else if(rand == 5)
					{
						ent = new EntityZombie(world);
						((EntityZombie) ent).addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 20, 3));
					}
					else
					{
						ent = new EntityItem(world);
						((EntityItem) ent).setItem(new ItemStack(Items.DIAMOND));
					}

					ent.setPosition(b.posX, b.posY, b.posZ);
					world.spawnEntity(ent);
				}
				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1f, 1f);
			}
		});

	}
}
