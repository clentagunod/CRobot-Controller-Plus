package clentlogic.cloy.crobotcontroller.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import clentlogic.cloy.crobotcontroller.data.communication.ble.BleHelper
import clentlogic.cloy.crobotcontroller.data.communication.wifi.WifiHelper
import clentlogic.cloy.crobotcontroller.data.local.roomdb.dao.RobotDao
import clentlogic.cloy.crobotcontroller.data.local.roomdb.db.CmdDatabase
import clentlogic.cloy.crobotcontroller.data.remote.firebase.FirebaseRealtimeDbHelper
import clentlogic.cloy.crobotcontroller.domain.repository.CmdRepository
import clentlogic.cloy.crobotcontroller.domain.repository.DataStoreRepository
import clentlogic.cloy.crobotcontroller.domain.repository.RobotControllerRepository
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.AddCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.DeleteCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.GetAllCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.UpdateCmd
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.dataflow.GetControlModeDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.dataflow.GetPermissionsDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.db_usecase.dataflow.GetToggleButtonControlDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetControlMode
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetPermissionState
import clentlogic.cloy.crobotcontroller.domain.usecase.ds_usecase.SetToggleButtonState
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.ConnectRobot
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.DisconnectRobot
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.SendDataToRobot
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.StartScan
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.StopScanning
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetBluetoothStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetConnectionStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetRobotModel
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetDeviceDataFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetScanningStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetWifiConnectionStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetWifiHasInternet
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.GetWifiStateFlow
import clentlogic.cloy.crobotcontroller.domain.usecase.robot_controller_usecase.dataflow.UpdateData
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
    fun provideDao(db: CmdDatabase): RobotDao = db.cmdDao()

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
    fun provideWifiHelper(@ApplicationContext context: Context): WifiHelper = WifiHelper(context)

    @Provides
    @Singleton
    fun provideStartScanning(repository: RobotControllerRepository): StartScan = StartScan(repository)

    @Provides
    @Singleton
    fun provideStopScanning(repository: RobotControllerRepository): StopScanning = StopScanning(repository)

    @Provides
    @Singleton
    fun provideConnectBleDevice(repository: RobotControllerRepository): ConnectRobot = ConnectRobot(repository)

    @Provides
    @Singleton
    fun provideGetDisconnectBleDevice(repository: RobotControllerRepository): DisconnectRobot =
        DisconnectRobot(repository)


    @Provides
    @Singleton
    fun provideSendDataToBle(repository: RobotControllerRepository): SendDataToRobot = SendDataToRobot(repository)

    @Provides
    @Singleton
    fun provideUpdateData(repository: RobotControllerRepository): UpdateData = UpdateData(repository)

    @Provides
    @Singleton
    fun provideGetDeviceDataFlow(repository: RobotControllerRepository): GetDeviceDataFlow =
        GetDeviceDataFlow(repository)

    @Provides
    @Singleton
    fun provideGetConnectionStateFlow(repository: RobotControllerRepository): GetConnectionStateFlow =
        GetConnectionStateFlow(repository)

    @Provides
    @Singleton
    fun provideGetBluetoothStateFlow(repository: RobotControllerRepository): GetBluetoothStateFlow =
        GetBluetoothStateFlow(repository)

    @Provides
    @Singleton
    fun provideGetScanningStateFlow(repository: RobotControllerRepository): GetScanningStateFlow =
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
    fun provideSetToggleButtonState(repository: DataStoreRepository): SetToggleButtonState =
        SetToggleButtonState(repository)


    @Provides
    @Singleton
    fun provideGetControlModeState(repository: DataStoreRepository): GetControlModeDataFlow =
        GetControlModeDataFlow(repository)

    @Provides
    @Singleton
    fun provideSetControlMode(repository: DataStoreRepository): SetControlMode =
        SetControlMode(repository)

    @Provides
    @Singleton
    fun provideFirebaseHelper(): FirebaseRealtimeDbHelper = FirebaseRealtimeDbHelper()

    @Provides
    @Singleton
    fun provideGetRobotModel(repository: RobotControllerRepository): GetRobotModel =
        GetRobotModel(repository)

    @Provides
    @Singleton
    fun provideWifiStateFlow(repository: RobotControllerRepository): GetWifiStateFlow =
        GetWifiStateFlow(repository)

    @Provides
    @Singleton
    fun provideWifiConnectionStateFlow(repository: RobotControllerRepository): GetWifiConnectionStateFlow =
        GetWifiConnectionStateFlow(repository)

    @Provides
    @Singleton
    fun provideWifiHasInternet(repository: RobotControllerRepository): GetWifiHasInternet =
        GetWifiHasInternet(repository)








}