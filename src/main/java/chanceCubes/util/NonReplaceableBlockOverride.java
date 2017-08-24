package chanceCubes.util;

import chanceCubes.CCubesCore;
import chanceCubes.config.CCubesSettings;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class NonReplaceableBlockOverride {
    boolean overrideType; //True for add, False for remove
    IBlockState overriddenBlock;
    int damagevalue;

    NonReplaceableBlockOverride(boolean providedOverrideType, String providedBlock,int providedDamage){
        overrideType = providedOverrideType;
        damagevalue = providedDamage;
        Block block = Block.getBlockFromName(providedBlock);
        overriddenBlock = RewardsUtil.getBlockStateFromBlockMeta(block, damagevalue);
    }

    public static List<NonReplaceableBlockOverride> parseOverrides(String[] inputs){
        List<NonReplaceableBlockOverride> ListChanges = new ArrayList<>();
        try {
            for (String block : inputs) {
                String trimmedBlock = null;
                    switch(block.toCharArray()[0]){
                        case '-':{
                            trimmedBlock = block.substring(1);
                            if(trimmedBlock.matches(".*:.*:\\d*")){
                                int damageStart = trimmedBlock.lastIndexOf(':');
                                int damage = Integer.parseInt(trimmedBlock.substring(damageStart+1));
                                ListChanges.add(new NonReplaceableBlockOverride(false,trimmedBlock.substring(0,damageStart-1),damage));
                            }else{
                                ListChanges.add(new NonReplaceableBlockOverride(false,trimmedBlock,0));
                            }
                            break;
                        }
                        case '+':
                            trimmedBlock = block.substring(1);
                            if(trimmedBlock.matches(".*:.*:\\d*")){
                                int damageStart = trimmedBlock.lastIndexOf(':');
                                int damage = Integer.parseInt(trimmedBlock.substring(damageStart+1));
                                ListChanges.add(new NonReplaceableBlockOverride(true,trimmedBlock.substring(0,damageStart-1),damage));
                            }else{
                                ListChanges.add(new NonReplaceableBlockOverride(true,trimmedBlock,0));
                            }
                            break;
                        default:{
                            if(trimmedBlock == null){
                                trimmedBlock = block;
                            }
                            if(trimmedBlock.matches(".*:.*:\\d*")){
                                int damageStart = trimmedBlock.lastIndexOf(':');
                                int damage = Integer.parseInt(trimmedBlock.substring(damageStart+1));
                                ListChanges.add(new NonReplaceableBlockOverride(true,trimmedBlock.substring(0,damageStart-1),damage));
                            }else{
                                ListChanges.add(new NonReplaceableBlockOverride(true,trimmedBlock,0));
                            }
                            break;
                        }
                    }
            }
        }catch(Exception e){
            CCubesCore.logger.warn("Unable to parse Non-Replaceable Block overrides from config file, aborting changes.");
            return new ArrayList<>();
        }
        return ListChanges;
    }

    public static void applyOverrides()
    {
        CCubesCore.logger.info("Applying Non-Replaceable Block overrides");
        for(NonReplaceableBlockOverride override : CCubesSettings.nonReplaceableBlockOverrides){
            if(override.overrideType){ //Add
                CCubesSettings.nonReplaceableBlocks.add(override.overriddenBlock);
            }
            else{ //Remove
                if(CCubesSettings.nonReplaceableBlocks.contains(override.overriddenBlock)){
                    CCubesSettings.nonReplaceableBlocks.remove(override.overriddenBlock);
                }
            }
        }
    }
}
