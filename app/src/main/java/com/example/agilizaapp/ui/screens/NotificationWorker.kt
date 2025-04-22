package com.example.agilizaapp.ui.screens

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.agilizaapp.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        // Crear canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(
                NotificationChannel(
                    "calendar_channel",
                    "Notificaciones de Pedidos",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }

        val canNotify = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true

        if (canNotify) {
            val codigo = inputData.getString("codigo") ?: return Result.failure()

            val noti = NotificationCompat.Builder(applicationContext, "calendar_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_launcher_foreground))
                .setContentTitle("📦 Tienes un pedido")
                .setContentText("Tienes un pedido, no lo olvides")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("Tienes un pedido programado con código: $codigo. ¡Revisa la app para más detalles!")
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            NotificationManagerCompat.from(applicationContext).notify(1001, noti)
        }

        return Result.success()
    }
}

// Función para convertir fecha "dd/MM/yyyy" a Calendar
fun String.toCalendar(): Calendar {
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return Calendar.getInstance().apply {
        time = formatoFecha.parse(this@toCalendar) ?: Date()
    }
}

// Función para programar notificaciones según pedidos
fun programarNotificaciones(context: Context, pedidos: List<PedidoAgenda>) {
    val workManager = WorkManager.getInstance(context)

    pedidos.forEach { pedido ->
        val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
        val hora = try { formatoHora.parse(pedido.hora) } catch (e: Exception) { null }
        val fecha = try { pedido.fecha.toCalendar() } catch (e: Exception) { null }

        if (hora != null && fecha != null) {
            fecha.set(Calendar.HOUR_OF_DAY, hora.hours)
            fecha.set(Calendar.MINUTE, hora.minutes)
            fecha.set(Calendar.SECOND, 0)
            fecha.set(Calendar.MILLISECOND, 0)

            val delayReal = fecha.timeInMillis - System.currentTimeMillis()

            // 👇 Si el pedido es para mañana o más adelante, notificar en 5 segundos como prueba
            val delayFinal = if (delayReal > 5000) 5000L else delayReal

            if (delayReal > 0) {
                val data = workDataOf("codigo" to pedido.codigo)

                val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                    .setInitialDelay(delayFinal, TimeUnit.MILLISECONDS)
                    .setInputData(data)
                    .build()

                workManager.enqueue(workRequest)
            }
        }
    }
}