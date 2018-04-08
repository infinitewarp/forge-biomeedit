package com.infinitewarp.biomeedit;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class CommandBiomeGet extends CommandBase implements ICommand {

    @Override
    public String getName() {
        return "biomeget";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/biomeget <x> <z> displays biome info for coordinate (x,z)";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }

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
}
