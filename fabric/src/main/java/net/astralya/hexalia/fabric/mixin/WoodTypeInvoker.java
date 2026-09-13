package net.astralya.hexalia.fabric.mixin;

import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WoodType.class)
public interface WoodTypeInvoker {
  @Invoker("register")
  static WoodType hexalia$register(WoodType type) {
    throw new AssertionError();
  }
}
