package com.infinitewarp.biomeedit;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandBiomeList extends CommandBase implements ICommand {

    @Override
    public String getName() {
        return "biomelist";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/biomelist prints list of allowed biome types";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String joinedBiomeNames = String.join(" ", BiomeEditMod.getBiomeNames());
        sender.sendMessage(new TextComponentString(joinedBiomeNames));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 3) {
            return BiomeEditMod.getBiomeNames();
        }
        return new ArrayList<>();
    }
}
