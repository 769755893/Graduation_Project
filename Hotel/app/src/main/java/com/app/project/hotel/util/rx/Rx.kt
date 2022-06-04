package com.example.uitraning.util.rx

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.app.project.hotel.base.myapplicationContext
import com.app.project.hotel.common.mwindow
import com.example.uitraning.util.coroutines.Co
import com.example.uitraning.util.coroutines.Main
import com.example.uitraning.util.log
import com.example.uitraning.util.showToast
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import java.util.function.Consumer

/*
reified  make the object match all the constraint
 */
fun <T> Single<T>.autoSetupAllFunctions(time: Long): Single<T> = compose(SingleTransformer {
    it.switchThread()
        .setupTimeOut(time)
        .autoCatchErrorToast()
})

fun <T> Single<T>.switchThread(): Single<T> = compose(SingleTransformer {
    it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
})

fun <T> Single<T>.autoCatchErrorToast(): Single<T> = compose(SingleTransformer {
    it.doOnError {
        showToast(
            myapplicationContext,
            "网络开小差了，请稍后再试",
            mwindow,
            false
        )
    }
})

fun <T> Single<T>.setupTimeOut(time: Long): Single<T> = compose(SingleTransformer {
    it.timeout(time, TimeUnit.SECONDS) { emitter ->
        emitter.onError(TimeoutException())
    }
})

object Rx {
    abstract class Observer<T> : io.reactivex.Observer<T> {
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

    inline fun <reified T> just(item: T) = Observable.just(item)

    @SuppressLint("CheckResult")
    fun runInRetryUntilSuccess(block: (disposable: Disposable?) -> Unit) {
        var disposable: Disposable? = null
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