package com.example.openinapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.openinapp.dataclass.MainData
import com.example.openinapp.getretrofit.RetrofitClient
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val authToken:String="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
    private var itemList: List<MainData> = listOf()
    private lateinit var lineChart: LineChart
    private lateinit var location:TextView
    private lateinit var totalClick:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lineChart = findViewById(R.id.linechart)
         location=findViewById(R.id.location_name)
        totalClick=findViewById(R.id.totalcount)
        val buttonOne: TextView = findViewById(R.id.toplinks)
        val buttonTwo: TextView = findViewById(R.id.recentlinks)
        buttonOne.setOnClickListener {
            buttonOne.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.button_background_color
                )
            )
            buttonTwo.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.button_second_background_color
                )
            )
           replaceFragment(ToplinkFrag())
        }
        buttonTwo.setOnClickListener {
            buttonOne.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.button_second_background_color
                )
            )
            buttonTwo.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.button_background_color
                )
            )
          replaceFragment(RecentLinkFrag())
        }
        setupChart()
        callApi()
    }

    private fun callApi(){
        val apiService = RetrofitClient.apiService
        apiService.getDetails(authToken).enqueue(object : Callback<MainData> {
            override fun onResponse(call: Call<MainData>, response: Response<MainData>) {
                if (response.isSuccessful) {
                    val allData = response.body()
                    allData?.let {
                        itemList = listOf(it)
                        //itemAdapter.updateData(itemList[0].data.recent_links)
                        location.text=itemList[0].top_location
                        totalClick.text=itemList[0].today_clicks.toString()

                    }


                }else{
                    Log.e("ToplinkFrag", "Error: ${response.code()} - ${response.message()}")
                    //Toast.makeText(context, "Error: ${response.code()} - ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MainData>, t: Throwable) {
                Log.e("ToplinkFrag", "Failure: ${t.message}", t)
                //Toast.makeText(context, "Some error happened: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("CommitTransaction")
    private fun replaceFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    //     private fun setupChart(){
//        val entries= ArrayList<Entry>()
//         entries.add(Entry(0f, 12f))
//         entries.add(Entry(1f, 74f))
//         entries.add(Entry(2f, 77f))
//         entries.add(Entry(3f, 9f))
//         entries.add(Entry(4f, 31f))
//         entries.add(Entry(5f, 42f))
//         entries.add(Entry(6f, 94f))
//         entries.add(Entry(7f, 18f))
//         entries.add(Entry(8f, 82f))
//         entries.add(Entry(9f, 90f))
//         entries.add(Entry(10f, 18f))
//         entries.add(Entry(11f, 22f))
//         entries.add(Entry(12f, 9f))
//         val lineDataset= LineDataSet(entries,"sampleData")
//         lineDataset.color=Color.BLACK
//         lineDataset.setCircleColor(Color.BLACK)
//         lineDataset.lineWidth=1.5f
//         lineDataset.circleRadius=5f
//         lineDataset.setDrawCircles(true)
//         lineDataset.setDrawValues(false)
//         lineDataset.fillFormatter = IFillFormatter { _, _ -> lineChart.axisLeft.axisMinimum }
//         val drawable = ContextCompat.getDrawable(this, R.drawable.fad_red)
//         lineDataset.fillDrawable = drawable
//         val dataset=ArrayList<ILineDataSet>()
//         dataset.add(lineDataset)
//         val lineData=LineData(dataset)
//         lineChart.data=lineData
//         lineChart.invalidate()
//
//     }
    private fun setupChart() {
        val entries = ArrayList<Entry>()
        // Static data representing monthly data
        entries.add(Entry(0f, 20f))
        entries.add(Entry(1f, 40f))
        entries.add(Entry(2f, 60f))
        entries.add(Entry(3f, 80f))
        entries.add(Entry(4f, 50f))
        entries.add(Entry(5f, 70f))
        entries.add(Entry(6f, 60f))
        entries.add(Entry(7f, 90f))

        val lineDataSet = LineDataSet(entries, "Sample Data")
        lineDataSet.color = Color.BLUE
        lineDataSet.setCircleColor(Color.BLUE)
        lineDataSet.lineWidth = 2f
        lineDataSet.circleRadius = 3f
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = Color.BLUE
        lineDataSet.fillAlpha = 50

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineDataSet)

        val lineData = LineData(dataSets)
        lineChart.data = lineData

        // Customize x-axis labels
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug")
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM

        // Customize y-axis labels
        val yAxisLeft = lineChart.axisLeft
        yAxisLeft.setDrawGridLines(false)
        yAxisLeft.axisMinimum = 0f // This sets the minimum y-axis value

        lineChart.axisRight.isEnabled = false // Disable right y-axis
        lineChart.description.isEnabled = false // Disable description text
        lineChart.legend.isEnabled = false // Disable the legend

        lineChart.invalidate() // refresh
    }

}