package chanceCubes.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Handles modifications to {@link CCubesSettings#nonReplaceableBlocks} after blocks have been added
 * by IMC messages.
 */
public class NonreplaceableBlockOverride
{
	public BlockState overriddenBlock;
	public OverrideType overrideType;

	public NonreplaceableBlockOverride()
	{
		overriddenBlock = Blocks.AIR.getDefaultState();
		overrideType = OverrideType.ADD;
	}

	/**
	 * Parses compatible strings into a {@link java.util.List} of {@link NonreplaceableBlockOverride
	 * NonreplaceableBlockOverrides}.
	 * 
	 * @param strings
	 *            An array of {@link java.lang.String Strings} to attempt to parse.
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
	 * @param string
	 *            The string to parse.
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
	 * Creates a {@link NonreplaceableBlockOverride} with the {@link OverrideType#REMOVE REMOVE}
	 * {@link OverrideType} from the given Block ID.
	 * 
	 * @param substring
	 *            The Block ID.
	 * @return The {@link NonreplaceableBlockOverride} produced.
	 */
	private static NonreplaceableBlockOverride removeBlock(String substring)
	{
		NonreplaceableBlockOverride output = new NonreplaceableBlockOverride();

		output.overrideType = OverrideType.REMOVE;
		Block blockActual = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(substring));
		output.overriddenBlock = blockActual.getDefaultState();

		//		if(substring.matches(".*:.*:[0-9]*"))
		//		{
		//			output.overrideType = OverrideType.REMOVE;
		//			String block;
		//			String damage;
		//			block = substring.substring(0, substring.lastIndexOf(':') - 1);
		//			damage = substring.substring(substring.lastIndexOf(':') + 1);
		//			Block blockActual = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block));
		//			output.overriddenBlock = blockActual.getDefaultState();
		//		}
		//		else
		//		{
		//			output.overrideType = OverrideType.REMOVE;
		//			Block blockActual = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(substring));
		//			output.overriddenBlock = blockActual.getDefaultState();
		//		}
		return output;
	}

	/**
	 * Creates a {@link NonreplaceableBlockOverride} with the {@link OverrideType#ADD ADD}
	 * {@link OverrideType} from the given Block ID.
	 * 
	 * @param substring
	 *            The Block ID.
	 * @return The {@link NonreplaceableBlockOverride} produced.
	 */
	private static NonreplaceableBlockOverride addBlock(String substring)
	{
		NonreplaceableBlockOverride output = new NonreplaceableBlockOverride();
		output.overrideType = OverrideType.ADD;
		Block blockActual = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(substring));
		output.overriddenBlock = blockActual.getDefaultState();
		//		if(substring.matches(".*:.*:[0-9]*"))
		//		{
		//			output.overrideType = OverrideType.ADD;
		//			String block;
		//			String damage;
		//			int damageValue;
		//			IBlockState blockState;
		//			block = substring.substring(0, substring.lastIndexOf(':'));
		//			damage = substring.substring(substring.lastIndexOf(':') + 1);
		//			damageValue = Integer.parseInt(damage);
		//			Block blockActual = Block.getBlockFromName(block);
		//			blockState = RewardsUtil.getBlockStateFromBlockMeta(blockActual, damageValue);
		//			output.overriddenBlock = blockState;
		//		}
		//		else
		//		{
		//			output.overrideType = OverrideType.ADD;
		//			IBlockState blockState;
		//			Block blockActual = Block.getBlockFromName(substring);
		//			blockState = RewardsUtil.getBlockStateFromBlockMeta(blockActual, 0);
		//			output.overriddenBlock = blockState;
		//		}
		return output;
	}

	/**
	 * Produces an array of {@link java.lang.String Strings} from a {@link java.util.List} of
	 * {@link NonreplaceableBlockOverride NonreplaceableBlockOverrides}.
	 * 
	 * @param overrides
	 *            The {@link java.util.List} of {@link NonreplaceableBlockOverride
	 *            NonreplaceableBlockOverrides}.
	 * @return The {@link java.lang.String String} array produced.
	 */
	public static String[] parseOverrides(List<NonreplaceableBlockOverride> overrides)
	{
		List<String> strings = new ArrayList<>();
		for(NonreplaceableBlockOverride override : overrides)
		{
			String toAdd = parseOverride(override);
			if(toAdd != null)
			{
				strings.add(toAdd);
			}
		}
		return (String[]) strings.toArray();
	}

	/**
	 * Produces a{@link java.lang.String} from a {@link NonreplaceableBlockOverride
	 * NonreplaceableBlockOverride}.
	 * 
	 * @param override
	 *            The {@link NonreplaceableBlockOverride NonreplaceableBlockOverride}.
	 * @return The {@link java.lang.String String} produced or null if an error occurs.
	 */
	@Nullable
	private static String parseOverride(@Nonnull NonreplaceableBlockOverride override)
	{
		switch(override.overrideType)
		{
			case ADD:
			{
				return "+" + override.overriddenBlock.getBlock().getRegistryName() + Block.getStateId(override.overriddenBlock);
			}
			case REMOVE:
			{
				return "-" + override.overriddenBlock.getBlock().getRegistryName() + Block.getStateId(override.overriddenBlock);
			}
			default:
			{
				CCubesCore.logger.error("I have no idea how this managed to fall through to the default...");
				return null;
			}
		}
	}

	/**
	 * Reloads config file and rebuilds {@link CCubesSettings#nonReplaceableBlocks} from
	 * {@link CCubesSettings#nonReplaceableBlocksIMC} and
	 * {@link CCubesSettings#nonReplaceableBlocksOverrides}.
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
					case ADD:
					{
						if(!CCubesSettings.nonReplaceableBlocks.contains(override.overriddenBlock))
						{
							CCubesSettings.nonReplaceableBlocks.add(override.overriddenBlock);
							CCubesCore.logger.info("Adding " + override.overriddenBlock.getBlock().getRegistryName() + " to NRB array.");
						}
						else
						{
							CCubesCore.logger.info(override.overriddenBlock.getBlock().getRegistryName() + " already exists in the NRB array, skipping.");
						}
						break;
					}
					case REMOVE:
					{
						if(CCubesSettings.nonReplaceableBlocks.contains(override.overriddenBlock))
						{
							CCubesSettings.nonReplaceableBlocks.remove(override.overriddenBlock);
							CCubesCore.logger.info("Removing " + override.overriddenBlock.getBlock().getRegistryName() + " from NRB array.");
						}
						else
						{
							CCubesCore.logger.info(override.overriddenBlock.getBlock().getRegistryName() + " has already been removed from the NRB array, skipping.");
						}
						break;
					}
					default:
					{
						CCubesCore.logger.error("Something has gone horribly awry, #BlameDaemonumbra!");
					}
				}
			}
		} catch(Exception ex)
		{
			CCubesCore.logger.warn("Whoops, something went wrong with loading the config, replacing NRB array with safety template...");
			CCubesSettings.nonReplaceableBlocks = CCubesSettings.nonReplaceableBlocksIMC;
			CCubesSettings.nonReplaceableBlocks.addAll(CCubesSettings.backupNRB);
		}
	}

	/**
	 * Purges {@link CCubesSettings#nonReplaceableBlocks} of blocks that are no longer added by
	 * {@link CCubesSettings#nonReplaceableBlocksOverrides}.
	 */
	private static void purgeOverrides()
	{
		List<BlockState> blocksToRemove = new ArrayList<BlockState>();
		for(BlockState toRemove : CCubesSettings.nonReplaceableBlocks)
		{
			if(noLongerExists(toRemove))
			{
				blocksToRemove.add(toRemove);
				CCubesCore.logger.info("Removing " + toRemove.getBlock().getRegistryName() + " from Overrides list.");
			}
		}
		CCubesSettings.nonReplaceableBlocks.removeAll(blocksToRemove);
	}

	/**
	 * Determines if a {@link net.minecraft.block.BlockState} is still present in the
	 * {@link CCubesSettings#nonReplaceableBlocksOverrides} list.
	 * 
	 * @param toDetect
	 *            The {@link net.minecraft.block.BlockState} to check for.
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
