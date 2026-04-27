package dev.docvin.legendofelements;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.math.shape.Box;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.asset.type.model.config.camera.CameraAxis;
import com.hypixel.hytale.server.core.asset.type.model.config.camera.CameraSettings;
import com.hypixel.hytale.server.core.io.adapter.PacketFilter;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.docvin.legendofelements.commands.SpawnItemKey;
import dev.docvin.legendofelements.commands.SpawnItemLock;
import dev.docvin.legendofelements.registry.*;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegendOfTheElementsPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private static LegendOfTheElementsPlugin instance;
    private PacketFilter filter;

    public LegendOfTheElementsPlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from " + this.getName() + " version " + this.getManifest().getVersion().toString());

    }

    public static LegendOfTheElementsPlugin get() {
        return instance;
    }

    private static List<ModelAsset> getModelAssets() {
        List<ModelAsset> modelAssetList = new ArrayList<>();
        ModelAsset modelAsset = new ModelAsset() {
            {
                this.id = "Test";
                this.model = "Blocks/Sliding_Block.blockymodel";
                this.texture = "Items/Texture.png";
                this.camera = new CameraSettings((com.hypixel.hytale.protocol.Vector3f) null, CameraAxis.STATIC_HEAD, CameraAxis.STATIC_HEAD);
                this.boundingBox = Box.horizontallyCentered(0.5, 0.5, 0.5);
                this.minScale = 1.0F;
                this.maxScale = 1.0F;
            }

            @Override
            public Map<String, AnimationSet> getAnimationSetMap() {
                Map<String, AnimationSet> map = new HashMap<>();
                map.put("empty", new AnimationSet());
                return map;
            }
        };
        modelAssetList.add(modelAsset);
        return modelAssetList;
    }

    @Override
    protected void setup() {
        instance = this;
        LOGGER.atInfo().log("Setting up plugin " + this.getName());
        //filter = PacketAdapters.registerInbound(new UsageKeysPressed());

        try {
            AllInteractions.registerInteractions();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        AllAssets.register();

        AllEvents.register();

        AllComponents.register();
        AllSystems.register();
        getCommandRegistry().registerCommand(new ExampleCommand("", ""));
        getCommandRegistry().registerCommand(new SpawnItemKey());
        getCommandRegistry().registerCommand(new SpawnItemLock());

        List<ModelAsset> modelAssetList = getModelAssets();
        ModelAsset.getAssetStore().loadAssets(LegendOfTheElementsPlugin.get().getName(), modelAssetList);


    }


    @Override
    protected void start() {
    }


    @Override
    protected void shutdown() {
        LOGGER.atInfo().log("Shutting down plugin " + this.getName());
        //PacketAdapters.deregisterInbound(filter);
    }

}