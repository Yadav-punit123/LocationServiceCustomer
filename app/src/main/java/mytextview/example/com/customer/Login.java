package mytextview.example.com.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    private EditText EmailID;
    private EditText Password;
    private Button signIn;
    private Button Register;
    FirebaseAuth mAuth;
    private Button forgotPassword;
    FirebaseDatabase database;
    DatabaseReference custRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EmailID = findViewById(R.id.etemail);
        Password = findViewById(R.id.etpassword);
        signIn = findViewById(R.id.btnlogin);
        Register = findViewById(R.id.btnregister);
        forgotPassword = findViewById(R.id.btnfrgt);
        forgotPassword.setEnabled(false);
        database = FirebaseDatabase.getInstance();
        custRef = database.getReference("User");
        mAuth = FirebaseAuth.getInstance();

        EmailID.setText(getIntent().getStringExtra("email"));
        Password.setText(getIntent().getStringExtra("password"));

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customerLogin();
                //tartActivity(new Intent(getApplicationContext(),Dashboard.class));
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        EmailID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()>5 && charSequence.toString().contains("@") && charSequence.toString().contains("."))
                {
                    forgotPassword.setEnabled(true);
                }
                else {
                    forgotPassword.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = EmailID.getText().toString();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Login.this,"Email sent",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length()>=6;
    }

    public void customerLogin() {
        String customerEmail = EmailID.getText().toString();
        String customerPassword = Password.getText().toString();

        if(!isEmailValid(customerEmail))
        {
            EmailID.setError("Email not valid");
            EmailID.requestFocus();
            return;
        }

        if(!isPasswordValid(customerPassword))
        {
            Password.setError("Minimum 6 characters required");
            Password.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(customerEmail, customerPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(Login.this, dashboard1.class));
                    Login.this.finish();
                }

                if(!task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Wrong Credentials",Toast.LENGTH_LONG);
                }
            }
        });

    }



}
