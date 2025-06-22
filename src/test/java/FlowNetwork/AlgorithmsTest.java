import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    Algorithms algorithms = new Algorithms();

    @Test
    void testKMPFound() {
        String text = "abcxabcdabxabcdabcdabcy";
        assertDoesNotThrow(() -> algorithms.KMP(text, "abcdabcy"));
        assertDoesNotThrow(() -> algorithms.KMP(text, "absence"));
    }

    @Test
    void testBMFound() {
        String text = "test BMF found: 1, 2, 3";
        assertDoesNotThrow(() -> algorithms.BM(text, "test"));
        assertDoesNotThrow(() -> algorithms.BM(text, "absence"));
        assertDoesNotThrow(() -> algorithms.BM(text, ": 1"));
        assertDoesNotThrow(() -> algorithms.BM(text, "2"));
    }

    @Test
    void testCreatePi() {
        String pattern = "ab12fab1";
        int[] expected = {0, 0, 0, 0, 0, 1, 2, 3};
        assertArrayEquals(expected, algorithms.createPi(pattern));
    }
    @Test
    void testCreateBMNext() {
        String pattern = "ABBABBBA";
        int[] expected = {7, 7, 7, 7, 7, 4, 7, 7, 1};
        assertArrayEquals(expected, algorithms.createBMNext(pattern));
    }

    @Test
    void testCreateLast() {
        int[] last = algorithms.createLAST("algorithm01");
        assertEquals(1, last['l']);
        assertEquals(4, last['r']);
        assertEquals(-1, last['w']);
        assertEquals(9, last['0']);

    }
}