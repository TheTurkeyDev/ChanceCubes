package chanceCubes.mcwrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityWrapper
{
	public static void spawnLightning(Level level, BlockPos pos)
	{
		LightningBolt lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(level);
		if(lightningBoltEntity == null)
			return;
		lightningBoltEntity.moveTo(new Vec3(pos.getX(), pos.getY(), pos.getZ()));
		lightningBoltEntity.setVisualOnly(false);
		level.addFreshEntity(lightningBoltEntity);
	}

	public static void setCreeperPowered(Creeper creeper)
	{
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("powered", true);
		creeper.readAdditionalSaveData(tag);
	}
}
