package de.iu.boardgame.feature_user.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import de.iu.boardgame.R;
import de.iu.boardgame.feature_user.data.User;
import de.iu.boardgame.feature_user.helpers.SessionManager;
import de.iu.boardgame.feature_user.viewmodel.UsersViewModel;
import de.iu.boardgame.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private UsersViewModel viewModel;
    private ArrayAdapter<String> adapter;
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);

        ListView listView = findViewById(R.id.listUsers);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (position >=0 && position < users.size()){
                long userId = users.get(position).id;
                SessionManager.setCurrentUserId(LoginActivity.this, userId);
                Toast.makeText(this, "Eingeloggt als " + users.get(position).name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        listView.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            if (position < 0 || position >= users.size()) return true;
            final User userToDelete = users.get(position);
            new AlertDialog.Builder(this)
                    .setTitle("Benutzer löschen")
                    .setMessage("Benutzer '" + userToDelete.name + "' wirklich löschen? Diese Aktion ist dauerhaft.")
                    .setNegativeButton("Abbrechen", (dialog, which) -> dialog.dismiss())
                    .setPositiveButton("Löschen", (dialog, which) -> {
                        viewModel.deleteUser(userToDelete.id);
                        Toast.makeText(this, "Benutzer gelöscht", Toast.LENGTH_SHORT).show();
                    })
                    .show();
            return true;
        });

        EditText etName = findViewById(R.id.etName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPhone = findViewById(R.id.etPhone);
        EditText etAddress = findViewById(R.id.etAddress);
        CheckBox cbHost = findViewById(R.id.cbHost);
        Button btnCreate = findViewById(R.id.btnCreateUser);

        btnCreate.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            if (name.isEmpty()){ etName.setError("Name erforderlich"); return; }
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            boolean host = cbHost.isChecked();

            User newUser = new User(name, email, phone, address, host);
            viewModel.createUser(newUser, id -> runOnUiThread(() -> {
                if (id > 0){
                    SessionManager.setCurrentUserId(LoginActivity.this, id);
                    Toast.makeText(this, "Benutzer erstellt und eingeloggt", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Fehler beim Erstellen", Toast.LENGTH_SHORT).show();
                }
            }));
        });

        viewModel.getAllUsers().observe(this, list -> {
            users = list != null ? list : new ArrayList<>();
            List<String> names = new ArrayList<>();
            for (User u : users) names.add(u.name);
            adapter.clear();
            adapter.addAll(names);
            adapter.notifyDataSetChanged();
        });
    }
}
