package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CoinFlipReward extends BaseCustomReward
{
	private List<PlayerEntity> inFlip = new ArrayList<>();

	public CoinFlipReward()
	{
		super(CCubesCore.MODID + ":heads_or_tails", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		if(inFlip.contains(player))
			return;

		player.sendMessage(new StringTextComponent("Heads or Tails"), player.getUniqueID());

		inFlip.add(player);

		Task task = new Task("Heads_Or_Tails", 600)
		{
			@Override
			public void callback()
			{
				timeUp(player);
			}

		};

		Scheduler.scheduleTask(task);
	}

	private void timeUp(PlayerEntity player)
	{
		if(!inFlip.contains(player) || !RewardsUtil.isPlayerOnline(player))
			return;

		player.sendMessage(new StringTextComponent("Seem's that you didn't pick heads or tails."), player.getUniqueID());
		player.sendMessage(new StringTextComponent("You must be real fun at parties...."), player.getUniqueID());

		inFlip.remove(player);
	}

	@SubscribeEvent
	public void onMessage(ServerChatEvent event)
	{
		PlayerEntity player = event.getPlayer();

		String message = event.getMessage();

		if(!message.equalsIgnoreCase("Heads") && !message.equalsIgnoreCase("Tails"))
			return;

		if(inFlip.contains(player))
		{
			int flip = RewardsUtil.rand.nextInt(6000);

			// Yes I know heads has a 49.9833% chance and Tails has a 50% Chance. Deal with it.
			if(flip == 1738)
			{
				player.sendMessage(new StringTextComponent("The coin landed on it's side....."), player.getUniqueID());
				player.sendMessage(new StringTextComponent("Well this is awkward"), player.getUniqueID());
			}
			else if(flip < 3000)
			{
				if(message.equalsIgnoreCase("Heads"))
				{
					player.sendMessage(new StringTextComponent("It was heads! You're correct!"), player.getUniqueID());
					player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}
				else
				{
					player.sendMessage(new StringTextComponent("It was heads! You're incorrect!"), player.getUniqueID());
					for(int i = 0; i < 5; i++)
					{
						player.world.addEntity(new TNTEntity(player.world, player.getPosX(), player.getPosY() + 1D, player.getPosZ(), player));
						player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
			else
			{
				if(message.equalsIgnoreCase("Tails"))
				{
					player.sendMessage(new StringTextComponent("It was tails! You're correct!"), player.getUniqueID());
					player.world.addEntity(new ItemEntity(player.world, player.getPosX(), player.getPosY(), player.getPosZ(), new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}
				else
				{
					player.sendMessage(new StringTextComponent("It was tails! You're incorrect!"), player.getUniqueID());
					for(int i = 0; i < 5; i++)
					{
						player.world.addEntity(new TNTEntity(player.world, player.getPosX(), player.getPosY() + 1D, player.getPosZ(), player));
						player.world.playSound(player, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}

			inFlip.remove(player);
			event.setCanceled(true);
		}
	}
}