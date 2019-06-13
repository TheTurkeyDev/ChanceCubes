package chanceCubes.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public abstract class Task
{
	public String name;
	public int delayLeft;
	private boolean forever = false;

	public int updateTick;

	public Task(String name, int delay)
	{
		this(name, delay, -1);
	}

	public Task(String name, int delay, int updateTick)
	{
		this.name = name;
		this.delayLeft = delay;
		if(delay == -1)
			forever = true;
		this.updateTick = updateTick;
	}

	public boolean shouldUpdate()
	{
		return this.updateTick != -1 && (delayLeft % updateTick) == 0;
	}

	public abstract void callback();

	public boolean tickTask()
	{
		this.delayLeft--;
		return this.delayLeft <= 0 && !forever;
	}

	public void update()
	{

	}

	public void showTimeLeft(EntityPlayer player, SPacketTitle.Type location)
	{
		int time = this.delayLeft / 20;
		TextComponentString message = new TextComponentString(String.valueOf(time));
		message.getStyle().setBold(true).setColor(TextFormatting.RED);
		RewardsUtil.setPlayerTitle(player, new SPacketTitle(location, message, 0, 20, 0));
	}
}