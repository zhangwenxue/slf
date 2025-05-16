package android.boot.slf

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        LogFacade.installPubCloudShipBook(
            "663869f296288110dd396e8b",
            "37d9b05d5ca306712c8f1b10d0c71d18"
        )
        L.installPubCloudMixPanel("9b0ffe18f0ff60491f540a6f9292c365")
        L.init(this)

        L.i("Test log from opensource!")
        L.e("Test log from opensource!22")
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}