package net.astralya.hexalia.item.data;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.UUID;

public final class SpiritrootTetherData {

    private static final String TAG_KEY = "SpiritrootTether";
    private static final String KEY_DIMENSION = "Dimension";
    private static final String KEY_X = "X";
    private static final String KEY_Y = "Y";
    private static final String KEY_Z = "Z";
    private static final String KEY_MOB_TYPE = "MobType";
    private static final String KEY_MOB_NBT = "MobNbt";
    private static final String KEY_MOB_UUID = "MobUuid";
    private static final String KEY_MOB_NAME = "MobName";

    private final String boundDimension;
    private final int boundX;
    private final int boundY;
    private final int boundZ;
    private final String mobType;
    private final String mobNbt;
    private final String mobUuid;
    private final String mobNameJson;

    public SpiritrootTetherData(String boundDimension, int boundX, int boundY, int boundZ,
                                String mobType, String mobNbt, String mobUuid, String mobNameJson) {
        this.boundDimension = boundDimension;
        this.boundX = boundX;
        this.boundY = boundY;
        this.boundZ = boundZ;
        this.mobType = mobType;
        this.mobNbt = mobNbt;
        this.mobUuid = mobUuid;
        this.mobNameJson = mobNameJson;
    }

    public static final SpiritrootTetherData EMPTY =
            new SpiritrootTetherData("", 0, 0, 0, "", "", "", "");

    public String boundDimension() { return boundDimension; }
    public int boundX() { return boundX; }
    public int boundY() { return boundY; }
    public int boundZ() { return boundZ; }
    public String mobType() { return mobType; }
    public String mobNbt() { return mobNbt; }
    public String mobUuid() { return mobUuid; }
    public String mobNameJson() { return mobNameJson; }

    public boolean hasMob() {
        return !this.mobType.isEmpty();
    }

    public boolean hasBound() {
        return !this.boundDimension.isEmpty();
    }

    public Optional<BoundLocation> bound() {
        if (this.boundDimension.isEmpty()) return Optional.empty();
        return Optional.of(new BoundLocation(this.boundDimension, new BlockPos(this.boundX, this.boundY, this.boundZ)));
    }

    public Optional<CapturedMob> mob() {
        if (this.mobType.isEmpty()) return Optional.empty();
        return Optional.of(new CapturedMob(
                new ResourceLocation(this.mobType),
                this.mobNbt,
                parseUuid(this.mobUuid).orElse(null),
                this.mobNameJson
        ));
    }

    public SpiritrootTetherData withBound(ResourceKey<Level> dimension, BlockPos pos) {
        ResourceLocation id = dimension.location();
        return new SpiritrootTetherData(id.toString(), pos.getX(), pos.getY(), pos.getZ(),
                this.mobType, this.mobNbt, this.mobUuid, this.mobNameJson);
    }

    public SpiritrootTetherData withCapturedMob(ResourceLocation typeId, CompoundTag nbt, UUID uuid, String nameJson) {
        String snbt = nbt.toString();
        String uuidStr = uuid != null ? uuid.toString() : "";
        String name = nameJson != null ? nameJson : "";
        return new SpiritrootTetherData(this.boundDimension, this.boundX, this.boundY, this.boundZ,
                typeId.toString(), snbt, uuidStr, name);
    }

    public SpiritrootTetherData clearedMob() {
        return new SpiritrootTetherData(this.boundDimension, this.boundX, this.boundY, this.boundZ,
                "", "", "", "");
    }

    public void save(ItemStack stack) {
        CompoundTag root = stack.getOrCreateTag();
        CompoundTag tag = new CompoundTag();
        tag.putString(KEY_DIMENSION, this.boundDimension);
        tag.putInt(KEY_X, this.boundX);
        tag.putInt(KEY_Y, this.boundY);
        tag.putInt(KEY_Z, this.boundZ);
        tag.putString(KEY_MOB_TYPE, this.mobType);
        tag.putString(KEY_MOB_NBT, this.mobNbt);
        tag.putString(KEY_MOB_UUID, this.mobUuid);
        tag.putString(KEY_MOB_NAME, this.mobNameJson);
        root.put(TAG_KEY, tag);
    }

    public static SpiritrootTetherData load(ItemStack stack) {
        CompoundTag root = stack.getTag();
        if (root == null || !root.contains(TAG_KEY)) return EMPTY;
        CompoundTag tag = root.getCompound(TAG_KEY);
        return new SpiritrootTetherData(
                tag.getString(KEY_DIMENSION),
                tag.getInt(KEY_X),
                tag.getInt(KEY_Y),
                tag.getInt(KEY_Z),
                tag.getString(KEY_MOB_TYPE),
                tag.getString(KEY_MOB_NBT),
                tag.getString(KEY_MOB_UUID),
                tag.getString(KEY_MOB_NAME)
        );
    }

    public record BoundLocation(String dimensionId, BlockPos pos) {
        public ResourceKey<Level> dimension() {
            return ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION,
                    new ResourceLocation(this.dimensionId));
        }
    }

    public record CapturedMob(ResourceLocation typeId, String snbt, UUID uuid, String nameJson) {
    }

    private static Optional<UUID> parseUuid(String value) {
        if (value == null || value.isEmpty()) return Optional.empty();
        try {
            return Optional.of(UUID.fromString(value));
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }
}
