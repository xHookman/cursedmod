package net.fabricmc.cursedmod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CursedMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("cursedmod");
	public static final Identifier MY_SOUND_ID = new Identifier("cursedmod:mouche");
	public static SoundEvent MY_SOUND_EVENT = new SoundEvent(MY_SOUND_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("ATTENTION CA VA sssCRASH !!! (cursedmod est chargé)");
		Registry.register(Registry.SOUND_EVENT, CursedMod.MY_SOUND_ID, MY_SOUND_EVENT);
	}
}
