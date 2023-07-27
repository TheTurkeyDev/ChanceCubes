package chanceCubes.mcwrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockWrapper
{
	public static SignBlockEntity createSign(BlockPos pos, String[] linesText)
	{
		SignBlockEntity sign = new SignBlockEntity(pos.offset(0, 0, 0), Blocks.OAK_SIGN.defaultBlockState());

		for(int i = 0; i < Math.min(linesText.length, 3); i++)
			sign.setMessage(i, ComponentWrapper.string(linesText[i]));

		return sign;
	}

	public static void setSignText(BlockEntity signBE, String[] linesText)
	{
		if(signBE instanceof SignBlockEntity sign)
			for(int i = 0; i < Math.min(linesText.length, 3); i++)
				sign.setMessage(i, ComponentWrapper.string(linesText[i]));
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
		return ForgeRegistries.BLOCKS.getKey(b);
	}
}
