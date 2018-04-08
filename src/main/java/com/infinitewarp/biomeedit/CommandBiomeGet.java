package com.infinitewarp.biomeedit;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBiomeGet extends CommandBase implements ICommand {

    @Override
    public String getName() {
        return "biomeget";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/biomeget <z> <x> displays biome info for coordinate (x,z)";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(new String[] { });
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        BlockPos basePos = sender.getPosition();
        BlockPos pos = new BlockPos(
                parseDouble((double)basePos.getX(), args[0], false),
                (double)basePos.getY(),
                parseDouble((double)basePos.getZ(), args[1], false)
        );

        World world = sender.getEntityWorld();
//        TODO why is getBiomeName broken? It throws java.lang.NoSuchMethodError: net.minecraft.world.biome.Biome.getBiomeName()Ljava/lang/String
//        String biomeName = world.getChunkFromBlockCoords(pos).getBiome(pos, world.getBiomeProvider()).getBiomeName();
        Biome biome = world.getChunkFromBlockCoords(pos).getBiome(pos, world.getBiomeProvider());
        String biomeName = String.valueOf(Biome.REGISTRY.getNameForObject(biome));
        sender.sendMessage(new TextComponentString(String.format("biome at (%s, %s) is %s", pos.getX(), pos.getZ(), biomeName)));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
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
