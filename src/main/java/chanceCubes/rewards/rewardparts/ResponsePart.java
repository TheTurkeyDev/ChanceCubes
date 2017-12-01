package chanceCubes.rewards.rewardparts;

import java.util.List;

import chanceCubes.util.CCubesCommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ResponsePart
{
	private EntityPlayer player;
	private String question;
	private List<ResponseHolder> responses;
	
	private int attempts;

	public ResponsePart(EntityPlayer player, String question, List<ResponseHolder> responses)
	{
		this.player = player;
		this.question = question;
		this.responses = responses;
	}

	public boolean onResponse(EntityPlayer player, String message)
	{
		if(this.player.equals(player))
		{
			for(ResponseHolder response : this.responses)
			{
				if(response.response.equals(message))
				{
					World world = player.worldObj;
					CCubesCommandSender sender = new CCubesCommandSender(player, new BlockPos(player.getPosition()));
					MinecraftServer server = world.getMinecraftServer();
					Boolean rule = server.worldServers[0].getGameRules().getBoolean("commandBlockOutput");
					server.worldServers[0].getGameRules().setOrCreateGameRule("commandBlockOutput", "false");
					server.getCommandManager().executeCommand(sender, response.command);
					server.worldServers[0].getGameRules().setOrCreateGameRule("commandBlockOutput", rule.toString());
					return true;
				}
			}
		}
		return false;
	}

	public class ResponseHolder
	{
		public String response;
		public String command;
	}
}
