package dev.docvin.legendofelements.registry.data;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;

public interface IRegistryDataInteractions<T extends Interaction> {

    public abstract String getRegId();

    public abstract BuilderCodec<T> getCodec();
}
