package com.example.crosshairhider.client;

import com.example.crosshairhider.CrosshairHiderClient;
import com.example.crosshairhider.ModConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("gui.crosshairhider.title"))
                    .setSavingRunnable(ModConfig::save);

            ConfigEntryBuilder eb = builder.entryBuilder();

            ConfigCategory core = builder.getOrCreateCategory(Text.translatable("gui.crosshairhider.category.core"));

            core.addEntry(eb.startBooleanToggle(Text.translatable("gui.crosshairhider.enabled"), ModConfig.enabled)
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> ModConfig.enabled = v)
                    .build());

            ConfigCategory detection = builder.getOrCreateCategory(Text.translatable("gui.crosshairhider.category.detection"));

            detection.addEntry(eb.startBooleanToggle(Text.translatable("gui.crosshairhider.detectEntities"), ModConfig.detectEntities)
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> ModConfig.detectEntities = v)
                    .build());

            detection.addEntry(eb.startBooleanToggle(Text.translatable("gui.crosshairhider.detectBlocks"), ModConfig.detectBlocks)
                    .setDefaultValue(true)
                    .setSaveConsumer(v -> ModConfig.detectBlocks = v)
                    .build());

            detection.addEntry(eb.startBooleanToggle(Text.translatable("gui.crosshairhider.detectNone"), !ModConfig.detectEntities && !ModConfig.detectBlocks)
                    .setDefaultValue(false)
                    .setTooltip(Text.translatable("gui.crosshairhider.detectNone.tooltip"))
                    .setSaveConsumer(v -> {
                        if (v) {
                            ModConfig.detectEntities = false;
                            ModConfig.detectBlocks = false;
                        }
                    })
                    .build());

            ConfigCategory advanced = builder.getOrCreateCategory(Text.translatable("gui.crosshairhider.category.advanced"));

            advanced.addEntry(eb.startEnumSelector(Text.translatable("gui.crosshairhider.mode"), CrosshairHiderClient.Mode.class, ModConfig.mode)
                    .setDefaultValue(CrosshairHiderClient.Mode.ALL_LIVING)
                    .setEnumNameProvider(mode -> Text.translatable("text.crosshairhider.mode." + mode.name()))
                    .setSaveConsumer(v -> ModConfig.mode = v)
                    .build());

            advanced.addEntry(eb.startBooleanToggle(Text.translatable("gui.crosshairhider.filterBlocks"), ModConfig.filterBlocks)
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("gui.crosshairhider.filterBlocks.tooltip"))
                    .setSaveConsumer(v -> ModConfig.filterBlocks = v)
                    .build());

            advanced.addEntry(eb.startBooleanToggle(Text.translatable("gui.crosshairhider.showCooldown"), ModConfig.showCooldown)
                    .setDefaultValue(false)
                    .setSaveConsumer(v -> ModConfig.showCooldown = v)
                    .build());

            return builder.build();
        };
    }
}