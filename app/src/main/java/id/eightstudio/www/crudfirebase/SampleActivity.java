package id.eightstudio.www.crudfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SampleActivity extends AppCompatActivity {

    public static final String ARTIST_NAME = "id.eightstudio.www.crudfirebase.artistname";
    public static final String ARTIST_ID = "id.eightstudio.www.crudfirebase.artistid";

    DatabaseReference databaseTracks;
    ListView listKu;
    List<String> listTracks = new ArrayList<>();

    private static final String TAG = "SampleActivity";

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String id = intent.getStringExtra(ARTIST_ID);
        //getReference = Membuat child baru
        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);

        databaseTracks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren()){

                    //Cetak manual ada data, tapi ketika ingin di simpan di ArrayList data tidak bisa tersimpan.
                    //System.out.println(data.getValue());
                    Track track = data.getValue(Track.class);

                    listTracks.add(track.getTrackName());

                }

                listKu.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, listTracks));

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        Intent intent = getIntent();
        String id = intent.getStringExtra(ARTIST_ID);
        TextView idLagunya = (TextView) findViewById(R.id.txtIdLagu);
        idLagunya.setText(id);

        listKu  = (ListView) findViewById(R.id.lvPercobaanNested);

        //getReference = Membuat child baru
        databaseTracks = FirebaseDatabase.getInstance().getReference("tracks").child(id);
        String idNya  = databaseTracks.push().getKey();
        Track track = new Track(idNya, "nama", 10);
        databaseTracks.child(idNya).setValue(track);
        Toast.makeText(this, "Track saved", Toast.LENGTH_LONG).show();
    }

}
