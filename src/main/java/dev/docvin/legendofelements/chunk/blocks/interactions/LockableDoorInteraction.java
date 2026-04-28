package dev.docvin.legendofelements.chunk.blocks.interactions;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.shape.Box;
import com.hypixel.hytale.math.util.ChunkUtil;
import com.hypixel.hytale.math.util.MathUtil;
import com.hypixel.hytale.math.util.TrigMathUtil;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.InteractionState;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.server.core.asset.type.blockhitbox.BlockBoundingBoxes;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.BlockType;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.Rotation;
import com.hypixel.hytale.server.core.asset.type.blocktype.config.RotationTuple;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.client.SimpleBlockInteraction;
import com.hypixel.hytale.server.core.universe.world.SoundUtil;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.accessor.ChunkAccessor;
import com.hypixel.hytale.server.core.universe.world.chunk.BlockComponentChunk;
import com.hypixel.hytale.server.core.universe.world.chunk.WorldChunk;
import com.hypixel.hytale.server.core.universe.world.storage.ChunkStore;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.FillerBlockUtil;
import dev.docvin.legendofelements.chunk.blocks.components.LockableBlockComponent;
import dev.docvin.legendofelements.registry.data.IRegistryDataInteractions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

//todo have class extend Door Interaction class rather than making a copy of it.
public class LockableDoorInteraction extends SimpleBlockInteraction implements IRegistryDataInteractions<LockableDoorInteraction> {

    private static final String OPEN_DOOR_IN = "OpenDoorIn";
    private static final String OPEN_DOOR_OUT = "OpenDoorOut";
    private static final String CLOSE_DOOR_IN = "CloseDoorIn";
    private static final String CLOSE_DOOR_OUT = "CloseDoorOut";
    private static final String DOOR_BLOCKED = "DoorBlocked";


    private boolean horizontal;

    private boolean isHorizontalDoor(@Nonnull BlockType blockType) {
        String rootInteractionId = blockType.getInteractions().get(InteractionType.Use);
        if (rootInteractionId != null) {
            RootInteraction rootInteraction = RootInteraction.getAssetMap().getAsset(rootInteractionId);
            if (rootInteraction != null) {
                for (String interactionId : rootInteraction.getInteractionIds()) {
                    Interaction interaction = Interaction.getAssetMap().getAsset(interactionId);
                    if (interaction instanceof LockableDoorInteraction lockableDoorInteraction) {
                        return lockableDoorInteraction.horizontal;
                    }
                }

            }
        }
        return false;
    }

    @Nullable
    private BlockType activateDoor(@Nonnull World world, @Nonnull BlockType blockType, @Nonnull Vector3i blockPosition, @Nonnull LockableDoorInteraction.DoorState fromState, @Nonnull LockableDoorInteraction.DoorState doorState) {
        int rotationIndex = world.getBlockRotationIndex(blockPosition.x, blockPosition.y, blockPosition.z);
        BlockBoundingBoxes oldHitbox = BlockBoundingBoxes.getAssetMap().getAsset(blockType.getHitboxTypeIndex());
        String interactionStateToSend = getInteractionState(fromState, doorState);
        world.setBlockInteractionState(blockPosition, blockType, interactionStateToSend);
        BlockType currentBlockType = world.getBlockType(blockPosition);
        if (currentBlockType == null) {
            return null;
        } else {
            BlockType newBlockType = currentBlockType.getBlockForState(interactionStateToSend);
            if (oldHitbox != null) {
                FillerBlockUtil.forEachFillerBlock(oldHitbox.get(rotationIndex), (x, y, z) -> world.performBlockUpdate(blockPosition.x + x, blockPosition.y + y, blockPosition.z + z));
            }

            if (newBlockType != null) {
                BlockBoundingBoxes newHitbox = BlockBoundingBoxes.getAssetMap().getAsset(newBlockType.getHitboxTypeIndex());
                if (newHitbox != null && newHitbox != oldHitbox) {
                    FillerBlockUtil.forEachFillerBlock(newHitbox.get(rotationIndex), (x, y, z) -> world.performBlockUpdate(blockPosition.x + x, blockPosition.y + y, blockPosition.z + z));
                }
            }

            return newBlockType;
        }
    }

    @Nullable
    private LockableDoorInteraction.DoorInfo getDoubleDoor(@Nonnull ChunkAccessor<WorldChunk> chunkAccessor, @Nonnull Vector3i worldPosition, @Nonnull BlockType blockType, int rotation, @Nonnull LockableDoorInteraction.DoorState doorStateToCheck) {
        Item blockTypeItem = blockType.getItem();
        if (blockTypeItem == null) {
            return null;
        } else {
            BlockType blockTypeItemAsset = BlockType.getAssetMap().getAsset(blockTypeItem.getId());
            if (blockTypeItemAsset == null) {
                return null;
            } else {
                int hitboxTypeIndex = blockTypeItemAsset.getHitboxTypeIndex();
                BlockBoundingBoxes blockBoundingBoxes = BlockBoundingBoxes.getAssetMap().getAsset(hitboxTypeIndex);
                if (blockBoundingBoxes == null) {
                    return null;
                } else {
                    BlockBoundingBoxes.RotatedVariantBoxes baseBoxes = blockBoundingBoxes.get(Rotation.None, Rotation.None, Rotation.None);
                    Vector3i offset = new Vector3i((int) baseBoxes.getBoundingBox().getMax().x * 2 - 1, 0, 0);
                    Rotation rotationToCheck = RotationTuple.get(rotation).yaw();
                    Vector3i blockPosition = worldPosition.clone().add(MathUtil.rotateVectorYAxis(offset, rotationToCheck.getDegrees(), false));
                    LockableDoorInteraction.DoorInfo matchingDoor = getDoorAtPosition(chunkAccessor, blockPosition.x, blockPosition.y, blockPosition.z, rotationToCheck.flip());
                    if (matchingDoor != null && matchingDoor.doorState == doorStateToCheck) {
                        BlockType matchingBlockType = matchingDoor.blockType;
                        if (matchingDoor.filler != 0) {
                            return null;
                        } else {
                            Item item = matchingBlockType.getItem();
                            assert item != null;

                            BlockType type = BlockType.getAssetMap().getAsset(item.getId());
                            assert type != null;

                            int matchingDoorHitboxIndex = type.getHitboxTypeIndex();
                            return matchingDoorHitboxIndex == hitboxTypeIndex ? matchingDoor : null;
                        }
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    @Nullable
    private LockableDoorInteraction.DoorInfo getDoorAtPosition(@Nonnull ChunkAccessor<WorldChunk> chunkAccessor, int x, int y, int z, @Nonnull Rotation rotationToCheck) {
        WorldChunk chunk = chunkAccessor.getChunkIfInMemory(ChunkUtil.indexChunkFromBlock(x, z));
        if (chunk == null) {
            return null;
        } else {
            BlockType blockType = chunk.getBlockType(x, y, z);
            if (blockType != null && blockType.isDoor()) {
                RotationTuple blockRotation = RotationTuple.get(chunk.getWorld().getBlockRotationIndex(x, y, z));
                String blockState = blockType.getStateForBlock(blockType);
                LockableDoorInteraction.DoorState doorState = LockableDoorInteraction.DoorState.fromBlockState(blockState);
                Rotation doorRotation = blockRotation.yaw();

                int filler = chunk.getFiller(x, y, z);
                return doorRotation != rotationToCheck ? null : new LockableDoorInteraction.DoorInfo(blockType, filler, new Vector3i(x, y, z), doorState);
            } else {
                return null;
            }
        }
    }

    private boolean isInFrontOfDoor(@Nonnull Vector3i blockPosition, @Nullable Rotation doorRotationYaw, @Nonnull Vector3d playerPosition) {
        double doorRotationRad = Math.toRadians(doorRotationYaw != null ? (double) doorRotationYaw.getDegrees() : (double) 0.0F);
        Vector3d doorRotationVector = new Vector3d(TrigMathUtil.sin(doorRotationRad), 0.0, TrigMathUtil.cos(doorRotationRad));
        Vector3d direction = Vector3d.directionTo(blockPosition, playerPosition);
        return direction.dot(doorRotationVector) < (double) 0.0F;
    }

    @Nonnull
    private String getInteractionState(@Nonnull LockableDoorInteraction.DoorState fromState, @Nonnull LockableDoorInteraction.DoorState doorState) {
        String stateToSend;
        if (doorState == LockableDoorInteraction.DoorState.CLOSED && fromState == LockableDoorInteraction.DoorState.OPENED_IN) {
            stateToSend = CLOSE_DOOR_OUT;
        } else if (doorState == LockableDoorInteraction.DoorState.CLOSED && fromState == LockableDoorInteraction.DoorState.OPENED_OUT) {
            stateToSend = CLOSE_DOOR_IN;
        } else if (doorState == LockableDoorInteraction.DoorState.OPENED_IN) {
            stateToSend = OPEN_DOOR_OUT;
        } else {
            stateToSend = OPEN_DOOR_IN;
        }

        return stateToSend;
    }

    @Nonnull
    private LockableDoorInteraction.DoorState getOppositeDoorState(@Nonnull LockableDoorInteraction.DoorState doorState) {
        return doorState == LockableDoorInteraction.DoorState.OPENED_OUT ? LockableDoorInteraction.DoorState.OPENED_IN : (doorState == LockableDoorInteraction.DoorState.OPENED_IN ? LockableDoorInteraction.DoorState.OPENED_OUT : LockableDoorInteraction.DoorState.CLOSED);
    }

    private boolean canOpenDoor(@Nonnull ChunkAccessor<WorldChunk> chunkAccessor, @Nonnull Vector3i blockPosition, @Nonnull String state) {
        WorldChunk chunk = chunkAccessor.getChunk(ChunkUtil.indexChunkFromBlock(blockPosition.x, blockPosition.z));
        if (chunk == null) {
            return false;
        } else {
            System.out.println("Can Open?");
            int blockId = chunk.getBlock(blockPosition.x, blockPosition.y, blockPosition.z);
            BlockType originalBlockType = BlockType.getAssetMap().getAsset(blockId);
            if (originalBlockType == null) {
                return false;
            } else {
                BlockType variantBlockType = originalBlockType.getBlockForState(state);
                if (variantBlockType == null) {
                    return false;
                } else {
                    int rotation = chunk.getWorld().getBlockRotationIndex(blockPosition.x, blockPosition.y, blockPosition.z);
                    return chunkAccessor.testPlaceBlock(blockPosition.x, blockPosition.y, blockPosition.z, variantBlockType, rotation, (blockX, blockY, blockZ, blockType, _rotation, filler) -> {
                        if (filler != 0) {
                            blockX -= FillerBlockUtil.unpackX(filler);
                            blockY -= FillerBlockUtil.unpackY(filler);
                            blockZ -= FillerBlockUtil.unpackZ(filler);
                        }

                        return blockX == blockPosition.x && blockY == blockPosition.y && blockZ == blockPosition.z;
                    });
                }
            }
        }
    }

    protected void interactWithBlock(@Nonnull World world, @Nonnull CommandBuffer<EntityStore> commandBuffer, @Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack itemInHand, @Nonnull Vector3i targetBlock, @Nonnull CooldownHandler cooldownHandler) {
        WorldChunk chunk = world.getChunkIfInMemory(ChunkUtil.indexChunkFromBlock(targetBlock.x, targetBlock.z));
        if (chunk != null) {
            BlockType blockType = chunk.getBlockType(targetBlock);
            assert blockType != null;

            int rotation = chunk.getWorld().getBlockRotationIndex(targetBlock.x, targetBlock.y, targetBlock.z);
            RotationTuple rotationTuple = RotationTuple.get(rotation);
            String blockState = blockType.getStateForBlock(blockType);
            LockableDoorInteraction.DoorState doorState = LockableDoorInteraction.DoorState.fromBlockState(blockState);
            Ref<EntityStore> ref = context.getEntity();
            TransformComponent transformComponent = commandBuffer.getComponent(ref, TransformComponent.getComponentType());

            assert transformComponent != null;

            Vector3d entityPosition = transformComponent.getPosition();
            LockableDoorInteraction.DoorState newDoorState;
            if (doorState != LockableDoorInteraction.DoorState.CLOSED) {
                newDoorState = LockableDoorInteraction.DoorState.CLOSED;
            } else if (!this.horizontal && isInFrontOfDoor(targetBlock, rotationTuple.yaw(), entityPosition)) {
                newDoorState = LockableDoorInteraction.DoorState.OPENED_OUT;
            } else {
                newDoorState = LockableDoorInteraction.DoorState.OPENED_IN;
            }

            Store<ChunkStore> chunkStore = world.getChunkStore().getStore();
            BlockComponentChunk blockComponentChunk = chunkStore.getComponent(chunk.getReference(), BlockComponentChunk.getComponentType());
            boolean locked = false;

            if (blockComponentChunk != null && !doorState.equals(DoorState.CLOSED)) {
                Ref<ChunkStore> blockRef = blockComponentChunk.getEntityReference(chunk.getBlock(targetBlock));
                if (blockRef != null) {
                    LockableBlockComponent lockableBlockComponent = chunkStore.getComponent(blockRef, LockableBlockComponent.getComponentType());
                    if (lockableBlockComponent != null)
                        locked = lockableBlockComponent.isLocked();
                }
            }

            if (locked) {
                context.getState().state = InteractionState.Failed;
                return;
            }

            LockableDoorInteraction.DoorState checkResult = this.checkDoor(world, targetBlock, blockType, rotation, doorState, newDoorState);
            if (checkResult == null) {
                context.getState().state = InteractionState.Failed;
            } else {
                LockableDoorInteraction.DoorState stateDoubleDoor = getOppositeDoorState(doorState);
                BlockType interactionBlockState = activateDoor(world, blockType, targetBlock, doorState, checkResult);
                boolean doubleDoor = this.checkForDoubleDoor(world, targetBlock, blockType, rotation, checkResult, stateDoubleDoor);
                if (interactionBlockState != null) {
                    Vector3d pos = new Vector3d();

                    Item item = blockType.getItem();
                    assert item != null;

                    BlockType blockType1 = BlockType.getAssetMap().getAsset(item.getId());
                    assert blockType1 != null;

                    int hitboxTypeIndex = blockType1.getHitboxTypeIndex();
                    BlockBoundingBoxes blockBoundingBoxes = BlockBoundingBoxes.getAssetMap().getAsset(hitboxTypeIndex);
                    assert blockBoundingBoxes != null;

                    BlockBoundingBoxes.RotatedVariantBoxes rotatedBoxes = blockBoundingBoxes.get(rotation);
                    Box hitbox = rotatedBoxes.getBoundingBox();
                    if (doubleDoor) {
                        Vector3d offset = new Vector3d(hitbox.middleX(), 0.0, 0.0);
                        Rotation rotationToCheck = RotationTuple.get(rotation).yaw();
                        pos.add(MathUtil.rotateVectorYAxis(offset, rotationToCheck.getDegrees(), false));
                        pos.add(hitbox.middleX(), hitbox.middleY(), hitbox.middleZ());
                    } else {
                        pos.add(hitbox.middleX(), hitbox.middleY(), hitbox.middleZ());
                    }

                    pos.add(targetBlock);
                    SoundUtil.playSoundEvent3d(ref, interactionBlockState.getInteractionSoundEventIndex(), pos, commandBuffer);
                }

            }
        }
    }

    protected void simulateInteractWithBlock(@Nonnull InteractionType type, @Nonnull InteractionContext context, @Nullable ItemStack itemInHand, @Nonnull World world, @Nonnull Vector3i targetBlock) {
    }

    private boolean checkForDoubleDoor(@Nonnull World world, @Nonnull Vector3i blockPosition, @Nonnull BlockType blockType, int rotation, @Nonnull LockableDoorInteraction.DoorState fromState, @Nonnull LockableDoorInteraction.DoorState doorStateToCheck) {
        LockableDoorInteraction.DoorInfo doorToOpen = getDoubleDoor(world, blockPosition, blockType, rotation, doorStateToCheck);
        if (doorToOpen == null) {
            return false;
        } else {
            boolean otherDoorIsHorizontal = isHorizontalDoor(doorToOpen.blockType);
            LockableDoorInteraction.DoorState stateForDoubleDoor = otherDoorIsHorizontal ? fromState : getOppositeDoorState(fromState);
            activateDoor(world, doorToOpen.blockType, doorToOpen.blockPosition, doorToOpen.doorState, stateForDoubleDoor);
            return true;
        }
    }

    @Nullable
    private LockableDoorInteraction.DoorState checkDoor(@Nonnull ChunkAccessor<WorldChunk> chunkAccessor, @Nonnull Vector3i blockPosition, @Nonnull BlockType blockType, int rotation, @Nonnull LockableDoorInteraction.DoorState oldDoorState, @Nonnull LockableDoorInteraction.DoorState newDoorState) {
        LockableDoorInteraction.DoorInfo doubleDoor = getDoubleDoor(chunkAccessor, blockPosition, blockType, rotation, oldDoorState);
        LockableDoorInteraction.DoorState newOppositeDoorState = getOppositeDoorState(newDoorState);
        String newOppositeLockableDoorInteractionState = getInteractionState(oldDoorState, newOppositeDoorState);
        String newLockableDoorInteractionState = getInteractionState(oldDoorState, newDoorState);
        if (canOpenDoor(chunkAccessor, blockPosition, newLockableDoorInteractionState)) {
            if (!this.horizontal && doubleDoor != null && !canOpenDoor(chunkAccessor, doubleDoor.blockPosition, newOppositeLockableDoorInteractionState)) {
                if (canOpenDoor(chunkAccessor, blockPosition, newOppositeLockableDoorInteractionState) && canOpenDoor(chunkAccessor, doubleDoor.blockPosition, newLockableDoorInteractionState)) {
                    return newOppositeDoorState;
                } else {
                    chunkAccessor.setBlockInteractionState(blockPosition, blockType, DOOR_BLOCKED);
                    return null;
                }
            } else {
                return newDoorState;
            }
        } else if (canOpenDoor(chunkAccessor, blockPosition, newOppositeLockableDoorInteractionState) && !this.horizontal) {
            if (doubleDoor != null && !canOpenDoor(chunkAccessor, doubleDoor.blockPosition, newLockableDoorInteractionState)) {
                chunkAccessor.setBlockInteractionState(blockPosition, blockType, DOOR_BLOCKED);
                return null;
            } else {
                return newOppositeDoorState;
            }
        } else {
            if (newDoorState != LockableDoorInteraction.DoorState.CLOSED) {
                chunkAccessor.setBlockInteractionState(blockPosition, blockType, DOOR_BLOCKED);
            }

            return null;
        }
    }

    @Override
    public String getRegId() {
        return "Door";
    }

    @Override
    public BuilderCodec<LockableDoorInteraction> getCodec() {
        return BuilderCodec.builder(LockableDoorInteraction.class, LockableDoorInteraction::new, SimpleBlockInteraction.CODEC).documentation("Opens/Closes a door")
                .appendInherited(new KeyedCodec<>("Horizontal", Codec.BOOLEAN), (t, i) -> t.horizontal = i, (t) -> t.horizontal, (t, parent) -> t.horizontal = parent.horizontal)
                .documentation("Whether the door is horizontal (e.g. gates) or vertical (e.g. regular doors).")
                .add()
                .build();
    }


    private enum DoorState {
        CLOSED,
        OPENED_IN,
        OPENED_OUT;

        @Nonnull
        public static LockableDoorInteraction.DoorState fromBlockState(@Nullable String state) {
            if (state == null) {
                return CLOSED;
            } else {
                LockableDoorInteraction.DoorState var10000;
                switch (state) {
                    case OPEN_DOOR_OUT -> var10000 = OPENED_IN;
                    case OPEN_DOOR_IN -> var10000 = OPENED_OUT;
                    default -> var10000 = CLOSED;
                }

                return var10000;
            }
        }
    }

    private record DoorInfo(BlockType blockType, int filler, Vector3i blockPosition, DoorState doorState) {
    }
}
