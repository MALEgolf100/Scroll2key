package net.malegolf100.scroll2keymod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scroll2key implements ModInitializer {
	public static final String MOD_ID = "scroll2keymod";
	public static final Logger LOGGER = LoggerFactory.getLogger("scroll2key");

	// New keybinding category for Scroll2Key
	private static final KeyMapping.Category SCROLL_KEY_CATEGORY =
			KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));

	// Right scroll key
	private static final KeyMapping keyBindingRight = KeyMappingHelper.registerKeyMapping(
			new KeyMapping(
					"key.scroll2keymod.scroll_right",
					InputConstants.Type.KEYSYM,
					GLFW.GLFW_KEY_RIGHT_BRACKET,
					SCROLL_KEY_CATEGORY
			)
	);

	// Left scroll key
	private static final KeyMapping keyBindingLeft = KeyMappingHelper.registerKeyMapping(
			new KeyMapping(
					"key.scroll2keymod.scroll_left",
					InputConstants.Type.KEYSYM,
					GLFW.GLFW_KEY_LEFT_BRACKET,
					SCROLL_KEY_CATEGORY
			)
	);

	// Flags to track key press state
	private boolean keyRightPressed = false;
	private boolean keyLeftPressed = false;

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Scroll2Key mod!");

		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			if (client.player == null) return;
			Player player = client.player;

			if (keyBindingRight.isDown() && !keyRightPressed) {
				scrollHotbar(player, 1);
				keyRightPressed = true;
			} else if (!keyBindingRight.isDown()) {
				keyRightPressed = false;
			}

			if (keyBindingLeft.isDown() && !keyLeftPressed) {
				scrollHotbar(player, -1);
				keyLeftPressed = true;
			} else if (!keyBindingLeft.isDown()) {
				keyLeftPressed = false;
			}
		});
	}

	private void scrollHotbar(Player player, int direction) {
		int currentSlot = player.getInventory().getSelectedSlot();
		int nextSlot = (currentSlot + direction + 9) % 9;
		player.getInventory().setSelectedSlot(nextSlot);
	}
}