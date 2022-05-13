package bose.ankush.reposnews.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import bose.ankush.reposnews.data.local.NewsDao
import bose.ankush.reposnews.data.local.NewsDatabase
import bose.ankush.reposnews.data.network.NewsApiService
import bose.ankush.reposnews.data.network.WeatherApiService
import bose.ankush.reposnews.data.news_repo.NewsRepository
import bose.ankush.reposnews.data.news_repo.NewsRepositoryImpl
import bose.ankush.reposnews.data.weather_repo.WeatherRepository
import bose.ankush.reposnews.data.weather_repo.WeatherRepositoryImpl
import bose.ankush.reposnews.util.DATABASE_NAME
import bose.ankush.reposnews.util.NEWS_URL
import bose.ankush.reposnews.util.WEATHER_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**Created by
Author: Ankush Bose
Date: 19,May,2021
 **/

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }


    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }


    @Singleton
    @Provides
    @Named("news")
    fun provideRetrofitForNews(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NEWS_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }


    @Singleton
    @Provides
    @Named("weather")
    fun provideRetrofitForTemperature(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WEATHER_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }


    @Provides
    fun provideApiServiceForNews(@Named("news") retrofit: Retrofit): NewsApiService {
        return retrofit.create(NewsApiService::class.java)
    }


    @Provides
    fun provideApiServiceForTemperature(@Named("weather") retrofit: Retrofit): WeatherApiService {
        return retrofit.create(WeatherApiService::class.java)
    }


    @Singleton
    @Provides
    fun providesNewsDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()


    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideNewsRepository(newsApiService: NewsApiService, newsDatabase: NewsDatabase): NewsRepository =
        NewsRepositoryImpl(newsApiService, newsDatabase)


    @Singleton
    @Provides
    fun provideWeatherRepository(weatherApiService: WeatherApiService): WeatherRepository =
        WeatherRepositoryImpl(weatherApiService)
}