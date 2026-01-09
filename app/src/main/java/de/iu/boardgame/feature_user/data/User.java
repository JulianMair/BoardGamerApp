package de.iu.boardgame.feature_user.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "phone")
    public String phone;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "isHostPossible")
    public boolean isHostPossible;

    public User(@NonNull String name, String email, String phone, String address, boolean isHostPossible) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.isHostPossible = isHostPossible;
    }
}
