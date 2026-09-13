package net.astralya.hexalia.gameplay.enchantedplant;
import java.util.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.astralya.hexalia.Configuration;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.*;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;
import net.minecraft.world.Difficulty;
final class GrimshadeActivation {
  private static final int AURA_RADIUS=4;
  private static final Map<UUID,Integer> ACTIVE_PLAYERS=new HashMap<>();
  private GrimshadeActivation() {}
  static void register(){ServerTickEvents.END_SERVER_TICK.register(GrimshadeActivation::tick);}
  static boolean activate(ServerWorld world, PlayerEntity player) {
    if(world.getDifficulty()==Difficulty.PEACEFUL){player.sendMessage(Text.translatable("message.hexalia.grimshade.peaceful"),true);world.playSound(null,player.getBlockPos(),SoundEvents.BLOCK_AMETHYST_BLOCK_HIT,SoundCategory.PLAYERS,.35F,.65F);return false;}
    BlockPos origin=player.getBlockPos(); int radius=Math.max(1,Configuration.GRIMSHADE_EFFECT_RADIUS.get());
    convertSkeletons(world,origin,radius); convertSkulls(world,origin,radius);
    world.playSound(null,origin,SoundEvents.ENTITY_WITHER_AMBIENT,SoundCategory.PLAYERS,.65F,1.15F);
    world.spawnParticles(ParticleTypes.SOUL,origin.getX()+.5,origin.getY()+1.,origin.getZ()+.5,20,.7,.8,.7,.02);
    ACTIVE_PLAYERS.put(player.getUuid(),world.getServer().getTicks()+Math.max(1,Configuration.GRIMSHADE_DURATION.get()));
    applyAura(world,player,radius); return true;
  }
  private static void tick(MinecraftServer server){
    WindsongActivation.tick(server);
    if(server.getTicks()%20!=0)return;
    NautiliteActivation.tick(server);
    if(ACTIVE_PLAYERS.isEmpty())return;
    Iterator<Map.Entry<UUID,Integer>> entries=ACTIVE_PLAYERS.entrySet().iterator();
    while(entries.hasNext()){Map.Entry<UUID,Integer> entry=entries.next();PlayerEntity player=server.getPlayerManager().getPlayer(entry.getKey());if(server.getTicks()>=entry.getValue()||player==null||!player.isAlive()||player.isRemoved()){entries.remove();continue;}applyAura((ServerWorld)player.getWorld(),player,AURA_RADIUS);}
  }
  private static void applyAura(ServerWorld world,PlayerEntity player,int radius){
    if(world.getDifficulty()!=Difficulty.PEACEFUL){Box area=player.getBoundingBox().expand(radius);for(LivingEntity target:world.getEntitiesByClass(LivingEntity.class,area,e->e.isAlive()&&!(e instanceof PlayerEntity))){target.addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER,40,0,true,true));target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS,40,0,true,true));world.spawnParticles(ParticleTypes.SOUL,target.getX(),target.getY()+target.getHeight()*.65,target.getZ(),2,target.getWidth()*.25,target.getHeight()*.2,target.getWidth()*.25,.01);}world.spawnParticles(ParticleTypes.WITCH,player.getX(),player.getY()+1.,player.getZ(),3,.175,.25,.175,.01);}
  }
  private static void convertSkeletons(ServerWorld world,BlockPos origin,int radius){
    for(SkeletonEntity skeleton:world.getEntitiesByClass(SkeletonEntity.class,new Box(origin).expand(radius),e->true)){WitherSkeletonEntity replacement=EntityType.WITHER_SKELETON.create(world);if(replacement==null)continue;replacement.refreshPositionAndAngles(skeleton.getX(),skeleton.getY(),skeleton.getZ(),skeleton.getYaw(),skeleton.getPitch());replacement.setVelocity(skeleton.getVelocity());replacement.setCustomName(skeleton.getCustomName());replacement.setCustomNameVisible(skeleton.isCustomNameVisible());replacement.setPersistent();replacement.setHealth(Math.min(replacement.getMaxHealth(),skeleton.getHealth()));skeleton.discard();world.spawnEntity(replacement);world.spawnParticles(ParticleTypes.SOUL,replacement.getX(),replacement.getY()+1.,replacement.getZ(),8,.3,.6,.3,.02);}
  }
  private static void convertSkulls(ServerWorld world,BlockPos origin,int radius){
    for(BlockPos cursor:BlockPos.iterate(origin.add(-radius,-radius,-radius),origin.add(radius,radius,radius))){if(cursor.getSquaredDistance(origin)>radius*radius)continue;BlockState state=world.getBlockState(cursor);if(state.isOf(Blocks.SKELETON_SKULL))world.setBlockState(cursor,copyProperties(state,Blocks.WITHER_SKELETON_SKULL.getDefaultState()),3);else if(state.isOf(Blocks.SKELETON_WALL_SKULL)){BlockState replacement=copyProperties(state,Blocks.WITHER_SKELETON_WALL_SKULL.getDefaultState());if(state.contains(WallSkullBlock.FACING))replacement=replacement.with(WallSkullBlock.FACING,state.get(WallSkullBlock.FACING));world.setBlockState(cursor,replacement,3);}}
  }
  @SuppressWarnings({"rawtypes","unchecked"}) private static BlockState copyProperties(BlockState from,BlockState to){for(var property:from.getProperties())if(to.contains(property))to=to.with((net.minecraft.state.property.Property)property,from.get(property));return to;}
}
