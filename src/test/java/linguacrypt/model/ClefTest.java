package linguacrypt.model;

import com.google.zxing.common.BitMatrix;
import javafx.scene.image.WritableImage;
import linguacrypt.config.GameConfig;
import linguacrypt.utils.CardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ClefTest {
    private Clef clef;

    @BeforeEach
    void setUp() {
        clef = new Clef(5, 5);
    }

    @Test
    void constructor_initializesCorrectly() {
        assertEquals(5, clef.getWidth());
        assertEquals(5, clef.getHeight());
        assertNotNull(clef.getGrid());
        assertEquals(4, clef.getCardsCounts().length);
    }

    @Test
    void bitMatrixToImage_convertsCorrectly() throws Exception {
        BitMatrix bitMatrix = clef.to_qrcode();
        WritableImage image = clef.bitMatrixToImage(bitMatrix);
        assertNotNull(image);
        assertEquals(300, (int) image.getWidth());
        assertEquals(300, (int) image.getHeight());
    }

    @Test
    void getCardType_returnsCorrectType() {
        CardType[][] grid = clef.getGrid();
        assertEquals(grid[0][0], clef.getCardType(0, 0));
    }

    @Test
    void prettyPrint_outputsCorrectFormat() {
        clef.prettyPrint();
        // No assertion needed, just ensure no exceptions are thrown
    }

    @Test
    void toString_returnsCorrectString() {
        String result = clef.toString();
        assertNotNull(result);
        assertTrue(result.contains(GameConfig.BLUE_STARTS_TEXT_QRCODE) || result.contains(GameConfig.RED_STARTS_TEXT_QRCODE));
    }

    @Test
    void to_qrcode_generatesValidQRCode() throws Exception {
        BitMatrix bitMatrix = clef.to_qrcode();
        assertNotNull(bitMatrix);
    }

    @Test
    void write_qrcode_writesFile() throws Exception {
        clef.write_qrcode();
        File file = new File(GameConfig.QRCODE_PATH);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }
}
