package com.example.finalproject.misc.weather

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class APIData {
    companion object {
        private val api by lazy { Connect.callApi() }
        private val apiKey = "006ce7725214dc6b468bbc8788ff4a47"
        var disposable: Disposable? = null

        fun getData(lat : Float, lon : Float, units : String, lang : String, callback: Response) {
            disposable = api.getWeatherInfo(lat, lon, units, lang, apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    callback.onResponse(result)
                }, { error ->
                    callback.onFailure(error)
                })
        }
    }

    interface Response {
        fun onResponse(data: Model.Result)
        fun onFailure(error: Throwable)
    }
}