package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CoinFlipReward implements IChanceCubeReward
{
	private List<EntityPlayer> inFlip = new ArrayList<EntityPlayer>();

	@Override
	public void trigger(World world, BlockPos pos, final EntityPlayer player)
	{
		if(inFlip.contains(player))
			return;

		player.sendMessage(new TextComponentString("Heads or Tails"));

		inFlip.add(player);

		Task task = new Task("Heads_Or_Tails", 600)
		{
			@Override
			public void callback()
			{
				timeUp(player, false);
			}

		};

		Scheduler.scheduleTask(task);
	}

	private void timeUp(EntityPlayer player, boolean correct)
	{
		if(!inFlip.contains(player) || !RewardsUtil.isPlayerOnline(player))
			return;

		player.sendMessage(new TextComponentString("Seem's that you didn't pick heads or tails."));
		player.sendMessage(new TextComponentString("You must be real fun at parties...."));
		
		inFlip.remove(player);
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Heads_or_Tails";
	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		EntityPlayer player = event.getPlayer();

		String message = event.getMessage();

		if(!message.equalsIgnoreCase("Heads") && !message.equalsIgnoreCase("Tails"))
			return;

		if(inFlip.contains(player))
		{
			int flip = RewardsUtil.rand.nextInt(6000);

			// Yes I know heads has a 49.9833% chance and Tails has a 50% Chance. Deal with it.
			if(flip == 1738)
			{
				player.sendMessage(new TextComponentString("The coin landed on it's side....."));
				player.sendMessage(new TextComponentString("Well this is awkward"));
			}
			else if(flip < 3000)
			{
				if(message.equalsIgnoreCase("Heads"))
				{
					player.sendMessage(new TextComponentString("It was heads! You're correct!"));
					player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}
				else
				{
					player.sendMessage(new TextComponentString("It was heads! You're incorrect!"));
					for(int i = 0; i < 5; i++)
					{
						player.world.spawnEntity(new EntityTNTPrimed(player.world, player.posX, player.posY + 1D, player.posZ, player));
						player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
			else
			{
				if(message.equalsIgnoreCase("Tails"))
				{
					player.sendMessage(new TextComponentString("It was tails! You're correct!"));
					player.world.spawnEntity(new EntityItem(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}
				else
				{
					player.sendMessage(new TextComponentString("It was tails! You're incorrect!"));
					for(int i = 0; i < 5; i++)
					{
						player.world.spawnEntity(new EntityTNTPrimed(player.world, player.posX, player.posY + 1D, player.posZ, player));
						player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}

			inFlip.remove(player);
			event.setCanceled(true);
		}
	}
}