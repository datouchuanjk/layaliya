package com.helper.develop.util

import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.getIntOrNull(key: String): Int? {
    return if (has(key)) {
        try {
            getInt(key)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        null
    }
}

fun JSONObject.getStringOrNull(key: String): String? {
    return if (has(key)) {
        try {
            getString(key)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        null
    }
}

fun JSONObject.getLongOrNull(key: String): Long? {
    return if (has(key)) {
        try {
            getLong(key)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        null
    }
}

fun JSONObject.getDoubleOrNull(key: String): Double? {
    return if (has(key)) {
        try {
            getDouble(key)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        null
    }
}

fun JSONObject.getBooleanOrNull(key: String): Boolean? {
    return if (has(key)) {
        try {
            getBoolean(key)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        null
    }
}

fun JSONObject.getJSONObjectOrNull(key: String): JSONObject? {
    return if (has(key)) {
        try {
            getJSONObject(key)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        null
    }
}

fun JSONObject.getJSONArrayOrNull(key: String): JSONArray? {
    return if (has(key)) {
        try {
            getJSONArray(key)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        null
    }
}

fun buildJsonObject(block: (JSONObject) -> Unit):JSONObject  {
    return JSONObject().apply(block)
}

fun buildJsonArray(block: (JSONArray) -> Unit):JSONArray {
    return JSONArray().apply(block)
}

