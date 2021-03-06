package xyz.przemyk.spookyarms.guns;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import xyz.przemyk.spookyarms.registry.ItemsRegistry;

import java.util.function.Predicate;

public class PumpkinBazookaItem extends ShootableItem {

    public PumpkinBazookaItem() {
        super(new Properties().maxStackSize(1).group(ItemsRegistry.MOD_ITEM_GROUP));
}

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        ItemStack ammo = playerIn.findAmmo(heldItem);

        if (!ammo.isEmpty() || playerIn.abilities.isCreativeMode) {
            PumpkinRocketEntity pumpkinRocketEntity = new PumpkinRocketEntity(worldIn, ammo.getItem() == ItemsRegistry.EXPLOSIVE_PUMPKIN.get());
            pumpkinRocketEntity.setPosition(playerIn.getPosX(), playerIn.getPosY() + 1, playerIn.getPosZ());
            pumpkinRocketEntity.func_234612_a_(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 3.0F, 1.0F);
            pumpkinRocketEntity.setMotion(pumpkinRocketEntity.getMotion().add(playerIn.getMotion()));

            worldIn.addEntity(pumpkinRocketEntity);

            playerIn.getCooldownTracker().setCooldown(this, 45);

            if (!ammo.isEmpty() && !playerIn.abilities.isCreativeMode) {
                ammo.shrink(1);
            }

            playerIn.rotationPitch -= 10;
            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.0F, 1.5F);
        }

        return ActionResult.resultPass(heldItem);
    }

    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate() {
        return itemStack -> itemStack.getItem() == Items.PUMPKIN || itemStack.getItem() == ItemsRegistry.EXPLOSIVE_PUMPKIN.get() || itemStack.getItem() == Items.CARVED_PUMPKIN;
    }

    @Override
    public int func_230305_d_() {
        return 0;
    }
}
