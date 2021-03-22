package huaji.hamilton.toggleadb

import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import java.io.DataOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    fun sudo(vararg strings: String) {
        try {
            val su = Runtime.getRuntime().exec("su")
            val outputStream = DataOutputStream(su.outputStream)
            for (s in strings) {
                outputStream.writeBytes(s + "\n")
                outputStream.flush()
            }
            outputStream.writeBytes("exit\n")
            outputStream.flush()
            try {
                su.waitFor()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val s = findViewById<Switch>(R.id.TheSwitch)
        s.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sudo("setprop service.adb.tcp.port 5555", "stop adbd", "start adbd")
            } else {
                sudo("setprop service.adb.tcp.port -1", "stop adbd", "start adbd")
            }
        }
    }
}