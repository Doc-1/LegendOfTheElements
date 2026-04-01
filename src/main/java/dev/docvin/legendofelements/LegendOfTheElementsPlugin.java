package dev.docvin.legendofelements;

import com.hypixel.hytale.assetstore.event.LoadedAssetsEvent;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.io.adapter.PacketAdapters;
import com.hypixel.hytale.server.core.io.adapter.PacketFilter;
import com.hypixel.hytale.server.core.modules.entitystats.asset.EntityStatType;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import dev.docvin.legendofelements.packets.filter.UsageKeysPressed;
import dev.docvin.legendofelements.registry.*;

import javax.annotation.Nonnull;

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

    @Override
    protected void setup() {
        instance = this;
        LOGGER.atInfo().log("Setting up plugin " + this.getName());
        filter = PacketAdapters.registerInbound(new UsageKeysPressed());

        AllCodecs.registerInteractions();
        AllAssets.register();

        AllEvents.register();

        AllComponents.register();
        AllSystems.register();
        getCommandRegistry().registerCommand(new ExampleCommand("", ""));
        getEventRegistry().register(LoadedAssetsEvent.class, EntityStatType.class, this::onStatsLoaded);

    }

    private void onStatsLoaded(LoadedAssetsEvent event) {
        System.out.println(EntityStatType.getAssetMap().getIndex("hytale:movement_speed"));
    }

    @Override
    protected void start() {
    }


    @Override
    protected void shutdown() {
        LOGGER.atInfo().log("Shutting down plugin " + this.getName());
        PacketAdapters.deregisterInbound(filter);
    }

}