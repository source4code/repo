package info.source4code.security;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

public class TestLoginModule implements LoginModule {

    private Map<String, String> USERS = new HashMap<String, String>();

    private CallbackHandler callbackHandler;
    private boolean loggedIn;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
            Map<String, ?> sharedState, Map<String, ?> options) {

        this.callbackHandler = callbackHandler;

        // load the users from a properties file
        String filePath = (String) options.get("file");
        if (filePath != null) {
            BufferedReader bufferedReader = null;

            try {
                String currentLine;
                bufferedReader = new BufferedReader(new FileReader(filePath));

                while ((currentLine = bufferedReader.readLine()) != null) {
                    if (!currentLine.startsWith("#")) {
                        String[] userSplit = currentLine.split(": ");
                        String userName = userSplit[0];

                        String[] passwordSplit = userSplit[1].split(",");
                        String password = passwordSplit[0];
                        USERS.put(userName, password);
                    }
                }
            } catch (IOException e) {
                // TODO handle exception
            } finally {
                try {
                    if (bufferedReader != null)
                        bufferedReader.close();
                } catch (IOException ex) {
                    // TODO handle exception
                }
            }

        }
    }

    @Override
    public boolean login() throws LoginException {
        boolean result = false;

        if (callbackHandler == null) {
            throw new LoginException("No callback handler supplied.");
        }

        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("Username");
        callbacks[1] = new PasswordCallback("Password", false);

        try {
            callbackHandler.handle(callbacks);
            String userName = ((NameCallback) callbacks[0]).getName();
            char[] passwordCharArray = ((PasswordCallback) callbacks[1])
                    .getPassword();
            String password = new String(passwordCharArray);

            if (password != null) {
                String expectedPassword = USERS.get(userName);
                if (password.equalsIgnoreCase(expectedPassword)) {
                    result = true;
                    loggedIn = true;
                }
            }
        } catch (IOException ioException) {
            throw new LoginException("IOException occured: "
                    + ioException.getMessage());
        } catch (UnsupportedCallbackException unsupportedCallbackException) {
            throw new LoginException(
                    "UnsupportedCallbackException encountered: "
                            + unsupportedCallbackException.getMessage());
        }

        return result;
    }

    @Override
    public boolean commit() throws LoginException {
        return loggedIn;
    }

    @Override
    public boolean abort() throws LoginException {
        loggedIn = false;
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        loggedIn = false;
        return true;
    }
}
