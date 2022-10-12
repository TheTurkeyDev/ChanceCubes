package chanceCubes.util;

import chanceCubes.CCubesCore;
import chanceCubes.blocks.CCubesBlocks;
import chanceCubes.config.CCubesSettings;
import chanceCubes.mcwrapper.BlockWrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles modifications to {@link CCubesSettings#nonReplaceableBlocks} after blocks have been added by IMC messages.
 */
public class NonreplaceableBlockOverride
{
	public BlockState overriddenBlock;
	public OverrideType overrideType;

	public NonreplaceableBlockOverride()
	{
		overriddenBlock = Blocks.AIR.defaultBlockState();
		overrideType = OverrideType.ADD;
	}

	/**
	 * Parses compatible strings into a {@link java.util.List} of {@link NonreplaceableBlockOverride NonreplaceableBlockOverrides}.
	 *
	 * @param strings An array of {@link java.lang.String Strings} to attempt to parse.
	 * @return The resulting {@link java.util.List} of {@link NonreplaceableBlockOverride} objects.
	 */
	public static List<NonreplaceableBlockOverride> parseStrings(String[] strings)
	{
		List<NonreplaceableBlockOverride> overrides = new ArrayList<>();
		for(String string : strings)
		{
			NonreplaceableBlockOverride toAdd = parseString(string);
			if(toAdd != null)
			{
				overrides.add(toAdd);
			}
		}
		return overrides;
	}

	/**
	 * Parses a compatible string into a {@link NonreplaceableBlockOverride}.
	 *
	 * @param string The string to parse.
	 * @return The resulting {@link NonreplaceableBlockOverride}.<
	 */
	@Nullable
	private static NonreplaceableBlockOverride parseString(@Nonnull String string)
	{
		try
		{
			switch(string.toCharArray()[0])
			{
				case '+':
				{
					return addBlock(string.substring(1));
				}
				case '-':
				{
					return removeBlock(string.substring(1));
				}
				case '#':
					return null;
				default:
				{
					return addBlock(string);
				}
			}
		} catch(Exception ex)
		{
			CCubesCore.logger.warn("Error adding block: " + ex.getMessage());
			CCubesCore.logger.warn("Could not add override for specified block \"" + string + "\", skipping.");
			return null;
		}
	}

	/**
	 * Creates a {@link NonreplaceableBlockOverride} with the {@link OverrideType#REMOVE REMOVE} {@link OverrideType} from the given Block ID.
	 *
	 * @param substring The Block ID.
	 * @return The {@link NonreplaceableBlockOverride} produced.
	 */
	private static NonreplaceableBlockOverride removeBlock(String substring)
	{
		NonreplaceableBlockOverride output = new NonreplaceableBlockOverride();
		output.overrideType = OverrideType.REMOVE;
		output.overriddenBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(substring)).defaultBlockState();
		return output;
	}

	/**
	 * Creates a {@link NonreplaceableBlockOverride} with the {@link OverrideType#ADD ADD} {@link OverrideType} from the given Block ID.
	 *
	 * @param substring The Block ID.
	 * @return The {@link NonreplaceableBlockOverride} produced.
	 */
	private static NonreplaceableBlockOverride addBlock(String substring)
	{
		NonreplaceableBlockOverride output = new NonreplaceableBlockOverride();
		output.overrideType = OverrideType.ADD;
		output.overriddenBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(substring)).defaultBlockState();
		return output;
	}

	/**
	 * Produces an array of {@link java.lang.String Strings} from a {@link java.util.List} of {@link NonreplaceableBlockOverride NonreplaceableBlockOverrides}.
	 *
	 * @param overrides The {@link java.util.List} of {@link NonreplaceableBlockOverride NonreplaceableBlockOverrides}.
	 * @return The {@link java.lang.String String} array produced.
	 */
	public static String[] parseOverrides(List<NonreplaceableBlockOverride> overrides)
	{
		List<String> strings = new ArrayList<>();
		for(NonreplaceableBlockOverride override : overrides)
		{
			String toAdd = parseOverride(override);
			if(toAdd != null)
				strings.add(toAdd);
		}
		return strings.toArray(new String[0]);
	}

	/**
	 * Produces a{@link java.lang.String} from a {@link NonreplaceableBlockOverride NonreplaceableBlockOverride}.
	 *
	 * @param override The {@link NonreplaceableBlockOverride NonreplaceableBlockOverride}.
	 * @return The {@link java.lang.String String} produced or null if an error occurs.
	 */
	@Nullable
	private static String parseOverride(@Nonnull NonreplaceableBlockOverride override)
	{
		switch(override.overrideType)
		{
			case ADD -> {
				return "+" + BlockWrapper.getBlockIdStr(override.overriddenBlock.getBlock()) + Block.getId(override.overriddenBlock);
			}
			case REMOVE -> {
				return "-" + BlockWrapper.getBlockIdStr(override.overriddenBlock.getBlock()) + Block.getId(override.overriddenBlock);
			}
			default -> {
				CCubesCore.logger.error("I have no idea how this managed to fall through to the default...");
				return null;
			}
		}
	}

	/**
	 * Reloads config file and rebuilds {@link CCubesSettings#nonReplaceableBlocks} from {@link CCubesSettings#nonReplaceableBlocksIMC} and {@link CCubesSettings#nonReplaceableBlocksOverrides}.
	 */
	public static void loadOverrides()
	{
		try
		{
			purgeOverrides();
			CCubesSettings.nonReplaceableBlocks = CCubesSettings.nonReplaceableBlocksIMC;
			for(NonreplaceableBlockOverride override : CCubesSettings.nonReplaceableBlocksOverrides)
			{
				switch(override.overrideType)
				{
					case ADD -> {
						if(!CCubesSettings.nonReplaceableBlocks.contains(override.overriddenBlock))
						{
							CCubesSettings.nonReplaceableBlocks.add(override.overriddenBlock);
							CCubesCore.logger.info("Adding " + BlockWrapper.getBlockIdStr(override.overriddenBlock.getBlock()) + " to NRB array.");
						}
						else
						{
							CCubesCore.logger.info(BlockWrapper.getBlockIdStr(override.overriddenBlock.getBlock()) + " already exists in the NRB array, skipping.");
						}
					}
					case REMOVE -> {
						if(CCubesSettings.nonReplaceableBlocks.contains(override.overriddenBlock))
						{
							CCubesSettings.nonReplaceableBlocks.remove(override.overriddenBlock);
							CCubesCore.logger.info("Removing " + BlockWrapper.getBlockIdStr(override.overriddenBlock.getBlock()) + " from NRB array.");
						}
						else
						{
							CCubesCore.logger.info(BlockWrapper.getBlockIdStr(override.overriddenBlock.getBlock()) + " has already been removed from the NRB array, skipping.");
						}
					}
					default -> CCubesCore.logger.error("Something has gone horribly awry, #BlameDaemonumbra!");
				}
			}
		} catch(Exception ex)
		{
			CCubesCore.logger.warn("Whoops, something went wrong with loading the config, replacing NRB array with safety template...");
			CCubesSettings.nonReplaceableBlocks = CCubesSettings.nonReplaceableBlocksIMC;
			CCubesSettings.nonReplaceableBlocks.addAll(CCubesSettings.backupNRB);
		}

		CCubesSettings.nonReplaceableBlocks.add(CCubesBlocks.CHANCE_CUBE.get().defaultBlockState());
		CCubesSettings.nonReplaceableBlocks.add(CCubesBlocks.GIANT_CUBE.get().defaultBlockState());
		//CCubesSettings.nonReplaceableBlocks.add(CCubesBlocks.CHANCE_ICOSAHEDRON.get().defaultBlockState());
	}

	/**
	 * Purges {@link CCubesSettings#nonReplaceableBlocks} of blocks that are no longer added by {@link CCubesSettings#nonReplaceableBlocksOverrides}.
	 */
	private static void purgeOverrides()
	{
		List<BlockState> blocksToRemove = new ArrayList<>();
		for(BlockState toRemove : CCubesSettings.nonReplaceableBlocks)
		{
			if(noLongerExists(toRemove))
			{
				blocksToRemove.add(toRemove);
				CCubesCore.logger.info("Removing " + BlockWrapper.getBlockIdStr(toRemove.getBlock()) + " from Overrides list.");
			}
		}
		CCubesSettings.nonReplaceableBlocks.removeAll(blocksToRemove);
	}

	/**
	 * Determines if a {@link net.minecraft.world.level.block.state.BlockState} is still present in the {@link CCubesSettings#nonReplaceableBlocksOverrides} list.
	 *
	 * @param toDetect The {@link net.minecraft.world.level.block.state.BlockState} to check for.
	 * @return Should be self explanatory...
	 */
	private static boolean noLongerExists(BlockState toDetect)
	{
		for(NonreplaceableBlockOverride override : CCubesSettings.nonReplaceableBlocksOverrides)
		{
			if(override.overriddenBlock == toDetect)
			{
				return false;
			}
		}
		return true;
	}

	public enum OverrideType
	{
		ADD, REMOVE, MODIFY // Not used, here just in case
	}
}
