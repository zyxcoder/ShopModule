package com.zkxy.shop.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * @author zhangyuxiang
 * @date 2024/4/19
 */
class NumberDeserializer : JsonDeserializer<Number> {
    override fun deserialize(
        json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?
    ): Number? {
        return try {
            json!!.asNumber
        } catch (e: Exception) {
            null
        }
    }
}