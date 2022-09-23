package bassem.ahoy.weather.data.di

import android.content.Context
import androidx.room.Room
import bassem.ahoy.weather.data.api.ApiHelper
import bassem.ahoy.weather.data.api.ApiHelperImpl
import bassem.ahoy.weather.data.api.ApiService
import bassem.ahoy.weather.data.db.AppDatabase
import bassem.ahoy.weather.data.db.ForecastDao
import bassem.ahoy.weather.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

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
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase = Room
        .databaseBuilder(appContext, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
        .build()
}