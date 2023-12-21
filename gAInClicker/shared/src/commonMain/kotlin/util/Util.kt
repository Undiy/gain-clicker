package util

import kotlinx.datetime.Clock

fun currentTimeMillis() = Clock.System.now().toEpochMilliseconds()
