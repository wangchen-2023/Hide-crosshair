package com.example.crosshairhider.client;

import com.example.crosshairhider.ModConfig;
import com.example.crosshairhider.CrosshairHiderClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class SmartCrosshairRenderer {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static final Identifier TEXTURE_ENTITY = Identifier.of("crosshairhider", "textures/gui/crosshair_entity.png");
    private static final Identifier TEXTURE_BLOCK = Identifier.of("crosshairhider", "textures/gui/crosshair_block.png");

    private static float scale = 0f, colorProgress = 0f;
    private static Identifier currentTex = TEXTURE_ENTITY;

    public static void render(DrawContext ctx) {
        if (client.player == null || !ModConfig.enabled) return;

        HitResult hit = client.crosshairTarget;
        float tScale = 0f, tColor = 0f;

        if (hit != null) {
            if (hit.getType() == HitResult.Type.ENTITY && ModConfig.detectEntities) {
                Entity e = ((EntityHitResult) hit).getEntity();
                boolean hostile = e instanceof HostileEntity;
                if (hostile || (e instanceof PassiveEntity && ModConfig.mode == CrosshairHiderClient.Mode.ALL_LIVING)) {
                    tScale = hostile ? 1.4f : 1.2f;
                    tColor = hostile ? 1.0f : 0.5f;
                    currentTex = TEXTURE_ENTITY;
                }
            }
            else if (hit.getType() == HitResult.Type.BLOCK && ModConfig.detectBlocks) {
                BlockState state = client.world.getBlockState(((BlockHitResult) hit).getBlockPos());
                if (!ModConfig.filterBlocks || isInteractable(state)) {
                    tScale = 1.1f;
                    tColor = 0.2f;
                    currentTex = TEXTURE_BLOCK;
                }
            }
        }

        scale = lerp(scale, tScale, 0.2f);
        colorProgress = lerp(colorProgress, tColor, 0.2f);

        if (scale > 0.01f) {
            drawCustom(ctx, scale, colorProgress, currentTex);
            if (ModConfig.showCooldown) drawCooldown(ctx);
        }
    }

    private static boolean isInteractable(BlockState s) {
        Block b = s.getBlock();
        return s.hasBlockEntity() || b instanceof DoorBlock || b instanceof TrapdoorBlock ||
                b instanceof FenceGateBlock || b instanceof ButtonBlock || b instanceof LeverBlock ||
                b instanceof CraftingTableBlock || b instanceof AnvilBlock || b instanceof LecternBlock;
    }

    private static void drawCustom(DrawContext ctx, float sc, float p, Identifier tex) {
        int sz = Math.round(16 * sc);
        int x = (ctx.getScaledWindowWidth() - sz) / 2;
        int y = (ctx.getScaledWindowHeight() - sz) / 2;

        float g = p > 0.5f ? lerp(1f, 0f, (p-0.5f)*2) : 1f;
        float b = p > 0.5f ? lerp(0.7f, 0f, (p-0.5f)*2) : lerp(1f, 0.7f, p*2);

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1f, g, b, 1.0f);
        ctx.drawTexture(tex, x, y, 0, 0, sz, sz, sz, sz);
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    private static void drawCooldown(DrawContext ctx) {
        float progress = client.player.getAttackCooldownProgress(0f);
        if (progress < 0.99f) {
            int x = ctx.getScaledWindowWidth() / 2 - 4;
            int y = ctx.getScaledWindowHeight() / 2 + 12;
            ctx.fill(x, y, x + 8, y + 2, 0x66000000);
            ctx.fill(x, y, x + (int)(8 * progress), y + 2, 0xFFFFFFFF);
        }
    }

    private static float lerp(float f, float t, float s) { return f + (t - f) * s; }
}