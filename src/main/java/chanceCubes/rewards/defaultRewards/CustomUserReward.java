package chanceCubes.rewards.defaultRewards;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.parsers.RewardParser;
import chanceCubes.registry.global.GlobalCCRewardRegistry;
import chanceCubes.util.HTTPUtil;
import chanceCubes.util.Scheduler;
import chanceCubes.util.Task;
import com.google.gson.JsonElement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class CustomUserReward extends BaseCustomReward
{
	private String userName;
	private UUID uuid;
	private String type;
	private List<BasicReward> customRewards;

	public static void getCustomUserReward(UUID uuid)
	{
		if(!CCubesSettings.userSpecificRewards)
			return;

		JsonElement users;
		try
		{
			users = HTTPUtil.getWebFile("GET", "https://api.theprogrammingturkey.com/chance_cubes/custom_rewards/UserList.json");
		} catch(Exception e)
		{
			e.printStackTrace();
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to get the list of users with custom rewards!");
			return;
		}

		String userName = "";
		String type = "";
		for(JsonElement user : users.getAsJsonArray())
		{
			if(user.getAsJsonObject().get("UUID").getAsString().equalsIgnoreCase(uuid.toString()))
			{
				userName = user.getAsJsonObject().get("Name").getAsString();
				type = user.getAsJsonObject().get("Type").getAsString();
			}
		}

		if(userName.equals(""))
		{
			CCubesCore.logger.log(Level.INFO, "No custom rewards detected for the current user!");
			return;
		}

		JsonElement userRewards;

		try
		{
			userRewards = HTTPUtil.getWebFile("GET", "https://api.theprogrammingturkey.com/chance_cubes/custom_rewards/users/" + userName + ".json");
		} catch(Exception e)
		{
			CCubesCore.logger.log(Level.ERROR, "Chance Cubes failed to get the custom list for " + userName + "!");
			CCubesCore.logger.log(Level.ERROR, e.getMessage());
			return;
		}

		List<BasicReward> customRewards = new ArrayList<>();

		for(Entry<String, JsonElement> reward : userRewards.getAsJsonObject().entrySet())
			customRewards.add(RewardParser.parseReward(reward).getKey());

		//GROSS, but idk what else todo
		String userNameFinal = userName;
		String typeFinal = type;
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() ->
		{
			//TODO: Only register/ enable for that user?
			GlobalCCRewardRegistry.INSTANCE.registerReward(new CustomUserReward(userNameFinal, uuid, typeFinal, customRewards));
			EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(uuid);
			player.sendMessage(new TextComponentString("Seems you have some custom Chance Cubes rewards " + userNameFinal + "...."));
			player.sendMessage(new TextComponentString("Let the fun begin! >:)"));
		});
	}

	public CustomUserReward(String un, UUID uuid, String type, List<BasicReward> rewards)
	{
		super(CCubesCore.MODID + ":CR_" + un, 0);
		this.userName = un;
		this.uuid = uuid;
		this.type = type;
		this.customRewards = rewards;
	}

	@Override
	public void trigger(final World world, final BlockPos pos, final EntityPlayer player, Map<String, Object> settings)
	{

		if(!UsernameCache.getLastKnownUsername(uuid).equalsIgnoreCase(player.getName()))
		{
			player.sendMessage(new TextComponentString("Hey you aren't " + this.userName + "! You can't have their reward! Try again!"));
			Entity itemEnt = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CCubesBlocks.CHANCE_CUBE, 1));
			world.spawnEntity(itemEnt);
			return;
		}

		player.sendMessage(new TextComponentString("Selecting best (possibly deadly) reward for " + this.type + " " + this.userName));

		Scheduler.scheduleTask(new Task("Custom Reward", 100)
		{
			@Override
			public void callback()
			{
				customRewards.get(world.rand.nextInt(customRewards.size())).trigger(world, pos, player, settings);
			}
		});
	}
}
