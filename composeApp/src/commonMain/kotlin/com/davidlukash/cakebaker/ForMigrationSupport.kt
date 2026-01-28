package com.davidlukash.cakebaker

@Suppress("ExperimentalAnnotationRetention")
@RequiresOptIn(message = "This should only be used for migrating a save. Do not access it outside of that.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY)
annotation class ForMigrationSupport
