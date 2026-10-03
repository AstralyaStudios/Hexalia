package net.astralya.hexalia.gameplay.mutation;

import dev.architectury.event.events.common.TickEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.astralya.hexalia.block.custom.MorphoraBlock;
import net.astralya.hexalia.particle.ModParticleTypes;
import net.astralya.hexalia.recipe.ModRecipeTypes;
import net.astralya.hexalia.recipe.MutationRecipe;
import net.astralya.hexalia.recipe.MutationRecipeInput;
import net.astralya.hexalia.util.MutationOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class MutationProcess {
  private static final int DIRECT_TICKS = 50;
  private static final int MORPHORA_TICKS = 100;
  private static final List<MutationProcess> ACTIVE = new ArrayList<>();
  private static MinecraftServer activeServer;
  private static boolean registered;

  private final ResourceKey<Level> dimension;
  private final BlockPos center;
  private final List<Target> targets;
  private final List<BlockPos> unfunded;
  private final boolean morphora;
  private int ticks;

  private MutationProcess(
      ServerLevel level, BlockPos center, List<Target> targets, List<BlockPos> unfunded, boolean morphora) {
    this.dimension = level.dimension();
    this.center = center.immutable();
    this.targets = targets;
    this.unfunded = unfunded;
    this.morphora = morphora;
  }

  public static Target findTarget(ServerLevel level, BlockPos pos) {
    BlockState state = level.getBlockState(pos);
    ItemStack input = state.getBlock().asItem().getDefaultInstance();
    if (input.isEmpty()) return null;
    MutationRecipeInput recipeInput = new MutationRecipeInput(input);
    RecipeHolder<MutationRecipe> recipe =
        level.getRecipeManager().getRecipeFor(ModRecipeTypes.MUTATION.get(), recipeInput, level).orElse(null);
    if (recipe == null) return null;
    ItemStack output = recipe.value().assemble(recipeInput, level.registryAccess());
    return output.isEmpty() ? null : new Target(pos.immutable(), state, output, null);
  }

  public static void startDirect(ServerLevel level, Target target) {
    start(level, target.pos(), List.of(target), List.of(), false);
  }

  public static void startMorphora(
      ServerLevel level, BlockPos center, List<Target> targets, List<BlockPos> unfunded) {
    start(level, center, targets, unfunded, true);
  }

  private static void start(
      ServerLevel level, BlockPos center, List<Target> targets, List<BlockPos> unfunded, boolean morphora) {
    if (!registered) {
      TickEvent.SERVER_POST.register(MutationProcess::tickAll);
      registered = true;
    }
    if (activeServer != level.getServer()) {
      ACTIVE.clear();
      activeServer = level.getServer();
    }
    ACTIVE.add(new MutationProcess(level, center, targets, unfunded, morphora));
    if (morphora) burst(level, center, 8);
  }

  private static void tickAll(MinecraftServer server) {
    if (activeServer != server) {
      ACTIVE.clear();
      activeServer = server;
      return;
    }
    Iterator<MutationProcess> iterator = ACTIVE.iterator();
    while (iterator.hasNext()) {
      MutationProcess process = iterator.next();
      ServerLevel level = server.getLevel(process.dimension);
      if (level == null || process.tick(level)) iterator.remove();
    }
  }

  private boolean tick(ServerLevel level) {
    this.ticks++;
    int duration = this.morphora ? MORPHORA_TICKS : DIRECT_TICKS;
    if (this.ticks < duration) {
      if (this.morphora) this.morphoraParticles(level);
      else this.directParticles(level);
      if (this.morphora && this.ticks == duration / 2) {
        level.playSound(null, this.center, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 0.45F, 1.1F);
      }
      return false;
    }

    int completed = 0;
    if (this.morphora
        && (!level.hasChunkAt(this.center)
            || !(level.getBlockState(this.center).getBlock() instanceof MorphoraBlock))) {
      for (Target target : this.targets) fail(level, target.pos());
      for (BlockPos pos : this.unfunded) fail(level, pos);
      return true;
    }
    for (Target target : this.targets) {
      if (!level.hasChunkAt(target.pos())
          || level.getBlockState(target.pos()) != target.original()
          || target.grass() != null
              && (!level.hasChunkAt(target.grass()) || !level.getBlockState(target.grass()).is(Blocks.GRASS_BLOCK))) {
        fail(level, target.pos());
        continue;
      }
      if (!level.destroyBlock(target.pos(), false)) {
        fail(level, target.pos());
        continue;
      }
      if (!MutationOutput.apply(level, target.pos(), target.output().copy())) {
        level.setBlock(target.pos(), target.original(), 3);
        fail(level, target.pos());
        continue;
      }
      if (target.grass() != null) level.setBlock(target.grass(), Blocks.COARSE_DIRT.defaultBlockState(), 3);
      burst(level, target.pos(), this.morphora ? 4 : 7);
      completed++;
    }
    for (BlockPos pos : this.unfunded) fail(level, pos);
    if (completed > 0) {
      if (this.morphora) burst(level, this.center, 12);
      level.playSound(null, this.center, SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.BLOCKS, 0.55F, 1.2F);
    }
    return true;
  }

  private void directParticles(ServerLevel level) {
    float progress = this.ticks / (float) DIRECT_TICKS;
    int count = progress < 0.3F ? 1 : progress < 0.7F ? 2 : 3;
    for (int i = 0; i < count; i++) {
      double angle = this.ticks * (0.23 + progress * 0.15) + i * Math.PI * 2.0 / count;
      double radius = (1.7 - progress * 1.35) * (0.8 + level.random.nextDouble() * 0.4);
      double x = this.center.getX() + 0.5 + Math.cos(angle) * radius;
      double y = this.center.getY() + 0.35 + progress * 0.5 + Math.sin(angle * 1.5) * 0.35;
      double z = this.center.getZ() + 0.5 + Math.sin(angle) * radius;
      double vx = -Math.cos(angle) * 0.025 - Math.sin(angle) * 0.035;
      double vy = 0.015 + (1.0 - progress) * 0.015;
      double vz = -Math.sin(angle) * 0.025 + Math.cos(angle) * 0.035;
      dust(level, x, y, z, vx, vy, vz);
      if (i == 0 && this.ticks % 5 == 0) {
        level.sendParticles(ModParticleTypes.LEAVES.get(),
            x + (level.random.nextDouble() - 0.5) * 0.18,
            y + (level.random.nextDouble() - 0.5) * 0.12,
            z + (level.random.nextDouble() - 0.5) * 0.18,
            0, vx + (level.random.nextDouble() - 0.5) * 0.01,
            vy + (level.random.nextDouble() - 0.5) * 0.01,
            vz + (level.random.nextDouble() - 0.5) * 0.01, 1.0);
      }
    }
    if (progress > 0.7F && this.ticks % 3 == 0) {
      level.sendParticles(ModParticleTypes.HEX_MOTES.get(),
          this.center.getX() + 0.5, this.center.getY() + 0.65, this.center.getZ() + 0.5,
          2, 0.2, 0.2, 0.2, 0.01);
    }
  }

  private void morphoraParticles(ServerLevel level) {
    float progress = this.ticks / (float) MORPHORA_TICKS;
    int orbitCount = progress < 0.5F ? 1 + this.ticks / 25 : 3;
    for (int i = 0; i < orbitCount; i++) {
      orbit(level, this.center, this.ticks * 0.3 + i * Math.PI * 2.0 / orbitCount,
          0.7 + progress * 0.5, 0.55 + i * 0.2);
    }
    if (this.ticks % (progress > 0.8F ? 1 : 2) != 0 || this.targets.isEmpty()) return;
    int count = Math.min(progress > 0.8F ? 8 : 5, this.targets.size());
    for (int i = 0; i < count; i++) {
      int index = (this.ticks / 2 + i * 3) % this.targets.size();
      Target target = this.targets.get(index);
      if (this.ticks < MORPHORA_TICKS / 2) {
        BlockPos grass = target.grass();
        dust(level, grass.getX() + 0.5 + (level.random.nextDouble() - 0.5) * 0.4,
            grass.getY() + 0.3, grass.getZ() + 0.5 + (level.random.nextDouble() - 0.5) * 0.4,
            0.0, 0.055, 0.0);
        arc(level, grass, this.center, (this.ticks + index * 4) % 18 / 18.0, index);
      } else {
        arc(level, this.center, target.pos(), (this.ticks + index * 5) % 20 / 20.0, index);
        if (this.ticks > 65) orbit(level, target.pos(), this.ticks * 0.4 + index,
            0.7 - Math.max(0.0, progress - 0.8) * 1.5, 0.55);
      }
    }
  }

  private static void arc(ServerLevel level, BlockPos source, BlockPos destination, double progress, int index) {
    double sx = source.getX() + 0.5;
    double sy = source.getY() + 0.45;
    double sz = source.getZ() + 0.5;
    double dx = destination.getX() - source.getX();
    double dz = destination.getZ() - source.getZ();
    double length = Math.max(1.0, Math.sqrt(dx * dx + dz * dz));
    double bend = Math.sin(progress * Math.PI) * Math.sin(index * 2.4) * 0.8;
    dust(level, sx + dx * progress - dz / length * bend,
        sy + (destination.getY() - source.getY()) * progress + Math.sin(progress * Math.PI) * 1.2,
        sz + dz * progress + dx / length * bend,
        dx * 0.012, 0.015, dz * 0.012);
  }

  private static void orbit(ServerLevel level, BlockPos center, double angle, double radius, double height) {
    dust(level, center.getX() + 0.5 + Math.cos(angle) * radius,
        center.getY() + height + Math.sin(angle * 0.7) * 0.25,
        center.getZ() + 0.5 + Math.sin(angle) * radius,
        -Math.sin(angle) * 0.04, 0.02, Math.cos(angle) * 0.04);
  }

  private static void dust(ServerLevel level, double x, double y, double z, double vx, double vy, double vz) {
    level.sendParticles(ModParticleTypes.HEX_MOTES.get(), x, y, z, 0, vx, vy, vz, 1.0);
  }

  private static void burst(ServerLevel level, BlockPos pos, int count) {
    level.sendParticles(ModParticleTypes.HEX_MOTES.get(),
        pos.getX() + 0.5, pos.getY() + 0.65, pos.getZ() + 0.5,
        count, 0.35, 0.3, 0.35, 0.04);
  }

  private static void fail(ServerLevel level, BlockPos pos) {
    if (!level.hasChunkAt(pos)) return;
    level.sendParticles(ModParticleTypes.LEAVES.get(), pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 3, 0.2, 0.2, 0.2, 0.01);
    level.sendParticles(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 2, 0.15, 0.15, 0.15, 0.005);
  }

  public record Target(BlockPos pos, BlockState original, ItemStack output, BlockPos grass) {
    public Target withGrass(BlockPos grass) {
      return new Target(this.pos, this.original, this.output, grass.immutable());
    }
  }
}
