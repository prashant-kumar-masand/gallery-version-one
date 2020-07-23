package com.example.galleryversionone;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageFragment extends Fragment {

    private ImageViewModel mViewModel;

    private static final String TAG = "ImageFragment";

    private static final int LIMIT = 10;

    private ImageListAdapter mAdapter;

    private GridLayoutManager mLayoutManager;

    private RecyclerView recyclerView;

    private AtomicInteger mOffset = new AtomicInteger(0);

    private List<ImageDocument> imageList = new ArrayList<>();

    private static DiffUtil.ItemCallback<ImageDocument> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ImageDocument>() {

                @Override
                public boolean areItemsTheSame(ImageDocument oldItem, ImageDocument newItem) {
                    // The ID property identifies when items are the same.
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(ImageDocument oldItem, ImageDocument newItem) {
                    // Don't use the "==" operator here. Either implement and use .equals(),
                    // or write custom data comparison logic here.
                    return oldItem.id.equals(newItem.id);
                }
            };

    public static ImageFragment newInstance() {
        return new ImageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Activity activity = getActivity();
        mViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerview);
        if (mLayoutManager == null) {
            mLayoutManager = new GridLayoutManager(activity, 4, RecyclerView.VERTICAL, false);
        }
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(mLayoutManager);

        if (mAdapter == null) {
            mAdapter = new ImageListAdapter(DIFF_CALLBACK, activity);
        }

        mViewModel.imageItemList.observe(getViewLifecycleOwner(), new Observer<PagedList<ImageDocument>>() {
            @Override
            public void onChanged(PagedList<ImageDocument> imageDocuments) {
                mAdapter.submitList(imageDocuments);
                imageList = imageDocuments;
            }
        });

        mViewModel.selectedItemList$.observe(getViewLifecycleOwner(), new Observer<HashMap<String, Boolean>>() {
            @Override
            public void onChanged(HashMap<String, Boolean> stringBooleanHashMap) {
                mAdapter.setSelectedImages(stringBooleanHashMap);
            }
        });

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ImageListItemTouchListener(getActivity(), recyclerView, new ImageListItemTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println("single click detected ::" + position);
                mViewModel.setSelectedList(imageList.get(position).mAbsolutePath.getPath(), true);
                mAdapter.updateSelectedImageUI(getContext(), imageList.get(position).mAbsolutePath, (ImageView) view.findViewById(R.id.imageview));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                System.out.println("long press detected ::" + position);
            }
        }));

    }

}