package chanceCubes.mcwrapper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.PrimedTnt;
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

	public static <T extends Entity> T spawnEntity(EntityType<T> type, Level level)
	{
		T ent = type.create(level);
		if(ent != null)
			level.addFreshEntity(ent);
		return ent;
	}

	public static <T extends Entity> T spawnEntityAt(EntityType<T> type, Level level, BlockPos pos)
	{
		return spawnEntityAt(type, level, pos.getX(), pos.getY(), pos.getZ());
	}

	public static <T extends Entity> T spawnEntityAt(EntityType<T> type, Level level, double x, double y, double z)
	{
		T ent = spawnEntity(type, level);
		if(ent != null)
			ent.moveTo(x, y, z, 0, 0);
		return ent;
	}

	public static <T extends Entity> T spawnNamedEntityAt(EntityType<T> type, Level level, String name, BlockPos pos)
	{
		return spawnNamedEntityAt(type, level, name, pos.getX(), pos.getY(), pos.getZ());
	}

	public static <T extends Entity> T spawnNamedEntityAt(EntityType<T> type, Level level, String name, double x, double y, double z)
	{
		T ent = spawnEntityAt(type, level, x, y, z);
		if(ent != null)
			ent.setCustomName(ComponentWrapper.string(name));
		return ent;
	}

	public static <T extends Entity> void spawnPrimedTNT(Level level, BlockPos pos, int fuse)
	{
		PrimedTnt tnt = spawnEntityAt(EntityType.TNT, level, pos);
		if(tnt != null)
			tnt.setFuse(fuse);
	}

	public static <T extends Entity> void spawnChargedCreeper(Level level, BlockPos pos)
	{
		Creeper ent = spawnEntityAt(EntityType.CREEPER, level, pos);
		if(ent != null)
			EntityWrapper.setCreeperPowered(ent);
	}
}
