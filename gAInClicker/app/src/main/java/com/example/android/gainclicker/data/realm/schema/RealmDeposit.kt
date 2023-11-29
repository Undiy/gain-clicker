package com.example.android.gainclicker.data.realm.schema

import com.example.android.gainclicker.core.Currency
import com.example.android.gainclicker.core.Deposit
import io.realm.kotlin.ext.realmDictionaryOf
import io.realm.kotlin.ext.toRealmDictionary
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmDictionary

class RealmDeposit : EmbeddedRealmObject {

    var accounts: RealmDictionary<Int?> = realmDictionaryOf()
}


fun RealmDeposit.toDeposit(): Deposit {
    return Deposit(
        accounts = this.accounts
            .mapKeys { (k, _) -> Currency.valueOf(k) }
            .mapValues { (_, v) -> v ?: 0 }
    )
}

fun RealmDeposit.updateFrom(deposit: Deposit) {
    accounts.clear()
    deposit.accounts.mapKeysTo(accounts) { (k, _) -> k.name }.toRealmDictionary()
}

fun Deposit.toRealmDeposit(): RealmDeposit {
    return RealmDeposit().also { it.updateFrom(this) }
}
