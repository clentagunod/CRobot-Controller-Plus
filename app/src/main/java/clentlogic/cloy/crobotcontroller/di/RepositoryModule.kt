package clentlogic.cloy.crobotcontroller.di

import clentlogic.cloy.crobotcontroller.data.repository.BleRepositoryImpl
import clentlogic.cloy.crobotcontroller.data.repository.CmdRepositoryImpl
import clentlogic.cloy.crobotcontroller.data.repository.DataStoreRepositoryImpl
import clentlogic.cloy.crobotcontroller.domain.repository.BleRepository
import clentlogic.cloy.crobotcontroller.domain.repository.CmdRepository
import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsCmdRepository(
        cmdRepositoryImpl: CmdRepositoryImpl
    ): CmdRepository

    @Binds
    @Singleton
    abstract fun bindsBleRepository(
        bleRepositoryImpl: BleRepositoryImpl
    ): BleRepository

    @Binds
    @Singleton
    abstract fun bindsDataStoreRepository(
        dataStoreRepositoryImpl: DataStoreRepositoryImpl
    ): DataStoreRepository

}