package com.zkxy.shop.network.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * @author zhangyuxiang
 * @date 2024/4/19
 */
class StringDeserializer : JsonDeserializer<String> {
    override fun deserialize(
        json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?
    ): String? {
        return try {
            json!!.asString
        } catch (e: Exception) {
            null
        }
    }
}