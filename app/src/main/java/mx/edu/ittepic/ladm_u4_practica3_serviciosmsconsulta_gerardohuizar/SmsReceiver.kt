package mx.edu.ittepic.ladm_u4_practica3_serviciosmsconsulta_gerardohuizar

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.widget.Toast

class SmsReceiver : BroadcastReceiver(){



    override fun onReceive(context: Context, intent: Intent) {


        val extras = intent.extras

        if(extras != null){
            var sms = extras.get("pdus") as Array<Any>
            for(indice in sms.indices){
                var formato = extras.getString("format")

                var smsMensaje = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    SmsMessage.createFromPdu(sms[indice] as ByteArray,formato)
                }else{
                    SmsMessage.createFromPdu(sms[indice] as ByteArray)
                }

                var celularOrigen = smsMensaje.originatingAddress
                var contenidoSMS = smsMensaje.messageBody.toString()
                var data = contenidoSMS.split(" ")


                Toast.makeText(context,"Recibiste mensaje de: "+celularOrigen,Toast.LENGTH_LONG)
                    .show()

                if(data.size !=3){


                }else{
                    if(!(data[0].equals("PACIENTE"))){

                        SmsManager.getDefault().sendTextMessage(
                            celularOrigen,null,
                            "Envia la palabra\nPaciente: [Nombre] [Edad] [CURP]",null,null)
                    }else{
                        if(data[2].toString().length != 2 ){

                            SmsManager.getDefault().sendTextMessage(
                                celularOrigen,null,
                                "Formato de edad incorrecto si eres menor a 10 años debes ingresar un [0] seguido de tu edad" +
                                        "\n Ejemplo:\nPaciente: Gerardo 07 HUML970204",null,null)

                        }else{
                            if(data[3].toString().length != 10){

                                SmsManager.getDefault().sendTextMessage(
                                    celularOrigen,null,
                                    "Formato incorrecto la curp consta de 10 caracteres\n" +
                                            "Ejemplo\nPaciente: Gerardo 07 HUML970204",null,null)

                            }else{
                                //
                                try {

                                    val cursor = DataBase(context,"INBOX",null,1)
                                        .readableDatabase
                                        .rawQuery("SELECT * FROM INBOX WHERE CURP = '${data[3]}'",null)
                                    if(cursor.moveToNext()){

                                        val smsData = "Hola: " + cursor.getString(1) + "tu historial clinico es el siguiente: \n"+cursor.getString(4) + "\nUn gusto poder atenderte"

                                        SmsManager.getDefault().sendTextMessage(
                                            celularOrigen,null,
                                            ""+smsData,null,null)

                                    }else{

                                        val smsData = "Verifique que los datos ingresados sean correctos y si no esta registrado lo invitamos a registrarse con su medico de preferencia "
                                        SmsManager.getDefault().sendTextMessage(celularOrigen,null," " + smsData,null,null)

                                    }
                                }catch (e: SQLiteException){
                                    SmsManager.getDefault().sendTextMessage(
                                        celularOrigen,null,
                                        e.message,null,null)

                                }




                            }
                        }
                    }

                }//End else Principal


            }//End for

        }//End if principal Extras

    }//End on receiver

}//End class