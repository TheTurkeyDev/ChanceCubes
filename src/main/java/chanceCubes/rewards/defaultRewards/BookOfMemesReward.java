package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;

import chanceCubes.CCubesCore;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.util.CCubesCommandSender;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BookOfMemesReward implements IChanceCubeReward
{
	private List<String> memes = new ArrayList<String>();

	public BookOfMemesReward()
	{
		memes.add("Sodium, atomic number 11, was first isolated by Peter Dager in 1807. A chemical component of salt, he named it Na in honor of the saltiest region on earth, North America.");
		memes.add("(â•¯Â°â–¡Â°ï¼‰â•¯ï¸µ â”»â”�â”» \n à¼¼á•—àºˆÙ„Íœàºˆà¼½á•— RAISE YOUR DONGERS à¼¼á•—àºˆÙ„Íœàºˆà¼½á•—");
		memes.add("Darude- status \n â˜� Not Sandstorm \n â˜‘ Sandstorm");
		memes.add("( Í¡Â° ÍœÊ– Í¡Â°) Every 60 seconds in Africa, a minute passes. Together we can stop this. Please spread the word ( Í¡Â° ÍœÊ– Í¡Â°) ");
		memes.add("YESTERDAY YOU SAID TOMMOROW, Don't let your dreams be memes, Don't meme your dreams be beams, Jet fuel won't melt tomorrow's memes, DON'T LET YOUR STEEL MEMES BE JET DREAMS");
		memes.add("If the human body is 75% water, how can you be 100% salt?");
		memes.add(" â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ \n Sorry, I've dropped my bag of Doritosâ„¢ brand chipsâ–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â–º â–¼ â—„ â—„ â–²â–² â–º â–¼ â—„â–¼ â—„ â—„ â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º");
		memes.add(" â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ \n Sorry, I've dropped my bag of Twitchâ„¢ brand bitsâ–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â–º â–¼ â—„ â—„ â–²â–² â–º â–¼ â—„â–¼ â—„ â—„ â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º â–¼ â—„ â–² â–º");
		memes.add("Hey Chat....... \n \n \n \n 123");
		memes.add("O-oooooooooo AAAAE-A-A-I-A-U- JO-oooooooooooo AAE-O-A-A-U-U-A- E-eee-ee-eee AAAAE-A-E-I-E-A-JO-ooo-oo-oo-oo EEEEO-A-AAA-AAAA \n O-oooooooooo AAAAE-A-A-I-A-U- JO-oooooooooooo AAE-O-A-A-U-U-A- E-eee-ee-eee AAAAE-A-E-I-E-A-JO-ooo-oo-oo-oo EEEEO-A-AAA-AAAA");
		memes.add("Did you just assume this books meme? #Triggered");
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player)
	{
		String meme = memes.get(RewardsUtil.rand.nextInt(memes.size()));
		MinecraftServer server = world.getMinecraftServer();
		Boolean rule = server.worlds[0].getGameRules().getBoolean("commandBlockOutput");
		server.worlds[0].getGameRules().setOrCreateGameRule("commandBlockOutput", "false");
		String command = "/summon Item ~ ~1 ~ {Item:{id:written_book,Count:1,tag:{title:\"Book of Memes\",author:\"Chance Cubes\",generation:0,pages:[\"{text:\\\"" + meme + "\\\",color:black}\"]}}}";
		CCubesCommandSender sender = new CCubesCommandSender(player, pos);
		server.getCommandManager().executeCommand(sender, command);
		server.worlds[0].getGameRules().setOrCreateGameRule("commandBlockOutput", rule.toString());
	}

	@Override
	public int getChanceValue()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return CCubesCore.MODID + ":Book_Of_Memes";
	}

}
