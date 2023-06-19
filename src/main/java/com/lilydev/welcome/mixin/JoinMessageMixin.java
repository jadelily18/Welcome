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
import net.minecraft.network.ClientConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.lilydev.welcome.WelcomeServer.config;

@Mixin(PlayerManager.class)
public abstract class JoinMessageMixin {

    @Final @Shadow private MinecraftServer server;
    private ServerPlayerEntity player;

    @Inject(method = "onPlayerConnect", at = @At("HEAD"))
    public void getPlayer(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        this.player = player;
    }

    @ModifyArg(
            method = "onPlayerConnect",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/PlayerManager;broadcast(Lnet/minecraft/text/Text;Z)V"
            )
    )
    private Text broadcastJoin(Text message) {
        assert config != null;
        String playerName = this.player.getDisplayName().getString();

        String leaveMessage = config.tomlData.getString("global_join_message");
        leaveMessage = LilyParsing.parseStringWithVariable(
                leaveMessage, "player_name", playerName
        );
        leaveMessage = LilyParsing.parseStringWithVariable(
                leaveMessage, "player_count", String.valueOf(server.getCurrentPlayerCount() + 1)
        );

        return TextParserUtils.formatText(leaveMessage);
    }

}
