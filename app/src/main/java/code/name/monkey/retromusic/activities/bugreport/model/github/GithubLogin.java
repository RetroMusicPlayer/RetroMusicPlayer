package code.name.monkey.retromusic.activities.bugreport.model.github;

import android.text.TextUtils;

public class GithubLogin {
    private final String username;

    private final String password;

    private final String apiToken;

    public GithubLogin(String username, String password) {
        this.username = username;
        this.password = password;
        this.apiToken = null;
    }

    public GithubLogin(String apiToken) {
        this.username = null;
        this.password = null;
        this.apiToken = apiToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean shouldUseApiToken() {
        return TextUtils.isEmpty(username) || TextUtils.isEmpty(password);
    }

    public String getApiToken() {
        return apiToken;
    }

}
