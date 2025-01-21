package com.apps.shared

import org.koin.core.module.Module

expect fun getKoinTestRule(modules: List<Module>): KoinTestRule

interface KoinTestRule {
    val modules: List<Module>

    fun starting()

    fun finished()

}