import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class MainTest {
    @Test
    void fun(){
        var inst = new Main();
        Assertions.assertEquals(5, inst.fun());
    }
}
