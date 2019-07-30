package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

public class CoinFlipReward extends BaseCustomReward
{
	private List<PlayerEntity> inFlip = new ArrayList<>();

	public CoinFlipReward()
	{
		super(CCubesCore.MODID + ":Heads_or_Tails", 0);
	}

	@Override
	public void trigger(World world, BlockPos pos, final PlayerEntity player, Map<String, Object> settings)
	{
		if(inFlip.contains(player))
			return;

		player.sendMessage(new StringTextComponent("Heads or Tails"));

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

		player.sendMessage(new StringTextComponent("Seem's that you didn't pick heads or tails."));
		player.sendMessage(new StringTextComponent("You must be real fun at parties...."));

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
				player.sendMessage(new StringTextComponent("The coin landed on it's side....."));
				player.sendMessage(new StringTextComponent("Well this is awkward"));
			}
			else if(flip < 3000)
			{
				if(message.equalsIgnoreCase("Heads"))
				{
					player.sendMessage(new StringTextComponent("It was heads! You're correct!"));
					player.world.addEntity(new ItemEntity(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}
				else
				{
					player.sendMessage(new StringTextComponent("It was heads! You're incorrect!"));
					for(int i = 0; i < 5; i++)
					{
						player.world.addEntity(new TNTEntity(player.world, player.posX, player.posY + 1D, player.posZ, player));
						player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}
			else
			{
				if(message.equalsIgnoreCase("Tails"))
				{
					player.sendMessage(new StringTextComponent("It was tails! You're correct!"));
					player.world.addEntity(new ItemEntity(player.world, player.posX, player.posY, player.posZ, new ItemStack(RewardsUtil.getRandomItem(), 1)));
				}
				else
				{
					player.sendMessage(new StringTextComponent("It was tails! You're incorrect!"));
					for(int i = 0; i < 5; i++)
					{
						player.world.addEntity(new TNTEntity(player.world, player.posX, player.posY + 1D, player.posZ, player));
						player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
					}
				}
			}

			inFlip.remove(player);
			event.setCanceled(true);
		}
	}
}