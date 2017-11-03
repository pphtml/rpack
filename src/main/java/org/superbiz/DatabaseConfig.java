package org.superbiz;

public class DatabaseConfig {
    private String host;
    private String user;
    private String password;
    private String db;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DatabaseConfig{");
        sb.append("host='").append(host).append('\'');
        sb.append(", user='").append(user).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", db='").append(db).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
