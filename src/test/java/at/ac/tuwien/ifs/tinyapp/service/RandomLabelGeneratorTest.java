package at.ac.tuwien.ifs.tinyapp.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:applicationContext.xml" })
public class RandomLabelGeneratorTest {

    @Autowired
    RandomLabelGenerator randomLabelGenerator;

    @Test
    public void generate_returnsValidLabel() throws Exception {
        Random rng = mock(Random.class);
        when(rng.nextInt(anyInt())).thenReturn(0);

        randomLabelGenerator.setRandomNumberGenerator(rng);

        assertEquals(RandomLabelGenerator.LABELS[0], randomLabelGenerator.generate());
    }
}
