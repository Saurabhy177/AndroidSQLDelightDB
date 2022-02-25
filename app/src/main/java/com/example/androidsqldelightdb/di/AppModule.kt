package com.example.androidsqldelightdb.di

import android.app.Application
import com.example.androidsqldelightdb.PersonDatabase
import com.example.androidsqldelightdb.data.PersonDataSource
import com.example.androidsqldelightdb.data.PersonDataSourceImpl
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * InstallIn annotation with SingletonComponent ensures our dependencies lives
 * as long as our application does.
 * */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a singleton instance of the sql driver to our application.
     * */
    @Provides
    @Singleton
    fun providesSqlDriver(app: Application): SqlDriver {
        return AndroidSqliteDriver(
            schema = PersonDatabase.Schema,
            context = app,
            name = "person.db"
        )
    }

    @Provides
    @Singleton
    fun providesPersonDataSource(driver: SqlDriver): PersonDataSource {
        return PersonDataSourceImpl(PersonDatabase(driver))
    }
}