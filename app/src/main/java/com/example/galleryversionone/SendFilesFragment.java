package com.example.galleryversionone;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.galleryversionone.FileUtils.formatFileSize;

public class SendFilesFragment extends Fragment {

    private SendFilesViewModel mViewModel;
    private MainViewModel mainViewModel;

    public HashMap<Long, ImageDocument> selectedImages = new HashMap<>();

    private TextView sendFilesMsg;

    public static SendFilesFragment newInstance() {
        return new SendFilesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.send_files_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Activity activity = getActivity();

        sendFilesMsg = activity.findViewById(R.id.txt_send_files_info);

//        mViewModel = new ViewModelProvider(this).get(SendFilesViewModel.class);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        mainViewModel.getSelectedItemList().observe(getViewLifecycleOwner(), new Observer<HashMap<Long, ImageDocument>>() {
            @Override
            public void onChanged(HashMap<Long, ImageDocument> imageDocumentHashMap) {
//                imageViewModel.getSelectedItemList().removeObserver(this);
                selectedImages = imageDocumentHashMap;
                Long totalSize = 0L;
                for (Map.Entry<Long, ImageDocument> file : selectedImages.entrySet()) {
                    totalSize += file.getValue().fileSize;
                }
                sendFilesMsg.setText(" No. of files : " + selectedImages.size() + "\nTotal size of files : " + formatFileSize(totalSize));

                writeDataToOutputStream();
            }
        });
    }

    public void writeDataToOutputStream() {
        WriteInBg writeData = new WriteInBg();
        writeData.start();
    }

    public class WriteInBg extends Thread {

        String socketFile = "socket.ser";

        @Override
        public void run() {
            try {
                /* create an object from file*/
                OutputStream socketOut = new FileOutputStream(new File(getActivity().getExternalFilesDir(null), socketFile));
                ObjectOutputStream socketOutput = new ObjectOutputStream(socketOut);

                for (Map.Entry<Long, ImageDocument> file : selectedImages.entrySet()) {

                    ImageDocument obj = file.getValue();

                    System.out.println(obj.fileDisplayName);
                    File fileMetaData;
                    InputStream in;
                    BufferedInputStream bin;

                    /* 1 represent the file read preparation flag  */
                    socketOutput.writeInt(1);
                    /* 2 represent the object is of type raw file */
                    socketOutput.writeInt(2);
                    socketOutput.writeObject(obj);

                    in = getActivity().getContentResolver().openInputStream(Uri.parse(obj.fileContentUri));
                    bin = new BufferedInputStream(in);

                    /* writing file from buffer reader into socket.bin Object output stream */
                    byte[] buffer = new byte[1024];
                    int bytReads;
                    while ((bytReads = bin.read(buffer)) != -1) {
                        socketOutput.write(buffer, 0, bytReads);
                    }
                    socketOutput.reset();

                    /* closing input streams */
                    /*socketOutput.flush();*/
                    bin.close();
                    in.close();
                }

                /* closing output stream with int 0 */
                socketOutput.writeInt(0);
                socketOutput.flush();
                socketOutput.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                /* used to handle closed all open connection in case exception throws */
            }
        }
    }


}