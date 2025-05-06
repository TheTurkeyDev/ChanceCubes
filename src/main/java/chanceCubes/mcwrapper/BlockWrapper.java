package chanceCubes.mcwrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;

public class BlockWrapper
{
	public static SignBlockEntity createSign(BlockPos pos, String[] linesText)
	{
		SignBlockEntity sign = new SignBlockEntity(pos, Blocks.OAK_SIGN.defaultBlockState());

		SignText text = new SignText();
		for(int i = 0; i < Math.min(linesText.length, 3); i++)
			text = text.setMessage(i, ComponentWrapper.string(linesText[i]));

		sign.frontText = text;

		return sign;
	}

	public static void setSignText(BlockEntity signBE, String[] linesText)
	{
		SignText text = new SignText();
		for(int i = 0; i < Math.min(linesText.length, 3); i++)
			text = text.setMessage(i, ComponentWrapper.string(linesText[i]));

		if(signBE instanceof SignBlockEntity sign)
			sign.frontText = text;
	}

	public static boolean isBlockSolid(Level level, BlockPos pos)
	{
		return level.getBlockState(pos).isSolidRender(level, pos);
	}


	public static String getBlockIdStr(Block b)
	{
		return getBlockId(b).toString();
	}


	public static ResourceLocation getBlockId(Block b)
	{
		return BuiltInRegistries.BLOCK.getKey(b);
	}
}
