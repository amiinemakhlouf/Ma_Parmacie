package esprims.gi2.ma_pharmacie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.useCase.*

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    fun provideRegisterUseCase():sendOtpConfirmationUseCase= sendOtpConfirmationUseCase()

    @Provides
    fun provideLoginUseCase()=LoginUseCase()

    @Provides
    fun provideConfirmEmailUseCase()=ConfirmEmailUseCase()

    @Provides
    fun ProvideForgetPasswordUSeCase() =ForgetPasswordUseCase()

    @Provides
    fun provideResetPasswordUseCase()=ResetPasswordUseCase()

    @Provides
    fun provideRegisterGoogleClient() =LoginGoogleClient()

}