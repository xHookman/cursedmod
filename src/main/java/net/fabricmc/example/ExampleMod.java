package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registry; // j'ai ajouté ça

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
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

		LOGGER.info("ATTENTION CA VA CRASH !!! (cursedmod est chargé)");
		Registry.register(Registry.SOUND_EVENT, ExampleMod.MY_SOUND_ID, MY_SOUND_EVENT);
	}
}
