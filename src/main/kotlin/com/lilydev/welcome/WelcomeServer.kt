/*
 * Copyright (c) 2023 Jade Lily Nash.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.lilydev.welcome

import com.lilydev.lilylib.util.LilyParsing
import com.lilydev.welcome.config.WelcomeConfig
import com.mojang.brigadier.Command
import eu.pb4.placeholders.api.TextParserUtils
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import net.minecraft.server.command.CommandManager.literal

object WelcomeServer : DedicatedServerModInitializer {

    const val MOD_ID: String = "welcome"
    const val MOD_NAME: String = "Welcome"

    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_NAME)

    @JvmField
    var config: WelcomeConfig? = null

    override fun onInitializeServer() {
        config = WelcomeConfig(MOD_NAME, "config", MOD_ID)
        config!!.init()

        CommandRegistrationCallback.EVENT.register {
            dispatcher, _, _ ->
            dispatcher.register(
                literal("welcome-reload")
                    .requires(Permissions.require("welcome.command.reload", 3))
                    .executes {_ ->
                        config!!.load()
                        return@executes Command.SINGLE_SUCCESS
                    }
            )
        }

        ServerPlayConnectionEvents.JOIN.register {
            serverHandler, _, server ->
            if (config!!.tomlData.getBoolean("send_welcome") == true) {
                var welcomeMessage: String = config!!.tomlData.getString("welcome_message")
                welcomeMessage = LilyParsing.parseStringWithVariable(
                    welcomeMessage, "player_name", serverHandler.player.displayName.string)
                welcomeMessage = LilyParsing.parseStringWithVariable(
                    welcomeMessage, "player_count", server.currentPlayerCount.toString()
                )
                serverHandler.player.sendMessageToClient(
                    TextParserUtils.formatText(welcomeMessage), false)
            }
        }

    }
}