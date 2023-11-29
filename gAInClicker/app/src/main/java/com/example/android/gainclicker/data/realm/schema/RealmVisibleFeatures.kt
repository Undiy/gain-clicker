package com.example.android.gainclicker.data.realm.schema

import com.example.android.gainclicker.core.ClickAction
import com.example.android.gainclicker.core.Task
import com.example.android.gainclicker.core.VisibleFeatures
import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.ext.toRealmSet
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmSet

class RealmVisibleFeatures : EmbeddedRealmObject {

    var tasks: RealmSet<String> = realmSetOf()

    var actions: RealmSet<String> = realmSetOf()
}

fun RealmVisibleFeatures.toVisibleFeatures(): VisibleFeatures {
    return VisibleFeatures(
        tasks = tasks.map(Task::valueOf).toSet(),
        actions = actions.map(ClickAction::valueOf).toSet()
    )
}

fun RealmVisibleFeatures.updateFrom(visibleFeatures: VisibleFeatures) {
    tasks.clear()
    tasks.addAll(visibleFeatures.tasks.map(Task::name).toRealmSet())
    actions.clear()
    actions.addAll(visibleFeatures.actions.map(ClickAction::name).toRealmSet())
}

fun VisibleFeatures.toRealmRealmVisibleFeatures(): RealmVisibleFeatures {
    return RealmVisibleFeatures().also{ it.updateFrom(this) }
}