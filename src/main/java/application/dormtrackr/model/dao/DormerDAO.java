package application.dormtrackr.model.dao;

import application.dormtrackr.model.Dormer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DormerDAO {

    public String getDormerCount(){
        return "4";
    }

    public ObservableList<Dormer> getDormers(){
        ObservableList<Dormer> dormers = FXCollections.observableArrayList();

        dormers.add(new Dormer(1,"John", "Doe", "1234567890", "john.doe@example.com", 101));
        dormers.add(new Dormer(2,"Jane", "Smith", "0987654321", "jane.smith@example.com", 102));
        dormers.add(new Dormer(3, "Alice", "Johnson", "1112223333", "alice.johnson@example.com", 103));
        dormers.add(new Dormer(4, "Bob", "Brown", "4445556666", "bob.brown@example.com",104));

        return dormers;
    }

}
