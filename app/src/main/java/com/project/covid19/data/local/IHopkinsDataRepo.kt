package com.project.covid19.data.local

import com.project.covid19.model.hopkinsdata.HopkinsCSSEDataRes

interface IHopkinsDataRepo {
    fun insert(list: List<HopkinsCSSEDataRes>)
    fun getAllData(): List<HopkinsCSSEDataRes>?
    fun deleteAll()
}