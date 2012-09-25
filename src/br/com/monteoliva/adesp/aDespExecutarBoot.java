package br.com.monteoliva.adesp;

// imports da API do JAVA
import java.util.Calendar;

// imports da API do ANDROID
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

public class aDespExecutarBoot extends BroadcastReceiver {
	/**
	 * @see android.content.BroadcastReceiver#onReceive(Context,Intent)
	 */
	@Override
	public void onReceive(Context contexto, Intent intent) {
		// executa o agendamento para mostrar os vencimentos
		agendar(contexto, 10);
	}

    /**
     * Metodo para gerar o agendamernto
     * 
     * @param contexto
     * @param segundos
     */
    private void agendar(Context contexto, int segundos) {
        // Repetir a cada 5 minutos
        final int tempoRepetir = 300 * 1000;
    
        // seta o ponto de partida
        Intent it = new Intent("ADESP_EXECUTAR_ALARME");

        // PendingIntent para executar
        PendingIntent pIntent = PendingIntent.getBroadcast(contexto, 0, it, 0);
        
        // Para executar o alarme depois de 5 segundos a partir de agora
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.SECOND, segundos);

        // Agenda o alarme
        AlarmManager alarme = (AlarmManager) contexto.getSystemService(Context.ALARM_SERVICE);
        long tempo = c.getTimeInMillis();

        // Repetir a cada 5 minutos
        alarme.setRepeating(AlarmManager.RTC_WAKEUP, tempo, tempoRepetir, pIntent);
    }
}