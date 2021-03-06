package dev.venomcode.vanillify.mixins.packets;

import dev.venomcode.vanillify.api.interfaces.BlockStateProxy;
import dev.venomcode.vanillify.api.interfaces.EntityTypeProxy;
import dev.venomcode.vanillify.api.interfaces.ItemStackProxy;
import dev.venomcode.vanillify.api.interfaces.RecipeProviderProxy;
import net.fabricmc.fabric.impl.registry.sync.RegistrySyncManager;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin( RegistrySyncManager.class )
public class RegistrySyncManagerMixin
{
    @Redirect( method = "createAndPopulateRegistryMap", at = @At( value = "INVOKE", target = "Lnet/minecraft/util/registry/Registry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;" ), require = 0 )
    private static Identifier onRegistryToTag( Registry<Object> registry, Object obj, boolean isClientSync )
    {
        {
            if ( isClientSync &&
                    (
                            obj instanceof ItemStackProxy ||
                                    obj instanceof EntityTypeProxy ||
                                    obj instanceof BlockStateProxy ||
                                    obj instanceof RecipeProviderProxy
                    )
            )
            {
                return null;
            }
            return registry.getId( obj );
        }
    }
}
