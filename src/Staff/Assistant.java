package Staff;

import controllers.HomePageController;

import java.time.LocalDate;
import java.time.Period;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.showMessageDialog;

public class Assistant {
    public static HomePageController homePageController;
    public static boolean validatePersonalInfo(String firstName, String lastName, String phoneNumber){
        try {
            if(!firstName.matches("[A-Z][a-z]+|[А-я][а-я]+")) {
                showMessageDialog(null,"Вы ввели некорректное имя", "Ошибка ввода", INFORMATION_MESSAGE);
                return false;
            }
            if(!lastName.matches("[A-Z][a-z]+|[А-я][а-я]+")) {
                showMessageDialog(null,"Вы ввели некорректную фамилию", "Ошибка ввода", INFORMATION_MESSAGE);
                return false;
            }
            if(!phoneNumber.matches("[0-9]{10}")) {
                showMessageDialog(null,"Вы ввели некорректный номер", "Ошибка ввода", INFORMATION_MESSAGE);
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean validateTransactionInfo(String size, LocalDate date, String time) {
        try {
            if(!size.matches("[0-9]{1,10}")) {
                showMessageDialog(null,"Вы ввели некорректный размер транзакции", "Ошибка ввода", INFORMATION_MESSAGE);
                return false;
            }
            if(!time.matches("[0-9][0-9]:[0-9][0-9]:[0-9][0-9]")) {
                showMessageDialog(null,"Вы ввели некорректное время", "Ошибка ввода", INFORMATION_MESSAGE);
                return false;
            }
            if(Period.between(date, LocalDate.now()).getDays() < 0) {
                showMessageDialog(null,"Вы не можете добавить операцию в будущем", "Ошибка ввода", INFORMATION_MESSAGE);
                return false;
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateAccountInfo(String accountName, String balance) {
        try {
            if(accountName == null || accountName.equals("")) {
                showMessageDialog(null,"Вы ввели некорректное название для счета", "Ошибка ввода", INFORMATION_MESSAGE);
                return false;
            }
            Integer.parseInt(balance);

            return true;
        } catch (Exception e) {
            showMessageDialog(null,"Вы ввели некорректное значение баланса", "Ошибка ввода", INFORMATION_MESSAGE);
            e.printStackTrace();
            return false;
        }
    }
}
