package practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02.network;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02.general.Constants;
import practicaltest02.pdsd.systems.cs.pub.ro.practicaltest02.general.Utilities;

public class ClientThread extends Thread {

    private String url;
    private int port;
    private TextView responseTextView;

    private Socket socket;

    public ClientThread(String url, int port, TextView responseTextView) {
        this.url = url;
        this.port = port;
        this.responseTextView = responseTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("127.0.0.1", port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            printWriter.println(url);
            printWriter.flush();
            String requestResponse;
            while ((requestResponse = bufferedReader.readLine()) != null) {
                final String requestResponseInformation = requestResponse;
                responseTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        responseTextView.setText(requestResponseInformation);
                        Toast.makeText(responseTextView.getContext(), requestResponseInformation, Toast.LENGTH_SHORT).show();
                        Log.e(Constants.TAG, requestResponseInformation);
                        //responseTextView.setText("hello");
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}