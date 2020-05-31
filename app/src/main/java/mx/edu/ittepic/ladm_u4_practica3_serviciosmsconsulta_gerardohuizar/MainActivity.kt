package mx.edu.ittepic.ladm_u4_practica3_serviciosmsconsulta_gerardohuizar

import android.content.pm.PackageManager
import android.database.sqlite.SQLiteException
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    //Permission
    val yesPermisionReadSms = 1
    val yesPermissionReceiverSms = 2
    val yesPermissionSendSms = 3

    var dataPatients = ArrayList<String>()
    val nameDataBase = "INBOX"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("Registro de pacientes")

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.RECEIVE_SMS),yesPermissionReceiverSms)
        }


        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_SMS),yesPermisionReadSms)
        }


        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS) !=
            PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.SEND_SMS), yesPermissionSendSms)
        }


        btnSave.setOnClickListener {
          //  checkEmptyText()
            if (txtName.text.toString().equals("")) {
                txtName.error = "Ingrese un nombre en el campo"
                txtName.requestFocus()
                return@setOnClickListener
            }

            if(txtLastName.text.toString().equals("")){
                txtLastName.error = "No deje el campo de apellido vacio"
                txtLastName.requestFocus()
                return@setOnClickListener
            }

            if (txtAge.text.toString().equals("")) {
                txtAge.error = "Ingrese un valor en el campo Edad"
                txtAge.requestFocus()
                return@setOnClickListener
            }

            if (txtCurp.text.toString().equals("")) {
                txtCurp.error = "Ingrese su CURP en el campo"
                txtCurp.requestFocus()
                return@setOnClickListener
            }

            if (txtCurp.text.toString().length != 10) {
                txtCurp.error = "El formato de la curp cuenta con 10 caracteres"
                txtCurp.requestFocus()
                return@setOnClickListener
            }


            if(txtAge.text.toString().toInt()>100 || txtAge.text.toString().toInt()<0){
                txtAge.error = "Ingrese una edad valida (1 - 100)"
                txtAge.requestFocus()
                return@setOnClickListener
            }


            insertRecord(txtName.text.toString(),txtLastName.text.toString(),txtAge.text.toString(),txtCurp.text.toString(),txtHistory.text.toString())

            cleanTextbox()
        }

        btnConsultar.setOnClickListener {
            loadList()
           //  readLastMessage()
        }



    }//OnCreate

    private fun insertRecord(name:String,lastname:String,age: String,curp:String,history:String){
        try {
            var baseDatos = DataBase(this,nameDataBase,null,1)
            var insertar = baseDatos.writableDatabase
            var SQL = "INSERT INTO INBOX VALUES('${name}','${lastname}','${age}','${curp}','${history}')"
            insertar.execSQL(SQL)
            baseDatos.close()
        }catch (e: SQLiteException){
            e.message.toString()
        }

    }

    private fun readLastMessage() {
        try {
            val cursor = DataBase(this,nameDataBase,null,1)
                .readableDatabase
                .rawQuery("SELECT * FROM INBOX",null)

            var lastSMS = ""

            if(cursor.moveToFirst()){
                do {
                    lastSMS = "ULTIMO SMS RECIBIDO"+"\nNombre:" + cursor.getString(0) + "\nEdad: " + cursor.getString(2) + "\n" + "Curp: " + cursor.getString(3)
                }while (cursor.moveToNext())
            }else{
                lastSMS= "No tienes mensajes, Tabla Vacia"
            }
           // textLastMessage.text = lastSMS


        }catch (er:SQLiteException){
            Toast.makeText(this,er.message,Toast.LENGTH_LONG).show()
        }



    }//Read last Message


    private fun loadList(){
        dataPatients=ArrayList<String>()
        try {
            var databa = DataBase(this,nameDataBase,null,1)
            var select = databa.readableDatabase
            var columns = arrayOf("*")

            var cursor = select.query("INBOX",columns,null,null,null,null,null)


            if(cursor.moveToFirst()){
                do{
                    var temporyPacient = "Nombre: " + cursor.getString(0) +  " "  + cursor.getString(1) + "\n" +"Edad: "  + cursor.getString(2) + "\n" + "CURP: " +  cursor.getString(3)
                    dataPatients.add(temporyPacient)

                }while (cursor.moveToNext())
            }else{

                dataPatients=ArrayList<String>()
                dataPatients.add("Aun no tiene pacientes registrados")
            }


            var adapter =   ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,dataPatients)
            listOfPatients.adapter = adapter
        }catch (er:SQLiteException){
            Toast.makeText(this,er.message, Toast.LENGTH_LONG)
                .show()
        }


    }//End loadList

    private fun cleanTextbox(){
        txtName.setText("")
        txtLastName.setText("")
        txtAge.setText("")
        txtCurp.setText("")
        txtHistory.setText("")
    }

    private fun checkEmptyText(){



    }//End checkEmptyText

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == yesPermissionSendSms){
            Toast.makeText(this,"Permisos de envio de mensajes otorgados",Toast.LENGTH_LONG).show()
        }

        if(requestCode == yesPermissionReceiverSms){
            Toast.makeText(this,"Permisos de recibimiento de mensajes otorgados",Toast.LENGTH_LONG).show()
        }

        if(requestCode == yesPermisionReadSms){
            Toast.makeText(this,"Permisos de lectura de mensajes otorgados",Toast.LENGTH_LONG).show()
        }
    }
}//end MainActivity
