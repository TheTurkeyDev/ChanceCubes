package chanceCubes.rewards.defaultRewards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chanceCubes.CCubesCore;
import chanceCubes.util.RewardsUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BookOfMemesReward extends BaseCustomReward
{
	private List<String> memes = new ArrayList<String>();

	public BookOfMemesReward()
	{
		super(CCubesCore.MODID + ":Book_of_Memes", 0);
		memes.add("Sodium, atomic number 11, was first isolated by Peter Dager in 1807. A chemical component of salt, he named it Na in honor of the saltiest region on earth, North America.");
		memes.add("(╯°□°）╯︵ ┻━┻ \n ༼ᕗຈل͜ຈ༽ᕗ RAISE YOUR DONGERS ༼ᕗຈل͜ຈ༽ᕗ");
		memes.add("Darude- status \n ☐ Not Sandstorm \n ☑ Sandstorm");
		memes.add("( ͡° ͜ʖ ͡°) Every 60 seconds in Africa, a minute passes. Together we can stop this. Please spread the word ( ͡° ͜ʖ ͡°) ");
		memes.add("YESTERDAY YOU SAID TOMMOROW, Don't let your dreams be memes, Don't meme your dreams be beams, Jet fuel won't melt tomorrow's memes, DON'T LET YOUR STEEL MEMES BE JET DREAMS");
		memes.add("If the human body is 75% water, how can you be 100% salt?");
		memes.add(" ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ \n Sorry, I've dropped my bag of Doritos™ brand chips▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ► ▼ ◄ ◄ ▲▲ ► ▼ ◄▼ ◄ ◄ ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ► ▼ ◄ ▲ ►");
		memes.add("Hey Chat....... \n \n \n \n 123");
		memes.add("O-oooooooooo AAAAE-A-A-I-A-U- JO-oooooooooooo AAE-O-A-A-U-U-A- E-eee-ee-eee AAAAE-A-E-I-E-A-JO-ooo-oo-oo-oo EEEEO-A-AAA-AAAA \n O-oooooooooo AAAAE-A-A-I-A-U- JO-oooooooooooo AAE-O-A-A-U-U-A- E-eee-ee-eee AAAAE-A-E-I-E-A-JO-ooo-oo-oo-oo EEEEO-A-AAA-AAAA");
		memes.add("Did you just assume this books meme? #Triggered");
	}

	@Override
	public void trigger(World world, BlockPos pos, EntityPlayer player, Map<String, Object> settings)
	{
		String meme = memes.get(RewardsUtil.rand.nextInt(memes.size()));
		String command = "/summon item ~ ~1 ~ {Item:{id:written_book,Count:1,tag:{title:\"Book of Memes\",author:\"Chance Cubes\",generation:0,pages:[\"{text:\\\"" + meme + "\\\",color:black}\"]}}}";
		RewardsUtil.executeCommand(world, player, command);
	}
}