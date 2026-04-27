package dev.docvin.legendofelements.registry.data;

import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.server.core.universe.world.WorldProvider;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface Component<T extends com.hypixel.hytale.component.Component<K>, K extends WorldProvider> extends com.hypixel.hytale.component.Component<K> {

    @SuppressWarnings("unchecked")
    static <T extends com.hypixel.hytale.component.Component<K>, K extends WorldProvider> BuilderCodec<T> getBuilderCodec(Class<? extends Component<?, ?>> component) throws NoSuchFieldException, IllegalAccessException {
        Field field = component.getDeclaredField("CODEC");
        field.setAccessible(true);
        return (BuilderCodec<T>) field.get(component);
    }

    @SuppressWarnings("unchecked")
    default BuilderCodec<T> getCodec() {
        BuilderCodec<T> codec;
        try {
            codec = getBuilderCodec((Class<? extends Component<?, ?>>) this.getClass());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return codec;
    }

    void setComponentType(ComponentType<K, T> staticComponentType);

    String getComponentId();

    @SuppressWarnings("unchecked")
    default Class<K> getProviderClass() {
        Type[] interfaces = getClass().getGenericInterfaces();
        for (Type interfaceType : interfaces) {
            if (interfaceType instanceof ParameterizedType parameterized) {
                //Magic number 1, label K will always be the index of 1.
                return (Class<K>) parameterized.getActualTypeArguments()[1];
            }
        }
        throw new RuntimeException("Could not resolve generic Label");
    }


}
