package chanceCubes.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

public abstract class Task
{
	public final String name;
	public int delayLeft;
	private boolean forever = false;

	public final int updateTick;

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

	public void showTimeLeft(Player player, GuiTextLocation location)
	{
		int time = this.delayLeft / 20;
		TextComponent message = new TextComponent(String.valueOf(time));
		message.setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)));
		RewardsUtil.setPlayerTitle(player, location, message, 0, 20, 0);
	}
}