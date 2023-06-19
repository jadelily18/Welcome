/*
 * Copyright (c) 2023 Jade Lily Nash.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.lilydev.welcome

import com.llamalad7.mixinextras.MixinExtrasBootstrap
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint

object WelcomePreLaunch : PreLaunchEntrypoint {
	override fun onPreLaunch() {
		MixinExtrasBootstrap.init()
	}
}