package bose.ankush.reposnews.di

import android.content.Context
import androidx.room.Room
import bose.ankush.reposnews.data.NewsRepository
import bose.ankush.reposnews.data.local.NewsDao
import bose.ankush.reposnews.data.local.NewsDatabase
import bose.ankush.reposnews.data.network.ApiService
import bose.ankush.reposnews.util.BASE_URL
import bose.ankush.reposnews.util.DATABASE_NAME
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
    fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun getOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun getMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    fun getRetrofit(
        moshi: Moshi,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun getApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesNewsDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun providesNewsDao(db: NewsDatabase) = db.newsDao()

    @Singleton
    @Provides
    fun getNewsRepository(apiService: ApiService, dao: NewsDao) = NewsRepository(apiService, dao)
}