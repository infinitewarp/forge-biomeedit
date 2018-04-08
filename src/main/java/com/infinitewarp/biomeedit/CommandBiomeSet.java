package com.infinitewarp.biomeedit;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBiomeSet extends CommandBase implements ICommand {

    @Override
    public String getName() {
        return "biomeset";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/biomeset <x> <z> <biomename> set specified biome at coordinate (x,z)";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(new String[]{});
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 3) {
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }

        BlockPos basePos = sender.getPosition();
        BlockPos pos = new BlockPos(
                parseDouble((double)basePos.getX(), args[0], false),
                (double)basePos.getY(),
                parseDouble((double)basePos.getZ(), args[1], false)
        );

        World world = sender.getEntityWorld();
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        byte[] biomeArray = chunk.getBiomeArray();
        int biomeArrayPos = (pos.getZ() & 15) << 4 | (pos.getX() & 15);

        Biome newBiome = Biome.REGISTRY.getObject(new ResourceLocation(args[2]));
        String newbiomeName = String.valueOf(Biome.REGISTRY.getNameForObject(newBiome));

        biomeArray[biomeArrayPos] = (byte) Biome.REGISTRY.getIDForObject(newBiome);
        chunk.setBiomeArray(biomeArray);
        chunk.markDirty();

        sender.sendMessage(new TextComponentString(String.format("biome at (%s, %s) is now %s", pos.getX(), pos.getZ(), newbiomeName)));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 3) {
            return BiomeEditMod.getBiomeNames();
        }
        return new ArrayList<>();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
