/*
 * Copyright (c) 2023 Jade Lily Nash.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.lilydev.welcome.config

import com.lilydev.lilylib.config.TomlConfig

class WelcomeConfig(modName: String, path: String, fileName: String) : TomlConfig(modName, path, fileName) {


	override fun generateTomlMap(): MutableMap<String, Any> {
		val configMap: MutableMap<String, Any> = HashMap()

		configMap["global_join_message"] = "<italic><gray>%player_name% has joined</gray></italic>"
		configMap["global_leave_message"] = "<italic><gray>%player_name% left the game</gray></italic>"
		configMap["welcome_message"] = "<yellow>Welcome, %player_name%!</yellow>"
		configMap["send_welcome"] = true

		return configMap
	}

}