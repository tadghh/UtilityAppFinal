package com.example.utilityapp.composables.weather

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * HTTP Client.
 */
private val client = OkHttpClient()


/**
 * Composable for displaying a single note item.
 */
interface WeatherDataCallback {
	/**
	 * On Success function
	 */
	fun onSuccess(data: String)

	/**
	 * On Failure function
	 */
	fun onFailure(error: String)
}


/**
 * Fetches weather data from the specified URL using OkHttp.
 *
 * @param url The URL to fetch weather data from.
 * @param callback The callback for handling successful/failure scenarios.
 */
fun fetchWeatherData(url: String, callback: WeatherDataCallback) {
	val request = Request.Builder()
		.url(url)
		.build()

	client.newCall(request).enqueue(object : Callback {
		override fun onFailure(call: Call, e: IOException) {
			callback.onFailure("Network error: ${e.message}")
		}

		override fun onResponse(call: Call, response: Response) {
			if (response.isSuccessful) {
				val data = response.body?.string()
				if (data != null) {
					callback.onSuccess(data)
				} else {
					callback.onFailure("Empty response body")
				}
			} else {
				callback.onFailure("Unsuccessful response: ${response.code}")
			}
		}
	})
}