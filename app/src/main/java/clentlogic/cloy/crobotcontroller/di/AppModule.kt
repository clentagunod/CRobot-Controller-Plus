package clentlogic.cloy.crobotcontroller.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import clentlogic.cloy.crobotcontroller.data.communication.ble.BleHelper
import clentlogic.cloy.crobotcontroller.data.local.roomdb.dao.CmdDao
import clentlogic.cloy.crobotcontroller.data.local.roomdb.db.CmdDatabase
import clentlogic.cloy.crobotcontroller.domain.repository.BleRepository
import clentlogic.cloy.crobotcontroller.domain.repository.CmdRepository
import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.ConnectBleDevice
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.DisconnectBleDevice
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.SendDataToBle
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.dataflow.GetDeviceDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.StartScan
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.StopScanning
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.dataflow.GetBluetoothStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.dataflow.GetConnectionStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ble_usecase.dataflow.GetScanningStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.AddCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.DeleteCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.GetAllCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.UpdateCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetPermissionState
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetToggleButtonState
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.dataflow.GetPermissionsDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.dataflow.GetToggleButtonControlDataFlow
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Application): CmdDatabase =
        Room.databaseBuilder(context,
        CmdDatabase::class.java,
        "cmd_db").build()

    @Provides
    @Singleton
    fun provideDao(db: CmdDatabase): CmdDao = db.cmdDao()

    @Provides
    @Singleton
    fun provideAddCmd(repository: CmdRepository): AddCmd = AddCmd(repository)

    @Provides
    @Singleton
    fun provideDeleteCmd(repository: CmdRepository): DeleteCmd = DeleteCmd(repository)

    @Provides
    @Singleton
    fun provideGetAllCmd(repository: CmdRepository): GetAllCmd = GetAllCmd(repository)

    @Provides
    @Singleton
    fun provideUpdateCmd(repository: CmdRepository): UpdateCmd = UpdateCmd(repository)


    @Provides
    @Singleton
    fun provideBleHelper(@ApplicationContext context: Context): BleHelper = BleHelper(context)

    @Provides
    @Singleton
    fun provideStartScanning(repository: BleRepository): StartScan = StartScan(repository)

    @Provides
    @Singleton
    fun provideStopScanning(repository: BleRepository): StopScanning = StopScanning(repository)

    @Provides
    @Singleton
    fun provideConnectBleDevice(repository: BleRepository): ConnectBleDevice = ConnectBleDevice(repository)

    @Provides
    @Singleton
    fun provideGetDisconnectBleDevice(repository: BleRepository): DisconnectBleDevice =
        DisconnectBleDevice(repository)


    @Provides
    @Singleton
    fun provideSendDataToBle(repository: BleRepository): SendDataToBle = SendDataToBle(repository)

    @Provides
    @Singleton
    fun provideGetDeviceDataFlow(repository: BleRepository): GetDeviceDataFlow =
        GetDeviceDataFlow(repository)

    @Provides
    @Singleton
    fun provideGetConnectionStateFlow(repository: BleRepository): GetConnectionStateFlow =
        GetConnectionStateFlow(repository)

    @Provides
    @Singleton
    fun provideGetBluetoothStateFlow(repository: BleRepository): GetBluetoothStateFlow =
        GetBluetoothStateFlow(repository)

    @Provides
    @Singleton
    fun provideGetScanningStateFlow(repository: BleRepository): GetScanningStateFlow =
        GetScanningStateFlow(repository)


    @Provides
    @Singleton
    fun provideContext(@ApplicationContext app: Context): Context = app


    @Provides
    @Singleton
    fun provideGetPermissionDataFlow(repository: DataStoreRepository): GetPermissionsDataFlow =
        GetPermissionsDataFlow(repository)

    @Provides
    @Singleton
    fun provideSetPermissionOk(repository: DataStoreRepository): SetPermissionState =
        SetPermissionState(repository)

    @Provides
    @Singleton
    fun provideGetToggleButtonControlDataFlow(repository: DataStoreRepository): GetToggleButtonControlDataFlow =
        GetToggleButtonControlDataFlow(repository)

    @Provides
    @Singleton
    fun provideSeToggleButtonState(repository: DataStoreRepository): SetToggleButtonState =
        SetToggleButtonState(repository)













}