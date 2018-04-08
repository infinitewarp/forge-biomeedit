package com.infinitewarp.biomeedit;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
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

public class CommandBiomeSet implements ICommand {

    @Override
    public String getName() {
        return "biomeset";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/biomeset <z> <x> <biomename> set specified biome at coordinate (x,z)";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(new String[]{});
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        World world = sender.getEntityWorld();
        int x = Integer.parseInt(args[0]);
        int z = Integer.parseInt(args[1]);
        BlockPos pos = new BlockPos(x, 1, z);

        Chunk chunk = world.getChunkFromBlockCoords(pos);
        byte[] biomeArray = chunk.getBiomeArray();
        int biomeArrayPos = (z & 15) << 4 | (x & 15);

        Biome newBiome = Biome.REGISTRY.getObject(new ResourceLocation(args[2]));
        String newbiomeName = String.valueOf(Biome.REGISTRY.getNameForObject(newBiome));

        biomeArray[biomeArrayPos] = (byte) Biome.REGISTRY.getIDForObject(newBiome);
        chunk.setBiomeArray(biomeArray);
        chunk.markDirty();

        sender.sendMessage(new TextComponentString(String.format("biome at (%s, %s) is now %s", x, z, newbiomeName)));
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
