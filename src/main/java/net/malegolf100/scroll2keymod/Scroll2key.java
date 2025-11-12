package net.malegolf100.scroll2keymod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scroll2key implements ModInitializer {
	public static final String MOD_ID = "scroll2keymod";
	public static final Logger LOGGER = LoggerFactory.getLogger("scroll2key");

	// New keybinding category for Scroll2Key
	private static final KeyBinding.Category SCROLL_KEY_CATEGORY = KeyBinding.Category.create(Identifier.of("scroll2key", "main"));

	// Right scroll key
	private static final KeyBinding keyBindingRight = KeyBindingHelper.registerKeyBinding(
			new KeyBinding(
					"key.scroll2keymod.scroll_right",
					InputUtil.Type.KEYSYM,
					GLFW.GLFW_KEY_RIGHT_BRACKET,
					SCROLL_KEY_CATEGORY
			)
	);

	// Left scroll key
	private static final KeyBinding keyBindingLeft = KeyBindingHelper.registerKeyBinding(
			new KeyBinding(
					"key.scroll2keymod.scroll_left",
					InputUtil.Type.KEYSYM,
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

		// Register client tick event listener
		ClientTickEvents.START_CLIENT_TICK.register(client -> {
			if (keyBindingRight.isPressed() && !keyRightPressed) {
				scrollHotbarRight();
				keyRightPressed = true;
			} else if (!keyBindingRight.isPressed()) {
				keyRightPressed = false;
			}

			if (keyBindingLeft.isPressed() && !keyLeftPressed) {
				scrollHotbarLeft();
				keyLeftPressed = true;
			} else if (!keyBindingLeft.isPressed()) {
				keyLeftPressed = false;
			}
		});
	}

	private void scrollHotbarRight() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			int currentSlot = player.getInventory().getSelectedSlot();
			int nextSlot = (currentSlot + 1) % 9;
			player.getInventory().setSelectedSlot(nextSlot);
		}
	}

	private void scrollHotbarLeft() {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null) {
			int currentSlot = player.getInventory().getSelectedSlot();
			int prevSlot = (currentSlot - 1 + 9) % 9;
			player.getInventory().setSelectedSlot(prevSlot);
		}
	}
}
