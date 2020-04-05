package com.project.covid19.utils.rxevents

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class RxEvents @Inject constructor(): IRxEvents {
    private val eventBus = PublishSubject.create<Any>()
    override fun post(event: Any) {
        if (this.eventBus.hasObservers()) {
            this.eventBus.onNext(event)
        }
    }

    override fun <T> observable(eventClass: Class<T>): Observable<T> {
        return this.eventBus
            .filter { event -> event != null }
            .filter(eventClass::isInstance)
            .cast(eventClass)
    }
}