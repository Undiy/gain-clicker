package util

import kotlinx.datetime.Clock

actual fun currentTimeMillis() = Clock.System.now().toEpochMilliseconds()