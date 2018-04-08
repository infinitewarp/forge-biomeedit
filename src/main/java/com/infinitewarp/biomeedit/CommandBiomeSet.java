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
        return "/biomeset <x1> <z1> <x2> <z2> <biomename> set specified biome over region x1,z1 to x2,z2";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(new String[]{});
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 5) {
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }

        // Verify the input has a legal biome name first.
        String newBiomeName = args[4];
        Biome newBiome;
        try {
            newBiome = Biome.REGISTRY.getObject(new ResourceLocation(newBiomeName));
        } catch (NullPointerException e) {
            throw new WrongUsageException(String.format("biome '%s' not found", newBiomeName), new Object[0]);
        }
        byte biomeIdAsByte = (byte) Biome.REGISTRY.getIDForObject(newBiome);

        // Build start and end positions from inputs and order them appropriately.
        BlockPos[] poses = getStartEndPos(sender, args);
        BlockPos start = poses[0];
        BlockPos end = poses[1];

        World world = sender.getEntityWorld();

        // Iterate to find all chunks touched by the selection.
        for (int chunkX = (start.getX() >> 4); chunkX <= (end.getX() >> 4); chunkX++) {
            for (int chunkZ = (start.getZ() >> 4); chunkZ <= (end.getZ() >> 4); chunkZ++) {
                Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
                byte[] biomeArray = chunk.getBiomeArray();
                // Iterate through all possible positions in the current chunk.
                for (int posX = (chunkX << 4); posX < (chunkX+1 << 4); posX++) {
                    for (int posZ = (chunkZ << 4); posZ < (chunkZ+1 << 4); posZ++) {
                        // Set the position's biome if the position is in our region.
                        if (posX >= start.getX() && posX <= end.getX() && posZ >= start.getZ() && posZ <= end.getZ()) {
                            int biomeArrayPos = (posZ & 15) << 4 | (posX & 15);
                            biomeArray[biomeArrayPos] = biomeIdAsByte;
                            // sender.sendMessage(new TextComponentString(String.format("set %s at x=%s,z=%s", newBiomeName, posX, posZ)));
                            System.out.println(String.format("set %s at x=%s,z=%s", newBiomeName, posX, posZ));
                        }
                    }
                }
                // Push the updated biome array into the chunk and mark for saving.
                chunk.setBiomeArray(biomeArray);
                chunk.markDirty();
            }
        }

        sender.sendMessage(new TextComponentString(String.format("biome set to %s", newBiomeName)));
    }

    private BlockPos[] getStartEndPos(ICommandSender sender, String[] args) throws CommandException {
        BlockPos basePos = sender.getPosition();
        BlockPos pos1 = new BlockPos(
                parseDouble((double)basePos.getX(), args[0], false),
                (double)basePos.getY(),
                parseDouble((double)basePos.getZ(), args[1], false)
        );
        BlockPos pos2 = new BlockPos(
                parseDouble((double)basePos.getX(), args[2], false),
                (double)basePos.getY(),
                parseDouble((double)basePos.getZ(), args[3], false)
        );

        int startX = pos1.getX();
        int endX = pos2.getX();
        if (pos2.getX() < startX) {
            startX = endX;
            endX = pos1.getX();
        }

        int startZ = pos1.getZ();
        int endZ = pos2.getZ();
        if (pos2.getZ() < startZ) {
            startZ = endZ;
            endZ = pos1.getZ();
        }

        return new BlockPos[] {
                new BlockPos(startX, basePos.getY(), startZ),
                new BlockPos(endX, basePos.getY(), endZ)
        };
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 5) {
            return BiomeEditMod.getBiomeNames(args[4]);
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
