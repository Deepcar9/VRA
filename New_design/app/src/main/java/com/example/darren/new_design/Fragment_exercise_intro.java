package com.example.darren.new_design;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.ArrayList;
import java.util.List;

public class Fragment_exercise_intro extends Fragment implements YouTubePlayer.OnInitializedListener{

    Fragment myFragment;
    TextView Duration, exercise_date;
    Button start_exercise;
    ImageView btn_info_intro;
    int exercise = 0;

    TextView exercise_name;
    com.bluejamesbond.text.DocumentView general_desc;

    static List<Exercise_properties> exerc = new ArrayList<>();    // Holds a list of exercises
    static List<Exercise_description> exdesc = new ArrayList<>();    // Holds a list of exercises descriptions
    Database_Manager db;
    Cursor cur_desc, cur_list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View InputFragmentView = inflater.inflate(R.layout.exercise_intro, container, false);

        /*String ExName = "Side to side head rotation exercise, 6-8 feet away";
        String Description = "Place tablet on a shelf or ledge at roughly eye level. Stand 6-8 feet away. " +
                "The screen has an ‘E’ letter in the middle of the screen. Move your head from side to side " +
                "while focusing on the letter. If the letter starts to go out of focus then slow down. Continue " +
                "this exercise until timer has finished.";*/

        db = new Database_Manager(getActivity());
        db.open();

        cur_desc = db.getExerciseDescriptions();
        cur_desc.moveToPosition(0);
        do{
            exdesc.add(new Exercise_description(cur_desc.getString(1), cur_desc.getString(2), cur_desc.getString(3)));
        }while (cur_desc.moveToNext());

        cur_list = db.getExerciseList(1);
        cur_list.moveToPosition(0);
        do{
            Exercise_Type Type;
            if (cur_list.getString(5).equals("Exercise_Type1")){
                Type = new Exercise_Type2();
            }
            else{
                Type = new Exercise_Type1();
            }
            exerc.add(new Exercise_properties(cur_list.getInt(1), cur_list.getInt(2), cur_list.getString(3), cur_list.getInt(4), Type, cur_list.getInt(6), cur_list.getInt(7), cur_list.getInt(8)));
        }while (cur_list.moveToNext());

        db.close();

        btn_info_intro = (ImageView) InputFragmentView.findViewById(R.id.btn_info_intro);
        btn_info_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = CreateDialog("To improve balance, you have to challenge it. This" +
                        " means you will be unsteady when doing exercises in this application and " +
                        "there is a risk that you might fall. This risk must be minimised to ensure your safety." +
                        "\n       So this week when performing exercises, your therapist recommends:" +

                        "\n\n -Having someone beside you that is able to steady you." +
                        "\n -Having a chair(s)/ couch/ counter beside you to hold/lightly touch" +

                        "\n\nOnly do the exercises in the way your therapist has taught you. If at any time " +
                        "feel unsteady please make sure you have a firm support such as suggested above " +
                        "to help you regain your balance. The aim is to decrease your reliance on the support while remaining SAFE." +

                        "\n\nDuring exercises, don’t forget to blink. If you neck becomes sore use a heating pad or " +
                        "ice pack. If it is not better after three days call your physiotherapist. The exercises " +
                        "may make you dizzy but the dizziness should settle when you finish the exercise." +
                        "\n");
                diaBox.show();
            }
        });

        YouTubePlayerFragment youTubePlayerFragment = YouTubePlayerFragment.newInstance();
        youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_fragment, youTubePlayerFragment).commit();

        exercise_name = (TextView) InputFragmentView.findViewById(R.id.exercise_name);
        exercise_name.setText(exdesc.get(exerc.get(exercise).getexerciseNum()).getName());

        // Loads custom widget for Textview that allows for justification of text
        general_desc = (com.bluejamesbond.text.DocumentView) InputFragmentView.findViewById(R.id.general_desc);
        general_desc.setText(exdesc.get(exerc.get(exercise).getexerciseNum()).getDescription());

        //exercise_date = (TextView) InputFragmentView.findViewById(R.id.exercise_date);
        //String currentDateandTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", java.util.Locale.getDefault()).format(new Date());
        //exercise_date.setText(currentDateandTime);

        Duration = (TextView) InputFragmentView.findViewById(R.id.Duration);
        Duration.setText("Duration : " +exerc.get(exercise).getDuration());

        start_exercise = (Button) InputFragmentView.findViewById(R.id.start_exercise);
        start_exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                myFragment = new Fragment_exercise();
                ft.replace(R.id.content_layout, myFragment)
                        .addToBackStack(null);
                ft.commit();
            }
        });
        return InputFragmentView;
    }


    // If the Youtube Player has been successfully created, load the video for the current exercise you are on.
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        // If a video has not already been loaded into the player
        if(!wasRestored)
        {
            // The cueVideo method takes in the youtube video url identifier in a String format. Exercise 1 for example => pOcgcPUp1_g
            player.cueVideo(exdesc.get(exerc.get(exercise).getexerciseNum()).getIntro_Video());
        }
    }

    // If the Youtube Player has failed to be created return error dialog and or Toast notification of the problem
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        if (result.isUserRecoverableError()) {
            result.getErrorDialog(this.getActivity(), 1).show();
        }
        else {
            Toast.makeText(this.getActivity(), "YouTubePlayer.onInitializationFailure(): " + result.toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    // Dialog Creation method for the information button in the corner of the screen
    private AlertDialog CreateDialog( String message ){
        return new AlertDialog.Builder(getActivity())
                //set message, title
                .setTitle("Guide for Exercise")
                .setMessage(message)
                .create();
    }

}
