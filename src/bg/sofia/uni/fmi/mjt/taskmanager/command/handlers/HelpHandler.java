package bg.sofia.uni.fmi.mjt.taskmanager.command.handlers;

import java.util.Map;

public class HelpHandler implements CommandHandler {

    private static final String INSTRUCTIONS =
            """
                Help:
                --date и --due-date трябва да са във формат YYYY-MM-dd
           
                register - регистрация на потребител с username и password.
                $ register --username=<username> --password=<password>
                
                login - потребителят влиза в системата
                $ register --username=<username> --password=<password>
                
                add-collaboration - добавя нова колаборация.
                $ add-collaboration --name=<name>
                
                delete-collaboration - изтрива колаборация.
                $ delete-collaboration --name=<name>
                
                list-collaborations - предоставя информацията всички колаборации на съответния потребител.
                $ list-collaborations
                
                add-task - добавяне на нова задача. Задължителен параметър --name
                $ add-task --collaboration=<name> --name=<name> --date=<date> --due-date=<due-date> --description=<description>
                
                list-tasks - възможни са различни комбинации на аргументите
                $ list-tasks
                $ list-tasks --completed=true
                $ list-tasks --date=<date>
                $ list-tasks --collaboration=<name>
                
                add-user - добавя потребител към колаборацията.
                $ add-user --collaboration=<name> --user=<username>
                
                assign-task - задава assignee за дадена задача в колаборацията.
                $ assign-task --collaboration=<name> --user=<username> --task=<name>
                
                update-task - промяна на някой от атрибутите на задача. Задължителен параметър --name.
                В случай че --date е непразен, командата ще търси и промени задача с подаденото име и дата.
                Не можем да променяме дата след като вече е зададена.
                $ update-task --name=<name> --date=<date> --due-date=<due-date> --description=<description>
                
                delete-task - изтриване на задача.
                $ delete-task --name=<task_name>
                $ delete-task --name=<task_name> --date=<date>
                
                get-task - предоставя информацията за дадена задача в human-readable формат.
                 $ get-task --name=<task_name>
                 $ get-task --name=<task_name> --date=<date>
                
                list-dashboard - предоставя информацията за всички задачи от днешния ден.
                $ list-dashboard
                
                finish-task - маркира конкретната задача като завършена.Задължителен параметър --name (името на задачата).
                $ finish-task --name=<name> --date=<date> --collaboration=<collaboration>
           
                list-users - извежда всички потребители, които са част от колаборацията.
                $ list-users --collaboration=<name>
                
                disconnect - спира връзката със сървъра.
                """;

    @Override
    public String handle(Map<String, String> args, String identifier) {
        return INSTRUCTIONS;
    }

}
