/*
 * Copyright (C) 2018 The Tachiyomi Open Source Project
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package tachiyomi.app

import android.app.Application
import tachiyomi.app.initializers.AppInitializers
import tachiyomi.core.di.AppScope
import tachiyomi.core.http.HttpModule
import tachiyomi.core.rx.SchedulersModule
import tachiyomi.data.di.DataModule
import tachiyomi.ui.di.UiModule
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.smoothie.module.SmoothieApplicationModule

/**
 * The main application class. Only the dependency injection framework should be initialized
 * here. Any other dependency or library that requires initialization should do so through
 * the [AppInitializers] list.
 */
@Suppress("unused")
class App : Application() {

  override fun onCreate() {
    super.onCreate()

    Toothpick.setConfiguration(if (BuildConfig.DEBUG) {
      Configuration.forDevelopment()
    } else {
      Configuration.forProduction()
    })

    val scope = Toothpick.openScope(AppScope)
    scope.installModules(
      SmoothieApplicationModule(this),
      HttpModule,
      SchedulersModule,
      DataModule,
      UiModule
    )

    scope.getInstance(AppInitializers::class.java)
  }

}