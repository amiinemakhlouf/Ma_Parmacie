package esprims.gi2.ma_pharmacie.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import esprims.gi2.ma_pharmacie.domain.LoginUseCase
import esprims.gi2.ma_pharmacie.domain.RegisterUseCase

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    fun provideRegisterUseCase():RegisterUseCase= RegisterUseCase()

    @Provides
    fun provideLoginUseCase()=LoginUseCase()

}