package main.Controller;

public class StateTranslator {

    public static String translateState(String state) {

        switch (state.toCharArray()[0]) {
            case 'R':
                return "Выпущено";
            case 'I':
                return "В работе";
            case 'U':
                return "На проверке";
            case 'P':
                return "Изменение";
            case 'M':
                return "Подготовка производства";
            case 'O':
                return "Запрещено к применению";
            case 'C':
                return "Отменено";
        }
        return state;
    }

    public static void main(String[] args) {
        String wtName = "1.docx%CANCELLED";
        String fileName = wtName.split("%")[0];
        String state = StateTranslator.translateState(wtName.split("%")[1]);

        System.out.println(state);
    }
}
