package esprims.gi2.ma_pharmacie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.data.remote.userService.UserService
import esprims.gi2.ma_pharmacie.useCase.*

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    fun provideRegisterUseCase(userService: UserService):SendOtpConfirmationUseCase= SendOtpConfirmationUseCase(userService)

    @Provides
    fun provideLoginUseCase(userService: UserService)=LoginUseCase(userService)

    @Provides
    fun provideConfirmEmailUseCase(userService: UserService)=ConfirmEmailUseCase(userService)

    @Provides
    fun ProvideForgetPasswordUSeCase(userService: UserService) =ForgetPasswordUseCase(userService)

    @Provides
    fun provideResetPasswordUseCase(userService: UserService)=ResetPasswordUseCase(userService)

    @Provides
    fun provideRegisterGoogleClient(userService: UserService) =LoginGoogleClient(userService)

}