package validating;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class ConstantsTest {
    @BeforeEach
    public void beforeEach(TestInfo info){
        System.out.println("Executing test: " + info.getDisplayName());
    }

    @ParameterizedTest(name = "Test for arguments {arguments}")
    @DisplayName("Testing constants")
    @MethodSource("provideConstants")
    public void testConstants(String constantName, Object constant, Object expected){
        System.out.printf("\tTest for constant %s (%s is expected) - ", constantName, expected);
        Assertions.assertEquals(constant, expected, "Failed!");
        System.out.println("Done with actual = " + constant + "!");
    }

    public static Stream<Arguments> provideConstants(){
        return Stream.of(Arguments.of("YEAR_IN_MILLISECONDS",  Constants.YEAR_IN_MILLISECONDS, 31540000000L),
                         Arguments.of("ADULTHOOD_AGE",  Constants.ADULTHOOD_AGE, 18),
                         Arguments.of("MAX_BOOK_PRICE",  Constants.MAX_BOOK_PRICE, 999.99));
    }
}
