package com.github.hongkaiwen.kotlin_play2

import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

/**
 * https://www.jianshu.com/p/84cc26da7c6d
 */
fun main(args: Array<String>) {
    testAsync()
    runBlockingPlay()
    joinPlay()
    println("submit all coroutines")
}

/**
 * 一般runBlocking函数不用来当做普通协程函数使用，它的主要目的是用来桥接普通阻塞代码和挂起风格的非阻塞代码
 */
fun runBlockingPlay() = runBlocking {
    GlobalScope.launch {
        delay(3000)
        println("finished.")
    }

    delay(5000)
    println("execute finished")
}

fun joinPlay() = runBlocking {
    var firstJob = GlobalScope.launch {
        delay(2000)
        println("first job finished.")
    }

    firstJob.join()
    println("second job finished.")
}

/**
 * 并行执行两个任务
 */
fun testAsync() = runBlocking(Dispatchers.Default) {
    println("[testAsync] start thread:${Thread.currentThread()}")
    val time = measureTimeMillis {
        val one = GlobalScope.async(Dispatchers.Default) {
            println("[doJob1] thread:${Thread.currentThread()}")
            doJob1() }
        val two = GlobalScope.async(Dispatchers.Default) {
            println("[doJob2] thread:${Thread.currentThread()}")
            doJob2() }
        println("[testAsync] result :${one.await() + two.await()}")
    }
    println("[testAsync] completed in :$time ms")
}

fun doJob1(): Int{
    TimeUnit.SECONDS.sleep(2)
    return 1
}

fun doJob2(): Int{
    TimeUnit.SECONDS.sleep(3)
    return 2
}