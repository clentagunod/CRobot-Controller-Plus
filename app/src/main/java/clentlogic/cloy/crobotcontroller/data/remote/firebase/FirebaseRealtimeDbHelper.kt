package clentlogic.cloy.crobotcontroller.data.remote.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseRealtimeDbHelper {

    //TODO: Must refactor this for login page
    private val email = "xyz@gmail.com"
    private val password = "clent2003"


    private var valueEventListener: ValueEventListener? = null
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDb = FirebaseDatabase.getInstance()
    private val refRobotCrawler = firebaseDb.getReference("robot_crawler")

    var onDeviceConnectionState: ((Map<String, Boolean>) -> Unit)? = null




    fun signIn(){
        firebaseAuth.signInWithEmailAndPassword(email, password).apply {
            addOnSuccessListener {
                Log.d("LOGIN", "Login Successful! Result: $it")
            }
            addOnFailureListener {
                Log.d("LOGIN", "Login Error! Result: $it")
            }
        }

    }

    fun realtimeListener(){
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val statusData: Boolean = snapshot.child("isOnline").value as Boolean
                onDeviceConnectionState?.invoke(mapOf("C-Robot Crawler" to statusData, ))
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

    fun stopRealtimeListener(){
        valueEventListener?.let {
            refRobotCrawler.removeEventListener(it)
            valueEventListener = null
        }
    }




}