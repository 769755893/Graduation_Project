package com.app.project.hotel.common

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface BindLife {
    val compositeDisposable: CompositeDisposable
    fun Disposable.bindLife() = compositeDisposable.add(this)
    fun Observable<*>.bindLife() = subscribe().bindLife()
    fun Single<*>.bindLife() = subscribe().bindLife()
    fun Maybe<*>.bindLife() = subscribe().bindLife()

    fun clearAll() = compositeDisposable.clear()
}