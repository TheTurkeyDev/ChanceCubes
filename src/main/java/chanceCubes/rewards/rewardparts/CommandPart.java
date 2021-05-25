package chanceCubes.rewards.rewardparts;

import chanceCubes.rewards.variableTypes.BoolVar;
import chanceCubes.rewards.variableTypes.IntVar;
import chanceCubes.rewards.variableTypes.StringVar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.UUIDCodec;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommandPart extends BasePart
{
	public static Map<Integer, UUID> randUUIDs = new HashMap<>();
	private StringVar command;
	private IntVar copies = new IntVar(0);
	private BoolVar copiesSoft = new BoolVar(false);
	private BoolVar relativeToPlayer = new BoolVar(false);

	public CommandPart(String command)
	{
		this(command, 0);
	}

	public CommandPart(String command, int delay)
	{
		this(new StringVar(command), new IntVar(delay));
	}

	public CommandPart(StringVar command)
	{
		this(command, new IntVar(0));
	}

	public CommandPart(StringVar command, IntVar delay)
	{
		this.command = command;
		this.setDelay(delay);
	}

	public String getRawCommand()
	{
		return command.getValue();
	}

	public String getParsedCommand(World world, int x, int y, int z, PlayerEntity player)
	{
		String parsedCommand = command.getValue();
		//TODO:?
		parsedCommand = parsedCommand.replace("%player", player.getName().getUnformattedComponentText());
		parsedCommand = parsedCommand.replace("%x", "" + x);
		parsedCommand = parsedCommand.replace("%y", "" + y);
		parsedCommand = parsedCommand.replace("%z", "" + z);
		parsedCommand = parsedCommand.replace("%pyaw", "" + player.rotationYaw);
		parsedCommand = parsedCommand.replace("%ppitch", "" + player.rotationPitch);
		parsedCommand = parsedCommand.replace("%px", "" + player.getPosX());
		parsedCommand = parsedCommand.replace("%py", "" + player.getPosY());
		parsedCommand = parsedCommand.replace("%pz", "" + player.getPosZ());
		parsedCommand = parsedCommand.replace("%puuid", player.getUniqueID().toString());
		int[] enc = UUIDCodec.encodeUUID(player.getUniqueID());
		parsedCommand = parsedCommand.replace("%pencuuid", String.format("[I;%d,%d,%d,%d]", enc[0], enc[1], enc[2], enc[3]));
		parsedCommand = parsedCommand.replace("%pdir", player.getHorizontalFacing().toString());

		// Random UUID's
		int index;
		while((index = parsedCommand.indexOf("%randuuid")) != -1)
		{
			String randIDStr = parsedCommand.substring(index + 9, index + 10);
			int randId = 0;
			if(randIDStr.matches("[0-9]"))
				randId = Integer.parseInt(randIDStr);

			UUID uuid = randUUIDs.computeIfAbsent(randId, (id) -> UUID.randomUUID());
			parsedCommand.replace("%randuuid" + randIDStr, uuid.toString());
		}

		while((index = parsedCommand.indexOf("%randencuuid")) != -1)
		{
			String randIDStr = parsedCommand.substring(index + 12, index + 13);
			int randId = 0;
			if(randIDStr.matches("[0-9]"))
				randId = Integer.parseInt(randIDStr);

			UUID uuid = randUUIDs.computeIfAbsent(randId, (id) -> UUID.randomUUID());
			enc = UUIDCodec.encodeUUID(uuid);
			parsedCommand = parsedCommand.replace("%randencuuid", String.format("[I;%d,%d,%d,%d]", enc[0], enc[1], enc[2], enc[3]));
		}

		return parsedCommand;
	}

	public IntVar getCopies()
	{
		return copies;
	}

	public void setCopies(IntVar copies)
	{
		this.copies = copies;
	}

	public BoolVar areCopiesSoft()
	{
		return copiesSoft;
	}

	public void setCopiesSoft(BoolVar copiesSoft)
	{
		this.copiesSoft = copiesSoft;
	}

	public void setRelativeToPlayer(BoolVar boolVar)
	{
		this.relativeToPlayer = boolVar;
	}

	public BoolVar isRelativeToPlayer()
	{
		return this.relativeToPlayer;
	}
}
