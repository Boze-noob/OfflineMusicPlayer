package com.applid.musicbox.utils

typealias EventSubscriber<T> = (T) -> Unit
typealias EventUnsubscribeFn = () -> Unit

class Eventer<T> {
    private val subscribers = mutableListOf<EventSubscriber<T>>()

    fun subscribe(subscriber: EventSubscriber<T>): EventUnsubscribeFn {
        subscribers.add(subscriber)
        return { unsubscribe(subscriber) }
    }

    fun unsubscribe(subscriber: EventSubscriber<T>) {
        subscribers.remove(subscriber)
    }

    fun dispatch(event: T) {
        subscribers.forEach { it(event) }
    }
}
