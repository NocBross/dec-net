package main.java.abstraction;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class RegisterData {

    private StringProperty nickname;
    private StringProperty resourceHubAddress;
    private StringProperty userID;
    private StringProperty password;
    private StringProperty repeatedPassword;

    public RegisterData() {
        nickname = new SimpleStringProperty();
        resourceHubAddress = new SimpleStringProperty();
        userID = new SimpleStringProperty();
        password = new SimpleStringProperty();
        repeatedPassword = new SimpleStringProperty();

        nickname.addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                userID.set(newValue.toLowerCase() + "@" + getResourceHubAddress());
            }

        });
        resourceHubAddress.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                userID.set(getNickname() + "@" + newValue.toLowerCase());
            }
        });
    }

    /**
     * Returns the current mail.
     * 
     * @return current mail
     */
    public String getNickname() {
        return nickname.get();
    }

    /**
     * Returns the mail property.
     * 
     * @return
     */
    public StringProperty getNicknameProperty() {
        return nickname;
    }

    /**
     * Returns the current resource hub address.
     * 
     * @return resource hub address
     */
    public String getResourceHubAddress() {
        return resourceHubAddress.get();
    }

    /**
     * Returns the resource hub property.
     * 
     * @return resource hub property
     */
    public StringProperty getResourceHubAddressProperty() {
        return resourceHubAddress;
    }

    /**
     * Returns the current userID.
     * 
     * @return userID
     */
    public String getUserID() {
        return userID.get();
    }

    /**
     * Returns the userID property
     * 
     * @return userID property
     */
    public StringProperty getUserIDProperty() {
        return userID;
    }

    /**
     * Returns the current password.
     * 
     * @return current password
     */
    public String getPassword() {
        return password.get();
    }

    /**
     * Returns the password property.
     * 
     * @return password property
     */
    public StringProperty getPasswordProperty() {
        return password;
    }

    /**
     * Returns the current repeat password
     * 
     * @return current repeat password
     */
    public String getRepeatedPassword() {
        return repeatedPassword.get();
    }

    /**
     * Returns the repeat password property.
     * 
     * @return repeat password property
     */
    public StringProperty getRepeatedPasswordProperty() {
        return repeatedPassword;
    }
}
