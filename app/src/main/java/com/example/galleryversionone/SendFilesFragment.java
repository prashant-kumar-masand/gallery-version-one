package com.example.galleryversionone;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.galleryversionone.FileUtils.formatFileSize;
import static com.example.galleryversionone.FileUtils.getThumbnailInputStream;

public class SendFilesFragment extends Fragment {

    private SendFilesViewModel sendFilesViewModel;
    private MainViewModel mainViewModel;

    public HashMap<Long, ImageDocument> selectedImages = new HashMap<>();

    private TextView sendFilesMsg;
    private Button btn;
    private static final String TAG = "send files fragment";

    private RecyclerView receivedFilesRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private ReceivedFileListAdapter listAdapter;
    private Handler mHandler;

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

        mHandler = new Handler();

        btn = activity.findViewById(R.id.btn_read_file_data);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readDataFromOutputStream();
            }
        });

        receivedFilesRecyclerView = activity.findViewById(R.id.rv_transer_files);
        gridLayoutManager = new GridLayoutManager(activity, 5, RecyclerView.VERTICAL, false);

        receivedFilesRecyclerView.setHasFixedSize(false);
        receivedFilesRecyclerView.setLayoutManager(gridLayoutManager);

        listAdapter = new ReceivedFileListAdapter(activity);
        receivedFilesRecyclerView.setAdapter(listAdapter);

        sendFilesViewModel = new ViewModelProvider(this).get(SendFilesViewModel.class);
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

        sendFilesViewModel.getThumbnail().observe(getViewLifecycleOwner(), new Observer<List<Bitmap>>() {
            @Override
            public void onChanged(List<Bitmap> bitmaps) {
                listAdapter.updateThumbnailList(bitmaps);
                listAdapter.notifyDataSetChanged();
            }
        });

        sendFilesViewModel.getMapper().observe(getViewLifecycleOwner(), new Observer<Map<Integer, Integer>>() {
            @Override
            public void onChanged(Map<Integer, Integer> integerIntegerMap) {
                sendFilesViewModel.getMapper().removeObserver(this);
                listAdapter.updateThumbnailMapper(integerIntegerMap);
            }
        });
    }

    public void writeDataToOutputStream() {
        WriteInBg writeInBg = new WriteInBg();
        writeInBg.start();
    }

    public class WriteInBg extends Thread{

        @Override
        public void run() {
            String socketFile = "socket.ser";

            try {
                /* create an object from file*/
                OutputStream socketOut = new FileOutputStream(new File(getActivity().getExternalFilesDir(null), socketFile));
                ObjectOutputStream socketOutput = new ObjectOutputStream(socketOut);

                for (Map.Entry<Long, ImageDocument> file : selectedImages.entrySet()) {

                    ImageDocument obj = file.getValue();

                    InputStream in;
                    BufferedInputStream bin;

                    /* 1 represent the file read preparation flag  */
                    socketOutput.writeInt(1);
                    /* 2 represent the object is of type file */
                    socketOutput.writeInt(2);
                    socketOutput.writeObject(obj);
                    /* 3 represent the image thumbnail */
                    socketOutput.writeInt(3);

                    in = getThumbnailInputStream(getContext().getContentResolver(), obj.id);
                    bin = new BufferedInputStream(in);
                    /* writing thumbnail file size */
                    socketOutput.writeLong(in.available());

                    /* writing file from buffer reader into socket.bin Object output stream */
                    byte[] buffer = new byte[1024];
                    int bytReads = 0;
                    while ((bytReads = bin.read(buffer)) != -1) {
                        socketOutput.write(buffer, 0, bytReads);
                    }
                    socketOutput.reset();

                    /* closing input streams */
                    bin.close();
                    in.close();

                    /* 4 represent the image file */
                    socketOutput.writeInt(4);

                    in = getActivity().getContentResolver().openInputStream(Uri.parse(obj.fileContentUri));
                    bin = new BufferedInputStream(in);

                    /* writing file from buffer reader into socket.bin Object output stream */
                    bytReads = 0;
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

    public void readDataFromOutputStream() {

        ReadInBg readInBg = new ReadInBg();
        readInBg.start();

    }

    public class ReadInBg extends Thread{

        @Override
        public void run() {

            String socketFile = "socket.ser";

            try {

                InputStream sinput = new FileInputStream(new File(getActivity().getExternalFilesDir(null), socketFile));
                ObjectInputStream sobj = new ObjectInputStream(sinput);
                Bitmap bitmap = null;
                int thumbnailNumber = 0;


                while (sobj.readInt() == 1) {
                    ImageDocument fileObj;
                    OutputStream out;
                    BufferedOutputStream bout;

                    if (sobj.readInt() == 2) {
                        fileObj = (ImageDocument) sobj.readObject();

                        byte[] buffer = new byte[1024];
                        int bytReads = 0;
                        int length = 0;

                        if (sobj.readInt() == 3) {

                            int thunbnailLength = (int) sobj.readLong();
                            byte[] thumbnailBuffer = new byte[thunbnailLength];
                            sobj.readFully(thumbnailBuffer, 0, thunbnailLength);
                            bitmap = BitmapFactory.decodeByteArray(thumbnailBuffer, 0, thunbnailLength);

                            System.out.println("received bitmap : "+bitmap.getAllocationByteCount()+" of length "+ thunbnailLength);
                            sendFilesViewModel.addToThumbnailList(bitmap);

                        }

//                    if (sobj.readInt() == 3) {
//                        out = new FileOutputStream(new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "thumbnail.jpeg"));
//                        bout = new BufferedOutputStream(out);
//                        length = (int) sobj.readLong();
//                        while (length > 0) {
//                            bytReads = sobj.read(buffer);
//                            length -= bytReads;
//                            bout.write(buffer, 0, bytReads);
//                            Log.i(TAG, "readDataFromOutputStream: " + bytReads + ":" + sobj.available());
//                        }
//                        Log.i(TAG, "readDataFromOutputStream: " + bytReads + " " + sobj.available());
//                        bout.flush();
//                        bout.close();
//                        out.close();
//
////                        File smallFile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "thumbnail.jpeg");
////                        System.out.println(Uri.fromFile(smallFile).toString());
////                        Bitmap bitmap = BitmapFactory.decodeFile(Uri.fromFile(smallFile).toString());
//                    }

                        if (sobj.readInt() == 4) {
                            out = new FileOutputStream(new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileObj.fileDisplayName));
                            bout = new BufferedOutputStream(out);

                            bytReads = 0;
                            length = fileObj.fileSize.intValue();
                            System.out.println("file length :"+ length);

                            int chunk = 1024*10;
                            int chunkSize = length / chunk;
                            int chunkCount = 0;
                            int imageHeight = 91;
                            System.out.println("chunk size "+ chunkSize);

                            while (length > 0) {
                                bytReads = sobj.read(buffer);
                                length -= bytReads;
                                bout.write(buffer, 0, bytReads);
                                Log.i(TAG, "readDataFromOutputStream: " + bytReads + ":" + sobj.available());
                                if(chunkCount >= chunkSize){
                                    System.out.println("count "+chunkCount);
                                    chunkCount = 0;
                                    imageHeight-=10;
                                    System.out.println("image height "+ imageHeight);
                                    Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, 100, Math.max(1, imageHeight));
//                                    sendFilesViewModel.updateThumbnailList(thumbnailNumber, newBitmap);
                                    sendFilesViewModel.updateThumbnailImageMap(thumbnailNumber, Math.max(1, imageHeight));

                                    final int thumbnailNumberToUpdate = thumbnailNumber;
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listAdapter.notifyItemChanged(thumbnailNumberToUpdate);
                                        }
                                    });
                                    Thread.currentThread().sleep(500);
                                }else{
                                    chunkCount++;
                                }
                            }
                            Log.i(TAG, "readDataFromOutputStream: " + bytReads + " " + sobj.available());
                            bout.flush();
                            bout.close();
                            out.close();

                            thumbnailNumber++;
                        }

                    }
                }

                sobj.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                Log.i(TAG, "readDataFromOutputStream: end of file reached");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

}