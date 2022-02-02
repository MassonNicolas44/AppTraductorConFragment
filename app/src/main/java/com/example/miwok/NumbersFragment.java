package com.example.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NumbersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NumbersFragment extends Fragment {

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener= new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                //Pause playback
                mMediaPlayer.pause();
                //Back 0 music
                mMediaPlayer.seekTo(0);
            }else if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
                //Resume playback
                mMediaPlayer.start();
            }else if (focusChange==AudioManager.AUDIOFOCUS_LOSS){
                releaseMediaPlayer();
            }

        }
    };

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> words = new ArrayList<Word>();

        words.add(new Word("one","lutti", R.drawable.number_one,R.raw.number_one));
        words.add(new Word("two","ottiko", R.drawable.number_two,R.raw.number_two));
        words.add(new Word("three","tolookosu", R.drawable.number_three,R.raw.number_three));
        words.add(new Word("four","oyyisa", R.drawable.number_four,R.raw.number_four));
        words.add(new Word("five","massokka", R.drawable.number_five,R.raw.number_five));
        words.add(new Word("six","temmokka", R.drawable.number_six,R.raw.number_six));
        words.add(new Word("seven","kenekaku", R.drawable.number_seven,R.raw.number_seven));
        words.add(new Word("eight","kawinta", R.drawable.number_eight,R.raw.number_eight));
        words.add(new Word("nine","wo'e", R.drawable.number_nine,R.raw.number_nine));
        words.add(new Word("ten","na'aacha", R.drawable.number_ten,R.raw.number_ten));


        WordAdapter adapter = new WordAdapter (getActivity(), words,R.color.category_numbers);

        ListView listView = (ListView) rootView.findViewById(R.id.vista);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                releaseMediaPlayer();

                Word word = words.get(position);

                int result= mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                if (result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED){

                    Toast.makeText(getActivity(), ""+word.toString(), Toast.LENGTH_SHORT).show();
                    mMediaPlayer =MediaPlayer.create(getActivity(),word.getAudioResourceId());
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
        return rootView;
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NumbersFragment() {
    }

    public static NumbersFragment newInstance(String param1, String param2) {
        NumbersFragment fragment = new NumbersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

}