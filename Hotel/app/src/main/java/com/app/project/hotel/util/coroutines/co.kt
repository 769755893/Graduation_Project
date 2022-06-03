package com.example.uitraning.util.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext

val Co = object : CoroutineScope {
    val job = Job()
    val co = CoroutineScope(job)
    override val coroutineContext: CoroutineContext
        get() = co.launch {}
}

fun IO(block: () -> Unit) {
    Co.launch(Dispatchers.IO) {
        block.invoke()
    }
}

fun Main(block: () -> Unit) {
    Co.launch(Dispatchers.Main) {
        block.invoke()
    }
}