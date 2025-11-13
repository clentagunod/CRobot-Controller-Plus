package clentlogic.cloy.crobotcontroller.data.remote.firebase

import android.util.Log
import clentlogic.cloy.crobotcontroller.domain.model.firebase.RobotModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseRealtimeDbHelper {
    //TODO: TEMPORARY FIREBASE SET UP, THIS WILL BE UPDATED SOON!


    private var valueEventListener: ValueEventListener? = null
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDb = FirebaseDatabase.getInstance()
    private val refRobotCrawler = firebaseDb.getReference("robot_crawler")


    var onDeviceConnectionState: ((List<RobotModel>) -> Unit)? = null


    fun signIn(email: String, password: String, onSignIn: (Boolean, String) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).apply {
            addOnSuccessListener {
                Log.d("LOGIN", "Login Successful! Result: $it")
                onSignIn.invoke(true, it.user?.email ?: "Login Successful!")

            }
            addOnFailureListener {
                Log.d("LOGIN", "Login Error! Result: $it")
                onSignIn.invoke(false, it.message ?: "Error!")
            }
        }

    }

    fun realtimeListener(){
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val statusData: Boolean = snapshot.child("isOnline").value as Boolean
                val isInUse: Boolean = snapshot.child("isInUse").value as Boolean

                val robotCrawler = RobotModel("Crawler-001", statusData, isInUse)
                onDeviceConnectionState?.invoke(listOf(robotCrawler))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FirebaseRealtimeDbHelper", "Error: ${error.message}")
            }

        }
        refRobotCrawler.addValueEventListener(valueEventListener!!)

    }

    fun sendDataFirebase(value: Int){
        refRobotCrawler.child("movement").setValue(value)
    }

    fun updateDataFirebase(value: Boolean){
        refRobotCrawler.child("isInUse").setValue(value)
    }

    fun stopRealtimeListener(){
        valueEventListener?.let {
            refRobotCrawler.removeEventListener(it)
            valueEventListener = null
        }
    }
    fun signOut(onSignOut: (Boolean, String) -> Unit) {
        try {
            firebaseAuth.signOut()
            Log.d("LOGOUT", "Logout Successful!")
            onSignOut(true, "Logout Successful!")
        } catch (e: Exception) {
            Log.e("LOGOUT", "Logout Failed", e)
            onSignOut(false, e.message ?: "Logout Failed!")
        }
    }





}