package com.example.driverassistant.database

import android.content.Context
import android.util.Log
import com.example.driverassistant.model.DrivingSession
import com.example.driverassistant.model.Notification
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class DatabaseController() {
    fun getDrivingSessionsDataFromLocalStorage(context: Context, userId: String) : ArrayList<DrivingSession> {
        Log.d("STORAGE", "Attempting to read data from local storage for user $userId")
        val data = readDrivingSessionsDataFromLocalStorage(context, userId)
        Log.d("STORAGE", "Successfully read data: $data")
        return data
    }

    private fun readDrivingSessionsDataFromLocalStorage(
        context: Context,
        userId: String
    ): ArrayList<DrivingSession> {
        val initializedDatabase = verifyPresenceOfALocalFile(context, userId)
        if (initializedDatabase) {
            val objectMapper = jacksonObjectMapper()
            var drivingSessionList: ArrayList<DrivingSession>

            context.openFileInput(userId)
                .use { stream ->
                    val data = stream
                        .bufferedReader()
                        .use { it.readText() }
                    drivingSessionList =
                        objectMapper.readValue(data)
                }
            return drivingSessionList
        }
        return getFakeDrivingSession()
    }

    private fun getFakeDrivingSession(): ArrayList<DrivingSession> {
        val fakeNotificationList = arrayListOf(Notification("", "", "", 0, 0L))
        val fakeDrivingSession = DrivingSession(0, "", "", 0L, 0L, 0f, 0f, fakeNotificationList)
        val fakeData = ArrayList<DrivingSession>()
        fakeData.add(fakeDrivingSession)
        return fakeData
    }

    fun writeDrivingSessionsDataInLocalStorage(context: Context, userId: String, drivingSession: DrivingSession) {
        val initializedDatabase = verifyPresenceOfALocalFile(context, userId)
        var drivingSessionsList = ArrayList<DrivingSession>()
        if (initializedDatabase) {
            drivingSessionsList = readDrivingSessionsDataFromLocalStorage(context, userId)
            drivingSession.index = drivingSessionsList.size + 1
        } else {
            drivingSession.index = 1
        }
        drivingSessionsList.add(drivingSession)

        Log.d("STORAGE", "Writing data to DB $drivingSessionsList")

        val objectMapper = jacksonObjectMapper()
        val data: ByteArray = objectMapper.writeValueAsBytes(drivingSessionsList)
        context.openFileOutput(
            userId,
            Context.MODE_PRIVATE
        ).use {
            it.write(data)
        }
    }

    fun verifyPresenceOfALocalFile(context: Context, userId: String) : Boolean {
        val files: Array<String> = context.fileList()
        if (userId in files) {
            return true
        }
        return false
    }

    fun getDriverSessionBySessionIndex(context: Context, userId: String, index: Int) : DrivingSession {
        val initializedDatabase = verifyPresenceOfALocalFile(context, userId)
        if (initializedDatabase) {
            val drivingSessionsList = readDrivingSessionsDataFromLocalStorage(context, userId)
            return drivingSessionsList[index]
        }
        return getFakeDrivingSession().first()
    }
}