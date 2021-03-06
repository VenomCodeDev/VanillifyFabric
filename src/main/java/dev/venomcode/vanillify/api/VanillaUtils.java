package dev.venomcode.vanillify.api;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class VanillaUtils
{
    public static MutableText getText(String text, Formatting... formattings)
    {
        return Text.literal(text).setStyle( Style.EMPTY.withItalic(false).withFormatting(formattings) );
    }

    public static void playSoundToPlayer( ServerPlayerEntity player, SoundEvent event, float vol, float pitch)
    {
        player.networkHandler.sendPacket( new PlaySoundS2CPacket(event, SoundCategory.PLAYERS, player.getPos().x, player.getPos().y, player.getPos().z, vol, pitch, ((ServerWorld)player.world).getSeed()));
    }

    public static ItemStack getHeadFromRaw( String rawId )
    {
        ItemStack buffStack = new ItemStack( Items.PLAYER_HEAD );
        NbtCompound skullTag = buffStack.getOrCreateSubNbt( "SkullOwner" );

        NbtCompound propertiesTag = new NbtCompound();
        NbtList texturesTag = new NbtList();
        NbtCompound textureValue = new NbtCompound();

        textureValue.putString("Value", rawId);

        texturesTag.add(textureValue);
        propertiesTag.put("textures", texturesTag);
        skullTag.put("Properties", propertiesTag);
        skullTag.putUuid( "Id", UUID.randomUUID() );

        return buffStack;
    }

    public static void setStackModelData(ItemStack stack, int id)
    {
        NbtCompound stackTag = stack.getOrCreateNbt(  );

        stackTag.putInt("CustomModelData", id);
    }

    public static void setStackLore(ItemStack stack, List<MutableText> lores)
    {
        NbtCompound dispCompound = stack.getOrCreateSubNbt( "display" );

        NbtList loreListTag = new NbtList();

        for(Text t : lores)
        {
            loreListTag.add(NbtString.of(Text.Serializer.toJson(t)));
        }

        dispCompound.put( "Lore", loreListTag );
    }

    public static Text getGenericResponse(GenericResponseTypes type)
    {
        Text retText;
        switch(type)
        {
            case INSUFFICIENT_PERMISSION:
                retText = getText( "Insufficient Permissions!", Formatting.RED, Formatting.ITALIC );
                break;
            case UNKNOWN_COMMAND:
                retText = getText( "Uknown Command.", Formatting.RED );
                break;
            case BAD_COMMAND_SYNTAX:
                retText = getText( "Invalid Command Syntax.", Formatting.RED );
                break;
            case UNKNOWN_MOD_ERROR:
                retText = getText( "Uknown Mod-Related Error.", Formatting.RED );
                break;
            case ONLY_PLAYERS_CAN_EXECUTE:
                retText = getText( "Only players can run this command.", Formatting.RED );
                break;
            default:
                retText = getText( "Vanillify: Invalid GenericResponseType...?", Formatting.DARK_RED );
                break;
        }

        return retText;
    }

    public enum GenericResponseTypes
    {
        INSUFFICIENT_PERMISSION,
        UNKNOWN_COMMAND,
        BAD_COMMAND_SYNTAX,
        UNKNOWN_MOD_ERROR,
        ONLY_PLAYERS_CAN_EXECUTE,
    }
}
