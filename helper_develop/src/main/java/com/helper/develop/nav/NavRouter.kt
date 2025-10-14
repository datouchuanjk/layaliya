package com.helper.develop.nav

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.navArgument

class NavRouter private constructor(
    internal val route: String,
    val arguments: List<NamedNavArgument>
) {

    val static = arguments.joinToParams(route)

    fun dynamic(vararg pair: Pair<String, Any?>) = arguments.joinToParams(route, *pair)

    class Builder(private val route: String) {
        private val _arguments = mutableListOf<NamedNavArgument>()
        fun argument(
            name: String,
            builder: NavArgumentBuilder.() -> Unit
        ) = apply {
            _arguments.add(navArgument(name, builder))
        }

        fun build() = NavRouter(
            route = route,
            arguments = _arguments
        )
    }
}

internal fun List<NamedNavArgument>.joinToParams(
    route: String,
    vararg pair: Pair<String, Any?>
): String {
    return pair.toList().ifEmpty {
        map {
            it.name to "{${it.name}}"
        }
    }.joinToString("&") {
        "${it.first}=${it.second}"
    }.let {
        if (it.isEmpty()) {
            route
        } else {
            "${route}?$it"
        }
    }
}
