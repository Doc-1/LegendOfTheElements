package dev.docvin.legendofelements.entity.velocity.components;

import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import javax.annotation.Nullable;

public class AccelerateComponent implements Component<EntityStore> {

    private static ComponentType<EntityStore, AccelerateComponent> componentType;
    private Vector3d originPosition;
    private Vector3d finalPosition;

    public static ComponentType<EntityStore, AccelerateComponent> getComponentType() {
        return componentType;
    }

    public static void setComponentType(ComponentType<EntityStore, AccelerateComponent> modifyVelocityComponentType) {
        componentType = modifyVelocityComponentType;
    }

    public Vector3d getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(Vector3d finalPosition) {
        this.finalPosition = finalPosition;
    }

    public Vector3d getOriginPosition() {
        return originPosition;
    }

    public void setOriginPosition(Vector3d originPosition) {
        this.originPosition = originPosition;
    }

    public Vector3d getVelocity() {
        Vector3d finalNormalized = finalPosition.normalize();
        Vector3d originNormalized = originPosition.normalize();
        
        return finalNormalized.subtract(originNormalized).normalize();
    }

    @Nullable
    @Override
    public Component<EntityStore> clone() {
        return new AccelerateComponent();
    }
}
