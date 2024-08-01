package manager;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


class ManagersTest {

    @Test
    public void shouldReturnNotNull(){
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        Assertions.assertNotNull(historyManager);
        Assertions.assertNotNull(taskManager);
    }
}