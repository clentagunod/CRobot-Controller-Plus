package clentlogic.cloy.crobotcontroller.data.remote.fb


import android.util.Log
import androidx.compose.ui.graphics.rememberGraphicsLayer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FirebaseHelper {


    private var valueEventListener: ValueEventListener? = null
    private val firebaseDb = FirebaseDatabase.getInstance()
    private val ref = firebaseDb.getReference("robot_crawler")

    var onChildRead: ((Any) -> Unit)? = null

    fun realtimeListener(){
        valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data: Any = snapshot.child("movement")
                onChildRead?.invoke(data)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FirebaseHelper", "Error: ${error.message}")
            }

        }
        ref.addValueEventListener(valueEventListener!!)

    }

    fun sendDataFirebase(value: Int){
        ref.child("movement").setValue(value)
    }

    fun stopRealtimeListener(){
        valueEventListener?.let {
            ref.removeEventListener(it)
            valueEventListener = null
        }
    }




}