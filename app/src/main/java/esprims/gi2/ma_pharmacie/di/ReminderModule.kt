package esprims.gi2.ma_pharmacie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.data.remote.reminderService.ReminderService
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.data.repository.ReminderRepository
import esprims.gi2.ma_pharmacie.domain.usecases.reminder.GetAllRemindersUseCase
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ReminderModule {

    @Provides
    fun provideReminderService(retrofit: Retrofit): ReminderService
    {
        return retrofit.create(ReminderService::class.java)
    }

    @Provides
    fun provideReminderRepository(reminderService: ReminderService)=

        ReminderRepository(reminderService)
    @Provides
    fun providesAllReminderUseCase(reminderRepository: ReminderRepository)=
        GetAllRemindersUseCase(reminderRepository)


}