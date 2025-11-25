package com.davidlukash.cakebaker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform