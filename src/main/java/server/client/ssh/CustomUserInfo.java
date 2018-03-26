package server.client.ssh;

import com.jcraft.jsch.UserInfo;

public class CustomUserInfo implements UserInfo {

    public CustomUserInfo(String password) {
        this.password = password;
    }

    private String password;

    @Override
    public String getPassphrase() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean promptPassword(String s) {
        return true;
    }

    @Override
    public boolean promptPassphrase(String s) {
        return true;
    }

    @Override
    public boolean promptYesNo(String s) {
        return true;
    }

    @Override
    public void showMessage(String msg) {
        System.out.print(msg);
    }
}
