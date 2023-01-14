package xhookman.cursedmod.entity;

/*
public class VjGolem extends GolemEntity implements Angerable {
    protected static final TrackedData<Byte> VJGOLEM_FLAGS;
    //private static final int HEALTH_PER_INGOT = 25;
    private int attackTicksLeft;
    private int lookingAtVillagerTicksLeft;
    private static final UniformIntProvider ANGER_TIME_RANGE;
    private int angerTime;
    @Nullable
    private UUID angryAt;

    public VjGolem(EntityType<? extends IronGolemEntity> entityType, World world) {
        super(entityType, world);
        this.stepHeight = 1.0F;
    }

    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(2, new WanderNearTargetGoal(this, 0.9, 32.0F));
        this.goalSelector.add(2, new WanderAroundPointOfInterestGoal(this, 0.6, false));
        this.goalSelector.add(4, new IronGolemWanderAroundGoal(this, 0.6));
        this.goalSelector.add(5, new IronGolemLookGoal(this));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackIronGolemTargetGoal(this));
        this.targetSelector.add(2, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(3, new ActiveTargetGoal(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(3, new ActiveTargetGoal(this, MobEntity.class, 5, false, false, (entity) -> {
            return entity instanceof Monster && !(entity instanceof CreeperEntity);
        }));
        this.targetSelector.add(4, new UniversalAngerGoal(this, false));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(VJGOLEM_FLAGS, (byte)0);
    }

    public static DefaultAttributeContainer.Builder createVjGolemAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 15.0);
    }

    protected int getNextAirUnderwater(int air) {
        return air;
    }

    protected void pushAway(Entity entity) {
        if (entity instanceof Monster && !(entity instanceof CreeperEntity) && this.getRandom().nextInt(20) == 0) {
            this.setTarget((LivingEntity)entity);
        }
        super.pushAway(entity);
    }

    public void tickMovement() {
        super.tickMovement();
        if (this.attackTicksLeft > 0) {
            --this.attackTicksLeft;
        }

        if (this.lookingAtVillagerTicksLeft > 0) {
            --this.lookingAtVillagerTicksLeft;
        }

        if (this.getVelocity().horizontalLengthSquared() > 2.500000277905201E-7 && this.random.nextInt(5) == 0) {
            int i = MathHelper.floor(this.getX());
            int j = MathHelper.floor(this.getY() - 0.20000000298023224);
            int k = MathHelper.floor(this.getZ());
            BlockState blockState = this.world.getBlockState(new BlockPos(i, j, k));
            if (!blockState.isAir()) {
                this.world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), this.getX() + ((double)this.random.nextFloat() - 0.5) * (double)this.getWidth(), this.getY() + 0.1, this.getZ() + ((double)this.random.nextFloat() - 0.5) * (double)this.getWidth(), 4.0 * ((double)this.random.nextFloat() - 0.5), 0.5, ((double)this.random.nextFloat() - 0.5) * 4.0);
            }
        }

        if (!this.world.isClient) {
            this.tickAngerLogic((ServerWorld)this.world, true);
        }

    }

    public boolean canTarget(EntityType<?> type) {
        if (this.isPlayerCreated() && type == EntityType.PLAYER) {
            return false;
        } else {
            return type == EntityType.CREEPER ? false : super.canTarget(type);
        }
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("PlayerCreated", this.isPlayerCreated());
        this.writeAngerToNbt(nbt);
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setPlayerCreated(nbt.getBoolean("PlayerCreated"));
        this.readAngerFromNbt(this.world, nbt);
    }

    public void chooseRandomAngerTime() {
        this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
    }

    public void setAngerTime(int angerTime) {
        this.angerTime = angerTime;
    }

    public int getAngerTime() {
        return this.angerTime;
    }

    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }

    private float getAttackDamage() {
        return (float)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    public boolean tryAttack(Entity target) {
        this.attackTicksLeft = 10;
        this.world.sendEntityStatus(this, (byte)4);
        float f = this.getAttackDamage();
        float g = (int)f > 0 ? f / 2.0F + (float)this.random.nextInt((int)f) : f;
        boolean bl = target.damage(DamageSource.mob(this), g);
        if (bl) {
            double var10000;
            if (target instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity)target;
                var10000 = livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
            } else {
                var10000 = 0.0;
            }

            double d = var10000;
            double e = Math.max(0.0, 1.0 - d);
            target.setVelocity(target.getVelocity().add(0.0, 0.4000000059604645 * e, 0.0));
            this.applyDamageEffects(this, target);
        }

        this.playSound(SoundEvents.ENTITY_VJGOLEM_ATTACK, 1.0F, 1.0F);
        return bl;
    }

    public boolean damage(DamageSource source, float amount) {
        IronGolemEntity.Crack crack = this.getCrack();
        boolean bl = super.damage(source, amount);
        if (bl && this.getCrack() != crack) {
            this.playSound(SoundEvents.ENTITY_VJGOLEM_DAMAGE, 1.0F, 1.0F);
        }

        return bl;
    }

    public void handleStatus(byte status) {
        if (status == 4) {
            this.attackTicksLeft = 10;
            this.playSound(SoundEvents.ENTITY_VJGOLEM_ATTACK, 1.0F, 1.0F);
        } else if (status == 11) {
            this.lookingAtVillagerTicksLeft = 400;
        } else if (status == 34) {
            this.lookingAtVillagerTicksLeft = 0;
        } else {
            super.handleStatus(status);
        }

    }

    public int getAttackTicksLeft() {
        return this.attackTicksLeft;
    }

    public void setLookingAtVillager(boolean lookingAtVillager) {
        if (lookingAtVillager) {
            this.lookingAtVillagerTicksLeft = 400;
            this.world.sendEntityStatus(this, (byte)11);
        } else {
            this.lookingAtVillagerTicksLeft = 0;
            this.world.sendEntityStatus(this, (byte)34);
        }

    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_VJGOLEM_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_VJGOLEM_DEATH;
    }

    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (!itemStack.isOf(CustomItem.VJB)) {
            return ActionResult.PASS;
        } else {
            float f = this.getHealth();
            this.heal(25.0F);
            if (this.getHealth() == f) {
                return ActionResult.PASS;
            } else {
                float g = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                //this.playSound(SoundEvents.ENTITY_VJGOLEM_REPAIR, 1.0F, g);
                if (!player.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }

                return ActionResult.success(this.world.isClient);
            }
        }
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_VJGOLEM_STEP, 1.0F, 1.0F);
    }

    public int getLookingAtVillagerTicks() {
        return this.lookingAtVillagerTicksLeft;
    }

    public boolean isPlayerCreated() {
        return ((Byte)this.dataTracker.get(VJGOLEM_FLAGS) & 1) != 0;
    }

    public void setPlayerCreated(boolean playerCreated) {
        byte b = (Byte)this.dataTracker.get(VJGOLEM_FLAGS);
        if (playerCreated) {
            this.dataTracker.set(VJGOLEM_FLAGS, (byte)(b | 1));
        } else {
            this.dataTracker.set(VJGOLEM_FLAGS, (byte)(b & -2));
        }

    }

    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
    }

    public boolean canSpawn(WorldView world) {
        BlockPos blockPos = this.getBlockPos();
        BlockPos blockPos2 = blockPos.down();
        BlockState blockState = world.getBlockState(blockPos2);
        if (!blockState.hasSolidTopSurface(world, blockPos2, this)) {
            return false;
        } else {
            for(int i = 1; i < 3; ++i) {
                BlockPos blockPos3 = blockPos.up(i);
                BlockState blockState2 = world.getBlockState(blockPos3);
                if (!SpawnHelper.isClearForSpawn(world, blockPos3, blockState2, blockState2.getFluidState(), EntityType.VJGOLEM)) {
                    return false;
                }
            }

            return SpawnHelper.isClearForSpawn(world, blockPos, world.getBlockState(blockPos), Fluids.EMPTY.getDefaultState(), EntityType.VJGOLEM) && world.doesNotIntersectEntities(this);
        }
    }

    public Vec3d getLeashOffset() {
        return new Vec3d(0.0, (double)(0.875F * this.getStandingEyeHeight()), (double)(this.getWidth() * 0.4F));
    }

    static {
        VJGOLEM_FLAGS = DataTracker.registerData(IronGolemEntity.class, TrackedDataHandlerRegistry.BYTE);
        ANGER_TIME_RANGE = TimeHelper.betweenSeconds(20, 39);
    }

}
*/
