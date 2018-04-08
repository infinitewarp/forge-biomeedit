package com.infinitewarp.biomeedit;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class CommandBiomeList implements ICommand {

    @Override
    public String getName() {
        return "biomelist";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/biomelist prints list of allowed biome types";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(new String[] { });
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String joinedBiomeNames = String.join(" ", BiomeEditMod.getBiomeNames());
        sender.sendMessage(new TextComponentString(joinedBiomeNames));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return null;
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
