package esprims.gi2.ma_pharmacie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.data.repository.UserRepository
import esprims.gi2.ma_pharmacie.useCase.*
import esprims.gi2.ma_pharmacie.useCase.authentication.*

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    fun provideRegisterUseCase(userRepository: UserRepository): SendOtpConfirmationUseCase = SendOtpConfirmationUseCase(userRepository)

    @Provides
    fun provideLoginUseCase(userRepository: UserRepository)= LoginUseCase(userRepository)

    @Provides
    fun provideConfirmEmailUseCase(userRepository: UserRepository)= ConfirmEmailUseCase(userRepository)

    @Provides
    fun ProvideForgetPasswordUSeCase(userRepository: UserRepository) = ForgetPasswordUseCase(userRepository)

    @Provides
    fun provideResetPasswordUseCase(userRepository: UserRepository)= ResetPasswordUseCase(userRepository)

    @Provides
    fun provideRegisterGoogleClient(userRepository: UserRepository) = LoginGoogleClient(userRepository)

}