package model;

public class BankModel {
    public String language;
    private BankSettingsDTO bankSettingsDTO;
    private String workingDay;
    private String username;
    private String userpassword;

    public BankSettingsDTO getBankSettingsDTO() {
        return bankSettingsDTO;
    }

    public void setBankSettingsDTO(BankSettingsDTO bankSettingsDTO) {
        this.bankSettingsDTO = bankSettingsDTO;
        language = bankSettingsDTO.getApplicationLanguage();
    }

    public BankModel() {
        bankSettingsDTO = new BankSettingsDTO();
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Language getLanguage() {
        return new Language(language);
    }

    public String getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(String workingDay) {
        this.workingDay = workingDay;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }
}
