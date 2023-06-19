/*
 * Copyright (c) 2023 Jade Lily Nash.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.lilydev.welcome.mixin;

import com.lilydev.lilylib.util.LilyParsing;
import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.lilydev.welcome.WelcomeServer.config;

@Mixin(ServerPlayNetworkHandler.class)
public class DisconnectMessageMixin {
    @Final @Shadow private MinecraftServer server;
    @Shadow public ServerPlayerEntity player;

    @ModifyArg(
            method = "onDisconnected",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V",
                    ordinal = 0
            )
    )
    private Text broadcastLeave(Text message) {
        assert config != null;
        String playerName = this.player.getDisplayName().getString();

        String leaveMessage = config.tomlData.getString("global_leave_message");
        leaveMessage = LilyParsing.parseStringWithVariable(
                leaveMessage, "player_name", playerName
        );
        leaveMessage = LilyParsing.parseStringWithVariable(
                leaveMessage, "player_count", String.valueOf(server.getCurrentPlayerCount() - 1)
        );

        return TextParserUtils.formatText(leaveMessage);
    }
}
