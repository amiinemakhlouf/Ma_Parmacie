package esprims.gi2.ma_pharmacie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.data.remote.notificationService.NotificationService
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    fun  provideNotificationService()=NotificationService()

}