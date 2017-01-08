package chanceCubes.util;

import chanceCubes.registry.ChanceCubeRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ICrashCallable;

/**
 * Created by TimeTheCat on 1/8/2017.
 */
public class CCubesCrashCallable implements ICrashCallable {

    public static void init() {
        FMLCommonHandler.instance().registerCrashCallable(new CCubesCrashCallable());
    }

    @Override
    public String getLabel() {
        return "Last opened chance cube";
    }

    @Override
    public String call() throws Exception {
        return (ChanceCubeRegistry.getLastReward() != null) ? ChanceCubeRegistry.getLastReward().getName() : "None";
    }
}
