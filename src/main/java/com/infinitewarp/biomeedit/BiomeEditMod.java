package com.infinitewarp.biomeedit;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Mod(modid = BiomeEditMod.MODID, name = BiomeEditMod.NAME, version = BiomeEditMod.VERSION, acceptableRemoteVersions="*")
public class BiomeEditMod
{
    public static final String MODID = "biomeedit";
    public static final String NAME = "BiomeEdit";
    public static final String VERSION = "%VERSION%";

    private static Logger logger;

    private static List<String> biomeNames = new ArrayList<String>();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        for (Iterator<Biome> iterator = Biome.REGISTRY.iterator(); iterator.hasNext();) {
            Biome biome = iterator.next();
            biomeNames.add(String.valueOf(Biome.REGISTRY.getNameForObject(biome)));
        }
        logger.info("BiomeEdit get hype!");
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandBiomeGet());
        event.registerServerCommand(new CommandBiomeList());
        event.registerServerCommand(new CommandBiomeSet());
    }

    public static List<String> getBiomeNames() {
        return Collections.unmodifiableList(biomeNames);
    }

    public static List<String> getBiomeNames(String startingWith) {
        if (startingWith.length() > 0) {
            return getBiomeNames().stream().filter(n -> n.startsWith(startingWith)).collect(Collectors.toList());
        }
        return getBiomeNames();
    }
}
