package chanceCubes.mcwrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockWrapper
{
	public static SignBlockEntity createSign(BlockPos pos, String[] linesText)
	{
		SignBlockEntity sign = new SignBlockEntity(pos.offset(0, 0, 0), Blocks.OAK_SIGN.defaultBlockState());

		for(int i = 0; i < Math.min(linesText.length, 3); i++)
			sign.setMessage(i, new TextComponent(linesText[i]));

		return sign;
	}

	public static void setSignText(BlockEntity signBE, String[] linesText)
	{
		if(signBE instanceof SignBlockEntity sign)
			for(int i = 0; i < Math.min(linesText.length, 3); i++)
				sign.setMessage(i, new TextComponent(linesText[i]));
	}

	public static boolean isBlockSolid(Level level, BlockPos pos)
	{
		BlockState state = level.getBlockState(pos);
		return state.isSolidRender(level, pos);
	}
}
