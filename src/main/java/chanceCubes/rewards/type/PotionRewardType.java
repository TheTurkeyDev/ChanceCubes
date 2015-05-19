package chanceCubes.rewards.type;

import lombok.Value;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class PotionRewardType extends BaseRewardType<PotionEffect>
{
    /**
     * This is real useful and all, but really hard to use from an API standpoint. Going to switch to raw PotionEffects
     */
    @Value
    @Deprecated
    public static class PotionData
    {
        private String potionEffect;
        private boolean tier, duration;

        /**
         * Reference: http://minecraft.gamepedia.com/Data_values#Potions
         * 
         * @return The damage value for this data
         */
        public int getDamage(boolean splash)
        {
            int damage = getDamageFromEffect(potionEffect);
            damage |= bitFor(tier, 5);
            damage |= bitFor(duration, 6);
            damage |= bitFor(splash, 14);
            return damage;
        }

        private int getDamageFromEffect(String effect)
        {
            int idx = effect.lastIndexOf('&');
            if (idx >= 0)
            {
                effect = effect.substring(0, effect.lastIndexOf('&'));
            }
            boolean set = false;
            int damage = 0;
            for (char c : effect.toCharArray())
            {
                if (c == '+' || c == '-')
                {
                    set = c == '+';
                }
                else
                {
                    damage |= bitFor(set, Integer.valueOf(String.valueOf(c)));
                }
            }
            return damage;
        }

        private int bitFor(boolean bool, int shift)
        {
            return (bool ? 1 : 0) << shift;
        }
    }
    
    public PotionRewardType(PotionEffect... effects)
    {
        super(effects);
    }

    @Override
    public void trigger(PotionEffect effect, World world, int x, int y, int z, EntityPlayer player)
    {
        ItemStack potion = new ItemStack(Items.potionitem);
        NBTTagList effects = new NBTTagList();
        NBTTagCompound effectData = new NBTTagCompound();
        effect.writeCustomPotionEffectToNBT(effectData);
        effects.appendTag(effectData);
        potion.stackTagCompound = new NBTTagCompound();
        potion.stackTagCompound.setTag("CustomPotionEffects", effects);

        EntityPotion entity = new EntityPotion(world, player, potion);
        entity.posX = player.posX;
        entity.posY = player.posY + 2;
        entity.posZ = player.posZ;
        entity.motionX = 0;
        entity.motionY = 0.1;
        entity.motionZ = 0;

        world.spawnEntityInWorld(entity);
    }
}
