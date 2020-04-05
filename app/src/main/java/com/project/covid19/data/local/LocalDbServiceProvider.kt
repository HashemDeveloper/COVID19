package com.project.covid19.data.local

import android.content.Context
import javax.inject.Inject

class LocalDbServiceProvider @Inject constructor(context: Context) {
    private val localDb: Covid19LocalDb = Covid19LocalDb.invoke(context)

    fun getHopkinsDataDao(): IHopkinsDataDao {
        return this.localDb.getHopkinsDataDao()
    }
}