package com.revature;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

//we will create mock db using mockito
import static org.mockito.Mockito.*;

//NOTE: this is just introducing JUnit testing. 
// DO NOT add any more test files
// until we finish the full original structure in main
// because our test structure must match main structure

// to run tests, use command mvn test from bank dir
public class APITest {

    private API theAPI;

    @BeforeEach 
    void setup() {
        theAPI = new API();
    }

    @Test 
    @DisplayName ("API object should not be null")
    void testNullAPIObject() {
        assertNotNull(theAPI);
    }
    
}
