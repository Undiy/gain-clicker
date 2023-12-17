package com.example.android.gainclicker.data.realm.schema

import core.CloudStorage
import core.IOModule
import core.Module
import io.realm.kotlin.types.EmbeddedRealmObject

class RealmModule : EmbeddedRealmObject {

    var name: String = ""
    var progress: Float? = null
}

fun RealmModule.toModule(): Module {
    return try {
        IOModule.valueOf(name)
    } catch (e: IllegalArgumentException) {
        if (name == CloudStorage::class.simpleName) {
            CloudStorage(progress = progress ?: 0.0f)
        } else {
            throw IllegalArgumentException("Invalid module name: $name")
        }
     }
}

fun RealmModule.updateFrom(module: Module) {
    when (module) {
        is CloudStorage -> {
            name = CloudStorage::class.simpleName!!
            progress = module.progress
        }
        is IOModule -> {
            name = module.name
        }
    }
}

fun Module.toRealmModule(): RealmModule {
    return RealmModule().also { it.updateFrom(this) }
}



