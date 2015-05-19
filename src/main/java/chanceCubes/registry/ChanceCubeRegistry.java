package chanceCubes.registry;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import chanceCubes.CCubesCore;
import chanceCubes.items.CCubesItems;
import chanceCubes.items.ItemChancePendant;
import chanceCubes.rewards.BasicReward;
import chanceCubes.rewards.IChanceCubeReward;
import chanceCubes.rewards.type.EntityRewardType;
import chanceCubes.rewards.type.ExperienceRewardType;
import chanceCubes.rewards.type.ItemRewardType;
import chanceCubes.rewards.type.MessageRewardType;
import chanceCubes.rewards.type.PotionRewardType;

import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.Bound;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChanceCubeRegistry implements IRewardRegistry
{
    private static final Bound<Integer> LUCK_BOUND = Bound.of(-100, 100);
    
    public static ChanceCubeRegistry INSTANCE = new ChanceCubeRegistry();
    
	private Map<String, IChanceCubeReward> nameToReward = Maps.newHashMap();
	private List<IChanceCubeReward> sortedRewards = Lists.newArrayList();
	
	/**
	 * loads the default rewards of the Chance Cube
	 */
	public static void loadDefaultRewards()
	{
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":RedstoneDiamond", -75, new ItemRewardType(new ItemStack(Items.redstone), new ItemStack(Items.diamond))));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Creeper", 0, new EntityRewardType("Creeper")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":RedstoneZombie", 100, new ItemRewardType(new ItemStack(Items.redstone)), new EntityRewardType("Zombie")));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":EXP", 25, new ExperienceRewardType(100)));
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":Potions", 0, new PotionRewardType(new PotionEffect(Potion.poison.id, 16 * 20))));		
		INSTANCE.registerReward(new BasicReward(CCubesCore.MODID+":ChatMessage", 0, new MessageRewardType("You have escaped the wrath of the Chance Cubes.........", "For now......")));
	}

    @Override
    public void registerReward(IChanceCubeReward reward)
    {
        nameToReward.put(reward.getName(), reward);
        redoSort(reward);
    }

    @Override
    public boolean unregisterReward(String name)
    {
        return nameToReward.remove(name) != null;
    }

    @Override
    public IChanceCubeReward getRewardByName(String name)
    {
        return nameToReward.get(name);
    }

    @Override
    public void triggerRandomReward(World world, BlockCoord pos, EntityPlayer player, int luck, Bound<Integer> luckBounds)
    {
        // Luck from pendant
        // TODO this is a mess...
		if (player != null && player.inventory.hasItem(CCubesItems.chancePendant))
		{
			for (int i = 0; i < player.inventory.mainInventory.length; i++)
			{
				ItemStack stack = player.inventory.mainInventory[i];
				if (stack != null && stack.getItem().equals(CCubesItems.chancePendant))
				{
					ItemChancePendant pendant = (ItemChancePendant) stack.getItem();
					
					if (stack.getItemDamage() == 0)
					{
						int lapisCounter = 0;
						for (int slotCounter = 0; slotCounter < player.inventory.mainInventory.length; slotCounter++)
						{
							ItemStack lapisStack = player.inventory.mainInventory[slotCounter];
							if (lapisStack != null && lapisStack.getItem().equals(Items.dye) && lapisStack.getItemDamage() == 4) //Lapis!
							{
								lapisCounter += lapisStack.stackSize;
								player.inventory.setInventorySlotContents(slotCounter, null); //Annnnnnnd it's gone.
							}
							else if (lapisStack != null && lapisStack.getItem().equals(Item.getItemFromBlock(Blocks.lapis_block)))
							{
								lapisCounter += 9*lapisStack.stackSize;
								player.inventory.setInventorySlotContents(slotCounter, null);
							}
						}
						luck += lapisCounter;
                    }
					
					if (stack.getItemDamage() == 1) //Not yet implemented
					{
						luck += pendant.getLuck(stack); //Stores lapis internally
						pendant.removeAllLuck(stack);
						pendant.damage(stack);
					}
				}
			}
        }
		
        int lowerIndex = 0;
        int upperIndex = sortedRewards.size() - 1;
        int lowerRange = LUCK_BOUND.clamp(luckBounds.min + luck);
        int upperRange = LUCK_BOUND.clamp(luckBounds.max + luck);
        
        System.out.println("Luck range: " + lowerRange + " - " + upperRange);

        while (sortedRewards.get(lowerIndex).getLuckValue() < lowerRange)
        {
            lowerIndex++;
        }
        while (sortedRewards.get(upperIndex).getLuckValue() > upperRange)
        {
            upperIndex--;
        }

        int pick = world.rand.nextInt(upperIndex - lowerIndex + 1) + lowerIndex;
        sortedRewards.get(pick).trigger(world, pos.x, pos.y, pos.z, player);
    }

    private void redoSort(@Nullable IChanceCubeReward newReward)
    {
        if (newReward != null)
        {
            sortedRewards.add(newReward);
        }
        
        Collections.sort(sortedRewards, new Comparator<IChanceCubeReward>()
        {
            public int compare(IChanceCubeReward o1, IChanceCubeReward o2)
            {
                return o1.getLuckValue() - o2.getLuckValue();
            };
        });
    }
}
