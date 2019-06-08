package chanceCubes.listeners;

import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.client.listeners.RenderEvent;
import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChanceCube;
import chanceCubes.registry.ChanceCubeRegistry;
import chanceCubes.tileentities.TileChanceCube;
import chanceCubes.util.Scheduler;
import chanceCubes.util.SchematicUtil;
import chanceCubes.util.Task;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickEmpty;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickEmpty;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockListener
{
	private boolean setdelay = false;

	@SubscribeEvent
	public void onBlockBreak(BreakEvent event)
	{
		World world = event.getWorld().getWorld();
		EntityPlayer player = event.getPlayer();
		BlockPos pos = event.getPos();
		TileEntity te = world.getTileEntity(pos);
		if(event.getState().getBlock().equals(CCubesBlocks.CHANCE_CUBE))
		{
			if(!world.isRemote() && player != null && !(player instanceof FakePlayer) && te instanceof TileChanceCube)
			{
				TileChanceCube tileCube = (TileChanceCube) te;
				if(!player.inventory.getCurrentItem().isEmpty() && player.inventory.getCurrentItem().getItem().equals(CCubesItems.silkPendant))
				{
					ItemStack stackCube = new ItemStack(Item.getItemFromBlock(CCubesBlocks.CHANCE_CUBE), 1);
					((ItemChanceCube) stackCube.getItem()).setChance(stackCube, tileCube.isScanned() ? tileCube.getChance() : -101);
					EntityItem blockstack = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stackCube);
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
					world.removeTileEntity(pos);
					world.spawnEntity(blockstack);
				}

				if(te != null)
				{
					world.setBlockState(pos, Blocks.AIR.getDefaultState());
					ChanceCubeRegistry.INSTANCE.triggerRandomReward(world, pos, player, tileCube.getChance());
				}
			}
		}
		if(this.setSchematicPoint(1, player, pos))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public void onBlockInteract(RightClickBlock event)
	{
		if(this.setSchematicPoint(2, event.getEntityPlayer(), event.getPos()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public void onAirInteractRight(RightClickEmpty event)
	{
		this.setSchematicPoint(2, event.getEntityPlayer(), event.getPos());
	}

	@SubscribeEvent
	public void onAirInteractLeft(LeftClickEmpty event)
	{
		this.setSchematicPoint(1, event.getEntityPlayer(), event.getPos());
	}

	public boolean setSchematicPoint(int point, EntityPlayer player, BlockPos pos)
	{
		if(Minecraft.getInstance().isSingleplayer() && RenderEvent.isCreatingSchematic() && !setdelay)
		{
			if(player.abilities.isCreativeMode)
			{
				boolean flag = false;
				if(point == 1)
				{
					SchematicUtil.selectionPoints[0] = pos;
					player.sendMessage(new TextComponentString("Point 1 set"));
					flag = true;
				}
				else if(point == 2)
				{
					SchematicUtil.selectionPoints[1] = pos;
					player.sendMessage(new TextComponentString("Point 2 set"));
					flag = true;
				}

				if(flag)
				{
					setdelay = true;
					Scheduler.scheduleTask(new Task("Schematic_Point_Set_Delay", 10)
					{

						@Override
						public void callback()
						{
							setdelay = false;
						}

					});
					return true;
				}
			}
		}
		return false;
	}
}