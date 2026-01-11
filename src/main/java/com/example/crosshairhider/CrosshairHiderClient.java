package com.example.crosshairhider;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class CrosshairHiderClient implements ClientModInitializer {
    public enum Mode { OFF, HOSTILE_ONLY, ALL_LIVING }

    private static KeyBinding toggleKey, blockKey, disableEntityKey, filterKey, cooldownKey;

    @Override
    public void onInitializeClient() {
        ModConfig.load();

        toggleKey = register("toggle", GLFW.GLFW_KEY_H);
        blockKey = register("toggle_block", GLFW.GLFW_KEY_J);
        disableEntityKey = register("disable_entity", GLFW.GLFW_KEY_I);
        filterKey = register("filter", GLFW.GLFW_KEY_U);
        cooldownKey = register("cooldown", GLFW.GLFW_KEY_K);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (toggleKey.wasPressed()) {
                ModConfig.enabled = !ModConfig.enabled;
                msg(client, "msg.crosshairhider.enabled", ModConfig.enabled);
                ModConfig.save();
            }

            while (blockKey.wasPressed()) {
                ModConfig.detectBlocks = !ModConfig.detectBlocks;
                msg(client, "msg.crosshairhider.detectBlocks", ModConfig.detectBlocks);
                ModConfig.save();
            }

            while (disableEntityKey.wasPressed()) {
                ModConfig.detectEntities = !ModConfig.detectEntities;
                msg(client, "msg.crosshairhider.detectEntities", ModConfig.detectEntities);
                ModConfig.save();
            }

            while (filterKey.wasPressed()) {
                ModConfig.filterBlocks = !ModConfig.filterBlocks;
                msg(client, "msg.crosshairhider.filterBlocks", ModConfig.filterBlocks);
                ModConfig.save();
            }

            while (cooldownKey.wasPressed()) {
                ModConfig.showCooldown = !ModConfig.showCooldown;
                msg(client, "msg.crosshairhider.showCooldown", ModConfig.showCooldown);
                ModConfig.save();
            }
        });
    }

    private KeyBinding register(String name, int key) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding("key.crosshairhider." + name, InputUtil.Type.KEYSYM, key, "category.crosshairhider"));
    }

    private void msg(net.minecraft.client.MinecraftClient client, String key, boolean state) {
        Text statusText = state ?
                Text.translatable("text.crosshairhider.on").formatted(net.minecraft.util.Formatting.GREEN) :
                Text.translatable("text.crosshairhider.off").formatted(net.minecraft.util.Formatting.RED);

        client.player.sendMessage(
                Text.translatable("text.crosshairhider.prefix")
                        .append(Text.translatable(key))
                        .append(": ")
                        .append(statusText),
                true
        );
    }
}