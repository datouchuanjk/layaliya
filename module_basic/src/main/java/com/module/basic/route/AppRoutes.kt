package com.module.basic.route

import androidx.navigation.NavType
import com.helper.develop.nav.NavRouter

object AppRoutes {
    val Launcher = NavRouter.Builder("Launcher").build()
    val Main = NavRouter.Builder("main").build()
    val Login = NavRouter.Builder("login").build()
    val FollowersOrFans = NavRouter.Builder("followersOrFans")
        .argument("uid") {
            type = NavType.StringType
            defaultValue = ""
        }.argument("type") {
            type = NavType.IntType
            defaultValue = 0
        }.build()
    val MyRoom = NavRouter.Builder("MyRoom").build()
    val WebView = NavRouter.Builder("WebView")
        .argument("title") {
            type = NavType.StringType
            defaultValue = ""
        }
        .argument("url") {
            type = NavType.StringType
            defaultValue = ""
        }.build()
    val ChatroomEnterCheck = NavRouter.Builder("ChatroomEnterCheck")
        .argument("roomId") {
            type = NavType.StringType
            defaultValue = ""
        }.argument("roomInfo") {
            type = NavType.StringType
            defaultValue = ""
        }.build()
    val Chatroom = NavRouter.Builder("Chatroom")
        .argument("roomId") {
            type = NavType.StringType
            defaultValue = ""
        }.argument("roomInfo") {
            type = NavType.StringType
            defaultValue = ""
        }.argument("isMysteriousPerson") {
            type = NavType.BoolType
            defaultValue = false
        }.build()
    val RoomCreateCheck = NavRouter.Builder("RoomCreateCheck").build()
    val CreateOrEditRoom = NavRouter.Builder("createOrEditRoom")
        .argument("isEdit") {
            type = NavType.BoolType
            defaultValue = false
        }
        .argument("roomInfo") {
            type = NavType.StringType
            nullable = true
            defaultValue = null
        }.build()
    val PersonCenter = NavRouter.Builder("personCenter")
        .argument("uid") {
            type = NavType.StringType
            defaultValue = ""
        }.build()
    val Chat = NavRouter.Builder("Chat")
        .argument("conversationId") {
            type = NavType.StringType
            defaultValue = ""
        }.build()

    val Setting = NavRouter.Builder("Setting").build()
    val Store = NavRouter.Builder("Store").build()
    val Bag = NavRouter.Builder("Bag").build()
    val CharmLevel = NavRouter.Builder("CharmLevel").build()
    val WealthLevel = NavRouter.Builder("WealthLevel").build()
    val Noble = NavRouter.Builder("Noble").build()
    val NobleHistory = NavRouter.Builder("NobleHistory")
        .argument("name") {
            type = NavType.StringType
            defaultValue = ""
        }
        .build()
    val Agent = NavRouter.Builder("Agent").build()
    val PostCommunity = NavRouter.Builder("PostCommunity").build()
    val ChatroomUserList = NavRouter.Builder("ChatroomUserList")
        .argument("roomId") {
            type = NavType.StringType
            defaultValue = ""
        }
        .build()
    val ChatroomReport = NavRouter.Builder("ChatroomReport")
        .argument("objId") {
            type = NavType.StringType
            defaultValue = ""
        }.argument("type") {
            type = NavType.IntType
            defaultValue = 0
        }.build()
    val Wallet = NavRouter.Builder("Wallet")
        .argument("fromGame") {
            type = NavType.BoolType
            defaultValue = false
        }
        .build()
    val Admin = NavRouter.Builder("Admin").build()
    val BD = NavRouter.Builder("BD").build()
    val Game = NavRouter.Builder("Game")
        .argument("withChildScreen") {
            type = NavType.BoolType
           defaultValue = true
        }
        .argument("roomId") {
            type = NavType.StringType
            nullable = true
        }
        .build()
    val Emoji = NavRouter.Builder("Emoji")
        .argument("x") {
            type = NavType.FloatType
            defaultValue = 0f
        }.argument("y") {
            type = NavType.FloatType
            defaultValue = 0f
        }.argument("w") {
            type = NavType.FloatType
            defaultValue = 0f
        }.argument("h") {
            type = NavType.FloatType
            defaultValue = 0f
        }
        .build()
    val Gift = NavRouter.Builder("Gift")
        .argument("roomId") {
            type = NavType.StringType
            defaultValue = "0"
        }
        .argument("receiveUid") {
            type = NavType.StringType
        }
        .argument("receiveName") {
            type = NavType.StringType
        }
        .argument("receiveAvatar") {
            type = NavType.StringType
        }
        .build()

    val GiftPlay = NavRouter.Builder("GiftPlay")
        .argument("json") {
            type = NavType.StringType
            defaultValue = ""
        }
        .build()
    val NoblePlay = NavRouter.Builder("NoblePlay")
        .argument("json") {
            type = NavType.StringType
            defaultValue = ""
        }
        .build()
    val Comment = NavRouter.Builder("Comment")
        .argument("id") {
            type = NavType.StringType
        }.build()

    val PersonEdit = NavRouter.Builder("PersonEdit")
        .argument("fromComplete") {
            type = NavType.BoolType
            defaultValue = false
        }.build()
    val CoinMerchant = NavRouter.Builder("CoinMerchant").build()
    val CoinDetail = NavRouter.Builder("CoinDetail").build()
}
