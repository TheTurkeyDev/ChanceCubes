package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.biodomeGen.BioDomeGen;
import chanceCubes.util.RewardsUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class BossBaseReward extends BaseCustomReward
{
	private String bossName;

	private List<Entity> trackedEntities = new ArrayList<>();
	private BioDomeGen domeGen;

	public BossBaseReward(String bossName)
	{
		super(CCubesCore.MODID + ":Boss_" + bossName, -35);
		this.bossName = bossName.replace("_", " ");
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		domeGen = new BioDomeGen(player);
		domeGen.genRandomDome(pos.add(0, -1, 0), world, 15, false);
		TextComponentString message = new TextComponentString("BOSS FIGHT!");
		message.setStyle((new Style()).setColor(TextFormatting.RED));
		SPacketTitle titlePacket = new SPacketTitle(SPacketTitle.Type.TITLE, message, 10, 500, 0);
		RewardsUtil.setNearPlayersTitle(world, titlePacket, pos, 50);

		Scheduler.scheduleTask(new Task("boss_fight_subtitle_1", 120)
		{
			@Override
			public void callback()
			{
				TextComponentString message = new TextComponentString("");
				message.appendSibling(player.getDisplayName());

				StringBuilder sbSpace = new StringBuilder();
				sbSpace.append(" VS ");
				for(int i = 0; i < bossName.length(); i++)
				{
					sbSpace.append(" ");
				}
				message.appendText(sbSpace.toString());

				message.setStyle((new Style()).setColor(TextFormatting.RED));
				SPacketTitle titlePacket = new SPacketTitle(SPacketTitle.Type.SUBTITLE, message, 0, 500, 0);
				RewardsUtil.setNearPlayersTitle(world, titlePacket, pos, 50);
			}
		});

		Scheduler.scheduleTask(new Task("boss_fight_subtitle_2", 160)
		{
			@Override
			public void callback()
			{
				TextComponentString message = new TextComponentString("");
				message.appendSibling(player.getDisplayName());
				message.appendText(" VS ");
				message.appendText(bossName);
				message.setStyle((new Style()).setColor(TextFormatting.RED));
				SPacketTitle titlePacket = new SPacketTitle(SPacketTitle.Type.SUBTITLE, message, 0, 100, 10);
				RewardsUtil.setNearPlayersTitle(world, titlePacket, pos, 50);
			}
		});

		Scheduler.scheduleTask(new Task("boss_fight_start", 200)
		{
			@Override
			public void callback()
			{
				startBossFight(world, pos, player, settings);
			}
		});
	}

	public void startBossFight(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		spawnBoss(world, pos, player, settings);
		Scheduler.scheduleTask(new Task("boss_fight_tracker", -1, 5)
		{
			@Override
			public void callback()
			{
			}

			@Override
			public void update()
			{
				CCubesCore.logger.log(Level.INFO, trackedEntities.size());
				for(int i = trackedEntities.size() - 1; i >= 0; i--)
				{
					Entity ent = trackedEntities.get(i);
					if(ent.isDead)
					{
						CCubesCore.logger.log(Level.INFO, "Dead");
						trackedEntities.remove(i);
						if(trackedEntities.isEmpty())
						{
							CCubesCore.logger.log(Level.INFO, "Empty");
							endBossfight(world, pos, player);
							Scheduler.removeTask(this);
						}
					}
				}
			}
		});
	}

	public void endBossfight(World world, BlockPos pos, EntityPlayer player)
	{
		onBossFightEnd(world, pos, player);
		domeGen.removeDome();
	}

	protected void trackEntities(Entity... ents)
	{
		trackedEntities.addAll(Arrays.asList(ents));
	}

	public abstract void spawnBoss(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings);

	public abstract void onBossFightEnd(World world, BlockPos pos, EntityPlayer player);
}
