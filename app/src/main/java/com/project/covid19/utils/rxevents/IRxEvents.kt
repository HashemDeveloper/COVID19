package com.project.covid19.utils.rxevents

import io.reactivex.Observable

interface IRxEvents {
    fun post(event: Any)
    fun <T> observable(eventClass: Class<T>): Observable<T>
}