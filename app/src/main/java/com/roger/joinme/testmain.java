package com.roger.joinme;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class testmain extends AppCompatActivity
{
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmain);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserID = mAuth.getCurrentUser().getUid();


//        mToolbar =findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);


        myViewPager = (ViewPager) findViewById(R.id.main_tabs_pager);
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);


        myTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
    }


//
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        super.onCreateOptionsMenu(menu);
//
//        getMenuInflater().inflate(R.menu.options_menu, menu);
//
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        super.onOptionsItemSelected(item);
//
//        if (item.getItemId() == R.id.main_logout_option)
//        {
//            updateUserStatus("offline");
//            mAuth.signOut();
//            SendUserToLoginActivity();
//        }
//        if (item.getItemId() == R.id.main_settings_option)
//        {
//            SendUserToSettingsActivity();
//        }
//        if (item.getItemId() == R.id.main_create_group_option)
//        {
//            RequestNewGroup();
//        }
//        if (item.getItemId() == R.id.main_find_friends_option)
//        {
//            SendUserToFindFriendsActivity();
//        }
//
//        return true;
//    }
//
//
//    private void RequestNewGroup()
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
//        builder.setTitle("Enter Group Name :");
//
//        final EditText groupNameField = new EditText(MainActivity.this);
//        groupNameField.setHint("e.g Coding Cafe");
//        builder.setView(groupNameField);
//
//        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i)
//            {
//                String groupName = groupNameField.getText().toString();
//
//                if (TextUtils.isEmpty(groupName))
//                {
//                    Toast.makeText(MainActivity.this, "Please write Group Name...", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    CreateNewGroup(groupName);
//                }
//            }
//        });
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i)
//            {
//                dialogInterface.cancel();
//            }
//        });
//
//        builder.show();
//    }
//
//
//
//    private void CreateNewGroup(final String groupName)
//    {
//        RootRef.child("Groups").child(groupName).setValue("")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task)
//                    {
//                        if (task.isSuccessful())
//                        {
//                            Toast.makeText(MainActivity.this, groupName + " group is Created Successfully...", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//

//
//    private void SendUserToSettingsActivity()
//    {
//        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
//        startActivity(settingsIntent);
//    }
//
//
//    private void SendUserToFindFriendsActivity()
//    {
//        Intent findFriendsIntent = new Intent(MainActivity.this, FindFriendsActivity.class);
//        startActivity(findFriendsIntent);
//    }
//
//
//

}