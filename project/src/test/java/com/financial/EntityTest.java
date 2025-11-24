import java.lang.reflect.Field;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {
    @BeforeEach
    void resetEntitiesList() throws Exception {
        Field field = Entity.class.getDeclaredField("entities");
        field.setAccessible(true);
        field.set(null, new ArrayList<Entity>());
    }

    @Test
    void testConstructorAddsEntityToList() throws Exception {
        assertEquals(0, getEntities().size());

        Entity e = new Entity("E001", "Finance Dept");

        assertEquals(1, getEntities().size());
        assertSame(e, getEntities().get(0));
    }

    @Test
    void testGetters() {
        Entity e = new Entity("E002", "HR");

        assertEquals("E002", e.getEntityCode());
        assertEquals("HR", e.getName());
    }

    @Test
    void testFindEntityWithEntityCode_Found() {
        Entity e1 = new Entity("E001", "Finance");
        Entity e2 = new Entity("E002", "HR");

        Entity found = Entity.findEntityWithEntityCode("E002");

        assertNotNull(found);
        assertEquals("HR", found.getName());
    }

    @Test
    void testFindEntityWithEntityCode_NotFound() {
        new Entity("E001", "Finance");

        Entity found = Entity.findEntityWithEntityCode("UNKNOWN");

        assertNull(found);
    }

    @Test
    void testToString() {
        Entity e = new Entity("E003", "IT");

        String expected = "Entity Code: E003, Name: IT";
        assertEquals(expected, e.toString());
    }

    // Helper: reflection access to private static list
    @SuppressWarnings("unchecked")
    private ArrayList<Entity> getEntities() throws Exception {
        Field field = Entity.class.getDeclaredField("entities");
        field.setAccessible(true);
        return (ArrayList<Entity>) field.get(null);
    }
}
