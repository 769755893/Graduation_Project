package com.example.uitraning.util.rx

import android.annotation.SuppressLint
import com.example.uitraning.util.coroutines.Co
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

object Rx {
    abstract class Observer<T>: io.reactivex.Observer<T> {
        override fun onComplete() {

        }

        override fun onError(e: Throwable) {

        }

        override fun onNext(t: T) {

        }

        override fun onSubscribe(d: Disposable) {

        }
    }
    fun interval(interval: Long) = Observable.interval(interval, TimeUnit.MILLISECONDS)

    inline fun <reified T> just (item: T) = Observable.just(item)

    @SuppressLint("CheckResult")
    fun runInRetryUntilSuccess(block: (disposable: Disposable?)-> Unit) {
        var disposable: Disposable ? = null
        interval(1000)
            .take(Long.MAX_VALUE)
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io())
            .doOnSubscribe {
                disposable = it
            }
            .subscribe {
                block.invoke(disposable)
            }
    }
}