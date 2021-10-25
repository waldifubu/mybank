package model;

import entity.User;
import util.DatabaseService;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class Model {

    private BankSettingsDTO bankSettingsDTO;
    private String languageStr;
    private String workingDay;
    private List accounts;
    private BigDecimal balance;
    private final DatabaseService databaseService;

    private User user;

    public Model() {
        databaseService = new DatabaseService();
        bankSettingsDTO = new BankSettingsDTO();
    }

    public void logoff() {
//        databaseService.exit();
    }

    public BankSettingsDTO getBankSettingsDTO() {
        return bankSettingsDTO;
    }

    public void setBankSettingsDTO(BankSettingsDTO bankSettingsDTO) {
        this.bankSettingsDTO = bankSettingsDTO;
        languageStr = bankSettingsDTO.getApplicationLanguage();
    }

    public String getLanguageStr() {
        return languageStr;
    }

    public void setLanguageStr(String language) {
        this.languageStr = language;
    }

    public String getWorkingDay() {
        return user.getWorkingDay().toLocalDate().toString();
    }

    public void setWorkingDay(String workingDay) {
        this.workingDay = workingDay;
    }

    public int login(String username, String userPassword) {
        if (!databaseService.isConnected()) {
            databaseService.connect(bankSettingsDTO);
        }

        user = databaseService.login(username, userPassword);
        return user != null ? AppConstants.LOGIN_SUCCESS : AppConstants.WRONG_CREDENTIALS;
    }

    public boolean saveWorkingDay(String workingDay) {
        try {
            user.setWorkingDay(Date.valueOf(workingDay));
        } catch (IllegalArgumentException illegalArgumentException) {
            return false;
        }

        return databaseService.saveUser(user);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void disconnect() {
        databaseService.exit();
    }
}
