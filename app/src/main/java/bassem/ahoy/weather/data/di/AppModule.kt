package bassem.ahoy.weather.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import bassem.ahoy.weather.data.api.ApiHelper
import bassem.ahoy.weather.data.api.ApiHelperImpl
import bassem.ahoy.weather.data.api.ApiService
import bassem.ahoy.weather.data.db.AppDatabase
import bassem.ahoy.weather.data.db.ForecastDao
import bassem.ahoy.weather.data.repository.Repository
import bassem.ahoy.weather.data.repository.RepositoryImpl
import bassem.ahoy.weather.data.store.SettingsStore
import bassem.ahoy.weather.data.store.SettingsStoreImpl
import bassem.ahoy.weather.utils.BASE_URL
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: String,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(moshiConverterFactory)
        .build()

    @Singleton
    @Provides
    fun provideConverterFactory(): MoshiConverterFactory =
        MoshiConverterFactory.create()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

    @Provides
    @Singleton
    fun provideForecastDao(appDataBase: AppDatabase): ForecastDao = appDataBase.forecastDao()

    @Provides
    @Singleton
    fun provideRepositoryHelper(repository: RepositoryImpl): Repository = repository

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase = Room
        .databaseBuilder(appContext, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
        .build()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> =
        appContext.dataStore

    @Provides
    @Singleton
    fun provideSettingStore(settingsStore: SettingsStoreImpl): SettingsStore =
        settingsStore

}